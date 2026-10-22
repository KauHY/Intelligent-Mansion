package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.DeviceInfoReqDTO;
import com.crlink.smartpark.device.dto.DeviceSyncDTO;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.entity.DeviceInfoHouse;
import com.crlink.smartpark.device.mapper.DeviceInfoMapper;
import com.crlink.smartpark.device.query.DeviceInfoQuery;
import com.crlink.smartpark.device.query.ScreenDeviceQuery;
import com.crlink.smartpark.device.service.DeviceInfoHouseService;
import com.crlink.smartpark.device.service.DeviceInfoService;
import com.crlink.smartpark.device.util.DeviceSyncUtil;
import com.crlink.smartpark.device.vo.DeviceChannelVO;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.feign.AccessPolicyProvider;
import com.crlink.smartpark.visitor.feign.ApartmentPersonProvider;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.DeviceCategory.DOOR_ACCESS;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.*;
import static com.crlink.smartpark.common.constants.enums.RocketmqConstant.RocketmqTagEnum.*;
import static com.crlink.smartpark.common.constants.enums.RocketmqConstant.RocketmqTopicEnum.DEVICE_TOPIC;

/**
 * <p>
 * 设备信息表 服务实现类
 * </p>
 * */
@Service("deviceInfoService")
@Slf4j
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements DeviceInfoService {

    @Value("${device.common.list.url}")
    private String deviceListUrl;
    @Resource
    protected CommonProvider commonProvider;
    @Resource
    private DeviceInfoHouseService deviceInfoHouseService;
    @Resource
    private ApartmentPersonProvider apartmentPersonProvider;
    @Resource
    private AccessPolicyProvider accessPolicyProvider;

    @Override
    public <T> Map<String, T> id2Value(Set<String> ids, SFunction<DeviceInfo, T> function) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return this.lambdaQuery()
                .select(DeviceInfo::getId, function)
                .in(DeviceInfo::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(DeviceInfo::getId, function));
    }

    @Override
    public Map<String, String> getIdByDeviceNames(Set<String> deviceNames, Integer deviceType, String parkOrgId) {
        if (ObjectUtil.isEmpty(deviceNames)) {
            return Collections.emptyMap();
        }
        Map<String, String> result = this.lambdaQuery()
                .select(DeviceInfo::getId, DeviceInfo::getDeviceName, DeviceInfo::getDeviceId)
                .eq(DeviceInfo::getDeviceType, deviceType)
                .eq(DeviceInfo::getParkOrgId, parkOrgId)
                .in(DeviceInfo::getDeviceName, deviceNames)
                .list().parallelStream().collect(Collectors.toMap(DeviceInfo::getDeviceName, DeviceInfo::getId));
        return result;
    }

    @Override
    public Map<String, String> getIdByDeviceIds(Set<String> deviceIds, Integer deviceType) {
        if (ObjectUtil.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        Map<String, String> result = this.lambdaQuery()
                .select(DeviceInfo::getId, DeviceInfo::getDeviceId).eq(DeviceInfo::getDeviceType, deviceType)
                .in(DeviceInfo::getDeviceId, deviceIds)
                .list().parallelStream().collect(Collectors.toMap(DeviceInfo::getDeviceId, DeviceInfo::getId));
        return result;
    }

    @Override
    public Map<String, Integer> getDeviceStatusByIds(Set<String> ids) {
        return this.id2Value(ids, DeviceInfo::getStatus);
    }

    @Override
    public boolean removeDeviceById(String id) {
        String deviceId = this.id2Value(Sets.newHashSet(id), DeviceInfo::getDeviceId).get(id);
        RocketmqUtil.sendSyncMessage(DEVICE_TOPIC, DELETE, Lists.newArrayList(deviceId));
        boolean apartmentExists = apartmentPersonProvider.existsDevice(id);
        boolean policyExists = accessPolicyProvider.existsDevice(id);
        AssertUtil.requireTrue(!(apartmentExists || policyExists), "60016");
        deviceInfoHouseService.remove(Wrappers.<DeviceInfoHouse>lambdaQuery()
                .eq(DeviceInfoHouse::getDeviceInfoId, id));
        return this.removeById(id);
    }

    @Override
    @Transactional
    public Boolean fireDeviceSync() {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        String parkOrgId = parkOrgIds.stream().findAny().orElseThrow(() -> new ResolinkRuntimeException("20016"));
        List<DeviceInfo> fireControlDevices = getFireControlDevices(parkOrgId);
        if (CollUtil.isEmpty(fireControlDevices)) {
            log.error("消防设备为空，parkOrgId:{}", parkOrgId);
            return false;
        }
        this.remove(Wrappers.<DeviceInfo>lambdaQuery().eq(DeviceInfo::getDeviceType, SMOKE_SENSING.type).eq(DeviceInfo::getParkOrgId, parkOrgId));
        this.saveBatch(fireControlDevices);
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateDevice(DeviceInfoReqDTO dto) {
        dto.setTenantId(SystemUtil.getInstance().getCurrentHeadOrgId());
        checkDeviceName(dto.getId(), dto.getParkOrgId(), dto.getDeviceType(), dto.getDeviceName());
        //checkDeviceIp(dto); //产品要求去除IP判重逻辑，20220628
        checkDeviceId(dto.getId(), dto.getParkOrgId(), dto.getDeviceType(), dto.getDeviceId());
        DeviceInfo deviceInfo = MyBeanUtils.copy(dto, DeviceInfo::new);
        EntityUserUtil.setAllDynamic(deviceInfo);
        if (StringUtils.isNotBlank(deviceInfo.getId())) {
            this.updateById(deviceInfo);
            //智能锁关联房间
            if (SMART_LOCK.type == dto.getDeviceType() && CollectionUtil.isNotEmpty(dto.getLockChanelList())) {
                List<DeviceChannelVO> lockChanelList = dto.getLockChanelList();
                List<DeviceInfoHouse> deviceInfoHouses = new LinkedList<>();
                lockChanelList.forEach(t -> {
                    if (StringUtils.isNotBlank(t.getHouseId())) {
                        DeviceInfoHouse deviceInfoHouse = MyBeanUtils.copy(dto, DeviceInfoHouse::new, "id");
                        deviceInfoHouse.setDeviceInfoId(dto.getId());
                        deviceInfoHouse.setChannelNo(t.getChannelNo());
                        deviceInfoHouse.setHouseId(t.getHouseId());
                        deviceInfoHouses.add(deviceInfoHouse);
                    }
                });
                deviceInfoHouseService.remove(Wrappers.<DeviceInfoHouse>lambdaQuery()
                        .eq(DeviceInfoHouse::getDeviceInfoId, dto.getId()));
                if (CollectionUtil.isNotEmpty(deviceInfoHouses)) {
                    deviceInfoHouseService.saveBatch(deviceInfoHouses);
                }
            }
            RocketmqUtil.sendSyncMessage(DEVICE_TOPIC, UPDATE, deviceInfo);
        } else {
            this.save(deviceInfo);
            RocketmqUtil.sendSyncMessage(DEVICE_TOPIC, SAVE, deviceInfo);
        }
    }


    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    @Id2objHandle
    public DeviceInfoVO getDeviceInfoById(String id) {
        if (StrUtil.isNotBlank(id)) {
            DeviceInfo entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            DeviceInfoVO dto = new DeviceInfoVO();
            BeanUtils.copyProperties(entity, dto);
            getLockChannelList(dto, entity);
            return dto;
        }
        return null;
    }

    @Override
    public DeviceInfoVO getByDeviceId(String deviceId) {
        if (StrUtil.isNotBlank(deviceId)) {
            DeviceInfo entity = this.getOne(Wrappers.<DeviceInfo>lambdaQuery().eq(DeviceInfo::getDeviceId, deviceId));
            if (Objects.nonNull(entity)) {
                DeviceInfoVO dto = new DeviceInfoVO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    private void getLockChannelList(DeviceInfoVO dto, DeviceInfo entity) {
        List<DeviceChannelVO> result = new LinkedList<>();
        String buildingId = null;
        if (CollectionUtil.isNotEmpty(entity.getChannelNos()) && CollectionUtil.isNotEmpty(entity.getExtension())) {
            Object obj = entity.getExtension().get("channelList");
            if (Objects.nonNull(obj)) {
                JSONArray channelList = JSONUtil.parseArray(obj.toString());
                if (CollectionUtil.isNotEmpty(channelList)) {
                    for (int i = 0; i < channelList.size(); i++) {
                        JSONObject t = (JSONObject) channelList.get(i);
                        DeviceChannelVO vo = new DeviceChannelVO();
                        vo.setChannelNo(t.getStr("channelCode"));
                        vo.setChannelSn(t.getStr("channelSn"));
                        vo.setChannelName(t.getStr("channelName"));
                        result.add(vo);
                    }
                }
                List<DeviceInfoHouse> list = deviceInfoHouseService.lambdaQuery().select(
                        DeviceInfoHouse::getId, DeviceInfoHouse::getBuildingId, DeviceInfoHouse::getFloor,
                        DeviceInfoHouse::getHouseId, DeviceInfoHouse::getChannelNo)
                        .eq(DeviceInfoHouse::getDeviceInfoId, entity.getId()).list();
                if (CollectionUtil.isNotEmpty(list)) {
                    for (DeviceInfoHouse t : list) {
                        result.forEach(vo -> {
                            if (t.getChannelNo().equals(vo.getChannelNo())) {
                                vo.setHouseId(t.getHouseId());
                            }
                        });
                    }
                    buildingId = list.get(0).getBuildingId();
                }

            }
            dto.setBuildingId(buildingId);
            dto.setLockChanelList(result);
        }
    }

    /**
     * 设备下拉列表
     *
     * @return
     */
    @Override
    public List<DictVO> listDropDownDevices(String parkOrgId, List<Integer> deviceTypes) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        if (StringUtils.isBlank(parkOrgId) || CollectionUtil.isEmpty(deviceTypes)
                || CollectionUtil.isEmpty(parkOrgIds) || !parkOrgIds.contains(parkOrgId)) {
            return Lists.newArrayList();
        }
        return this.lambdaQuery().select(DeviceInfo::getId, DeviceInfo::getDeviceName)
                .eq(DeviceInfo::getParkOrgId, parkOrgId)
                .in(DeviceInfo::getDeviceType, deviceTypes)
                .orderByDesc(DeviceInfo::getCreateTime)
                .list().stream().map(deviceInfo -> new DictVO(deviceInfo.getDeviceName(), deviceInfo.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Id2objHandle
    public DeviceInfoVO getByDeviceCode(String deviceCode) {
        DeviceInfo deviceInfo = lambdaQuery()
                .eq(DeviceInfo::getDeviceId, deviceCode)
                .one();
        return MyBeanUtils.copy(deviceInfo, DeviceInfoVO::new);
    }

    @Override
    public String getMeetingScreenshot(String id) {
        DeviceInfo deviceInfo = this.getById(id);
        // 下发送指令到会议屏进行截屏，取最新的截屏图片
        MeetingScreenUtils.termSnapshot(deviceInfo.getDeviceId(), 0);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("6666唤醒异常！", e);
        }
        String screenshots = MeetingScreenUtils.getScreenshotUrl(deviceInfo.getDeviceId());
        return screenshots;
    }

    @Override
    public Boolean sleepDevice(String id) {
        DeviceInfo deviceInfo = this.getById(id);
        Boolean result = MeetingScreenUtils.sleepDevice(deviceInfo.getDeviceId());
        return result;
    }

    @Override
    public Boolean wakeUpDevice(String id) {
        DeviceInfo deviceInfo = this.getById(id);
        Boolean result = MeetingScreenUtils.wakeUpDevice(deviceInfo.getDeviceId());
        return result;
    }

    @Override
    @Id2objHandle
    public PageInfo<DeviceInfoVO> listScreenDevicePage(ScreenDeviceQuery query) {
        CompletableFuture.runAsync(() -> {
            updateScreenDeviceStatus();
        });
        DeviceInfoQuery infoQuery = MyBeanUtils.copy(query, DeviceInfoQuery::new);
        query.startPage();
        infoQuery.setDeviceType(MEETING_SCREEN.type);
        infoQuery.setStatus(query.getScreenDeviceStatus());
        LambdaQueryWrapper<DeviceInfo> wrapper = buildPageListRecordWrapper(infoQuery);
        List<DeviceInfo> list = list(wrapper);
        List<DeviceInfoVO> result = MyBeanUtils.copyList(list, DeviceInfoVO::new);
        return query.createPageInfo(result);
    }

    @Override
    public void updateScreenDeviceStatus() {
        Map<String, Set<String>> map = null;
        try {
            map = MeetingScreenUtils.getTermStatus();
        } catch (Exception e) {
            log.error("接口调用失败", e);
            return;
        }
        Set<String> sleepDeviceIds = map.getOrDefault("Shutdown", Sets.newHashSet());
        Set<String> runningDeviceIds = map.getOrDefault("Running", Sets.newHashSet());
        Set<String> backgroundDeviceIds = map.getOrDefault("Background", Sets.newHashSet());
        Set<String> offlineDeviceIds = map.getOrDefault("Offline", Sets.newHashSet());
        Map<String, Integer> deviceIdStatusMap = new HashMap<>();
        sleepDeviceIds.forEach(t -> deviceIdStatusMap.put(t, DictConstant.ScreenDeviceStatus.SLEEP.ordinal()));
        runningDeviceIds.forEach(t -> deviceIdStatusMap.put(t, DictConstant.ScreenDeviceStatus.RUNNING.ordinal()));
        backgroundDeviceIds.forEach(t -> deviceIdStatusMap.put(t, DictConstant.ScreenDeviceStatus.RUNNING.ordinal()));
        offlineDeviceIds.forEach(t -> deviceIdStatusMap.put(t, DictConstant.ScreenDeviceStatus.OFFLINE.ordinal()));
        Map<String, DeviceInfo> infoMap = this.lambdaQuery().select(DeviceInfo::getId, DeviceInfo::getDeviceId)
                .eq(DeviceInfo::getDeviceType, MEETING_SCREEN.type)
                .list().parallelStream().collect(Collectors.toMap(DeviceInfo::getDeviceId, Function.identity()));
        infoMap.forEach((k, v) -> {
            Integer status = deviceIdStatusMap.getOrDefault(k, DictConstant.ScreenDeviceStatus.OFFLINE.ordinal());
            v.setStatus(status);

        });
        this.saveOrUpdateBatch(infoMap.values());
    }


    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<DeviceInfoVO> listPage(DeviceInfoQuery query) {
        query.startPage();
        LambdaQueryWrapper<DeviceInfo> wrapper = buildPageListRecordWrapper(query);
        List<DeviceInfo> list = list(wrapper);
        List<DeviceInfoVO> result = MyBeanUtils.copyList(list, DeviceInfoVO::new);
        return query.createPageInfo(result);
    }

    @Override
    public Map<String, Set<String>> findChannelNosByHouseIds(Set<String> houseIds) {
        if (CollectionUtil.isEmpty(houseIds)) {
            return Collections.emptyMap();
        }
        List<DeviceInfoHouse> list = deviceInfoHouseService.lambdaQuery().select(DeviceInfoHouse::getId,
                DeviceInfoHouse::getChannelNo, DeviceInfoHouse::getHouseId, DeviceInfoHouse::getDeviceInfoId)
                .in(DeviceInfoHouse::getHouseId, houseIds).list();
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Set<String> allDeviceInfoIds = list.stream().map(DeviceInfoHouse::getDeviceInfoId).collect(Collectors.toSet());
        Set<String> existDeviceInfoIds = this.lambdaQuery().select(DeviceInfo::getId)
                .in(DeviceInfo::getId, allDeviceInfoIds).list()
                .stream().map(DeviceInfo::getId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(existDeviceInfoIds)) {
            return Collections.emptyMap();
        }
        return list.stream().filter(t -> existDeviceInfoIds.contains(t.getDeviceInfoId()))
                .collect(Collectors.groupingBy(DeviceInfoHouse::getDeviceInfoId,
                        Collectors.mapping(DeviceInfoHouse::getChannelNo, Collectors.toSet())));
    }


    @Override
    public void updateDeviceStatus() {

    }
    /**
     * 更新水表、电表设备的状态（定时任务使用）
     */
    public void updateDeviceStatus1() {
        if (StringUtils.isBlank(deviceListUrl)) {
            return;
        }
        List<Integer> devTypes = Lists.newArrayList(WATER_METER.type, ELECTRICITY_METER.type);
        Map<String, String> deviceMap = this.list(Wrappers.<DeviceInfo>lambdaQuery().in(DeviceInfo::getDeviceType, devTypes))
                .stream().collect(Collectors.toMap(DeviceInfo::getDeviceId, DeviceInfo::getId, (o, n) -> n));
        for (Integer devType : devTypes) {
            String url = StrUtil.format(deviceListUrl, devType);
            log.info("获取设备列表url: {}", url);
            try {
                String respStr = HttpUtil.get(url);
                JSONObject respJson = JSONUtil.parseObj(respStr);
                if (Objects.equals(respJson.getInt("code"), HttpStatus.OK.value())) {
                    JSONArray dataArr = respJson.getJSONArray("data");
                    if (dataArr == null || dataArr.size() == 0) {
                        log.info("获取设备列表为空");
                        return;
                    }
                    List<DeviceInfo> deviceInfos = new ArrayList<>(dataArr.size());
                    for (int i = 0; i < dataArr.size(); i++) {
                        JSONObject jsonObj = dataArr.getJSONObject(i);
                        String deviceId = jsonObj.getStr("DeviceID");
                        String id = deviceMap.get(deviceId);
                        if (StrUtil.isBlank(id)) {
                            continue;
                        }
                        DeviceInfo deviceInfo = new DeviceInfo();
                        deviceInfo.setId(id);
                        deviceInfo.setStatus(jsonObj.getInt("Status"));
                        deviceInfos.add(deviceInfo);
                    }
                    log.info("更新设备{}状态，共{}条，结果{}", devType, deviceInfos.size(), this.updateBatchById(deviceInfos));
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    /**
     * 封装查询条件
     *
     * @param query 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryWrapper<DeviceInfo> buildPageListRecordWrapper(DeviceInfoQuery query) {
        LambdaQueryWrapper<DeviceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeviceInfo::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds())
                .in(CollUtil.isNotEmpty(query.getDeviceTypes()), DeviceInfo::getDeviceType, query.getDeviceTypes())
                .eq(Objects.nonNull(query.getDeviceCategory()), DeviceInfo::getDeviceCategory, query.getDeviceCategory())
                .eq(Objects.nonNull(query.getDeviceId()), DeviceInfo::getDeviceId, query.getDeviceId())
                .eq(Objects.nonNull(query.getDeviceType()), DeviceInfo::getDeviceType, query.getDeviceType())
                .eq(StringUtils.isNotBlank(query.getIpAddress()), DeviceInfo::getIpAddress, query.getIpAddress())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), DeviceInfo::getParkOrgId, query.getParkOrgId())
                .eq(Objects.nonNull(query.getStatus()), DeviceInfo::getStatus, query.getStatus())
                .ge(query.getStartTime() != null, DeviceInfo::getCreateTime, query.getStartTime())
                .le(query.getEndTime() != null, DeviceInfo::getCreateTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getDeviceName()), DeviceInfo::getDeviceName, query.getDeviceName())
                .orderByDesc(DeviceInfo::getCreateTime);
        return queryWrapper;
    }



    protected void checkDeviceName(String id, String parkOrgId, Integer deviceType, String deviceName) {
        AssertUtil.requireNull(this.getOne(Wrappers.<DeviceInfo>query().lambda().select(DeviceInfo::getId)
                .ne(StringUtils.isNotBlank(id), DeviceInfo::getId, id)
                .eq(DeviceInfo::getParkOrgId, parkOrgId)
                .eq(DeviceInfo::getDeviceName, deviceName.trim()).last("limit 1")), "60001");
    }

    //产品要求去除IP判重逻辑，20220628
    //protected void checkDeviceIp(DeviceInfoReqDTO dto) {
    //    AssertUtil.requireNull(this.getOne(Wrappers.<DeviceInfo>query().lambda().select(DeviceInfo::getId)
    //            .ne(StringUtils.isNotBlank(dto.getId()), DeviceInfo::getId, dto.getId())
    //            .eq(DeviceInfo::getParkOrgId, dto.getParkOrgId())
    //            .eq(DeviceInfo::getIpAddress, dto.getIpAddress()).last("limit 1")), "60003");
    //}

    protected void checkDeviceId(String id, String parkOrgId, Integer deviceType, String deviceId) {
        AssertUtil.requireTrue(StringUtils.isNotBlank(deviceId) && deviceId.length() <= 30, "60007");
        AssertUtil.requireNull(this.getOne(Wrappers.<DeviceInfo>query().lambda().select(DeviceInfo::getId)
                .ne(StringUtils.isNotBlank(id), DeviceInfo::getId, id)
                .eq(DeviceInfo::getDeviceType, deviceType)
                .eq(DeviceInfo::getDeviceId, deviceId).last("limit 1")), "60002");
    }

    private List<DeviceInfo> getFireControlDevices(String parkOrgId) {
        log.info("获取消防设备同步开始！");
        if (StringUtils.isBlank(deviceListUrl)) {
            log.error("消防设备同步url为空");
            return null;
        }
        List<DeviceInfo> deviceInfos = null;
        String url = StrUtil.format(deviceListUrl, SMOKE_SENSING.type);
        log.info("请求消防url: {}", url);
        try {
            String respStr = HttpUtil.get(url);
            log.info("请求消防的响应: {}", (StrUtil.isNotBlank(respStr) && respStr.length() > 100) ? respStr.substring(0, 100) : respStr);
            JSONObject respJson = JSONUtil.parseObj(respStr);
            if (Objects.equals(respJson.getInt("code"), HttpStatus.OK.value())) {
                JSONArray dataArr = respJson.getJSONArray("data");
                if (CollUtil.isNotEmpty(dataArr)) {
                    deviceInfos = dataArr.parallelStream().map(t -> {
                        JSONObject t1 = (JSONObject) t;
                        DeviceInfo deviceInfo = new DeviceInfo();
                        deviceInfo.setParkOrgId(parkOrgId);
                        deviceInfo.setDeviceType(SMOKE_SENSING.type);
                        deviceInfo.setStatus(t1.getInt("Status"));
                        deviceInfo.setDeviceId(t1.getStr("DeviceID"));
                        deviceInfo.setDeviceName(t1.getStr("DeviceName"));
                        return deviceInfo;
                    }).collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            log.error("消防设备同步出错", e);
        }
        log.info("获取消防设备同步结束！");
        return deviceInfos;
    }

}
