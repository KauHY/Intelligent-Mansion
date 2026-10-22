package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.DeviceInfoReqDTO;
import com.crlink.smartpark.device.dto.MeterDTO;
import com.crlink.smartpark.device.dto.MeterInfoDTO;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.model.MeterExchangeReqDTO;
import com.crlink.smartpark.device.model.MeterReqDTO;
import com.crlink.smartpark.device.model.MeterStatusUpdateReqDTO;
import com.crlink.smartpark.device.model.excel.MeterExcelExportDTO;
import com.crlink.smartpark.device.model.excel.MeterExcelImportDTO;
import com.crlink.smartpark.device.model.query.MeterQuery;
import com.crlink.smartpark.device.service.MeterConsumptionRecordService;
import com.crlink.smartpark.device.service.MeterService;
import com.crlink.smartpark.system.feign.BuildingProvider;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_FORMAT;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.ELECTRICITY_METER;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.WATER_METER;
import static com.crlink.smartpark.device.common.MeterConstant.E_METER_EXPORT_FILENAME;
import static com.crlink.smartpark.device.common.MeterConstant.MeteTypeEnum.GENERAL_METER;
import static com.crlink.smartpark.device.common.MeterConstant.MeteTypeEnum.INTELLIGENT_METER;
import static com.crlink.smartpark.device.common.MeterConstant.MeterStatusEnum.*;
import static com.crlink.smartpark.device.common.MeterConstant.W_METER_EXPORT_FILENAME;


/**
 * 仪表（电表和水表） 服务实现类
 *
  */
@Service
@Slf4j
public class MeterServiceImpl extends DeviceInfoServiceImpl implements MeterService {


    //电表编号前缀
    public static final String ELECTRICITY_METER_NUMBER = "EM";
    //水表编号前缀
    public static final String WATER_METER_NUMBER = "WM";
    public static final String DEVICE_ID = "DEVICE_ID";

    @Value("${device.electric.enable.url:}")
    private String electricEnableUrl;
    @Value("${device.electric.disable.url:}")
    private String electricDisableUrl;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private HouseProvider hoseProvider;

    @Autowired
    private BuildingProvider buildingProvider;

    @Autowired
    private MeterConsumptionRecordService meterConsumptionRecordService;

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, Function<MeterDTO, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DeviceInfo> devices = this.lambdaQuery().in(DeviceInfo::getDeviceType, WATER_METER.type, ELECTRICITY_METER.type)
                .in(DeviceInfo::getId, ids).list();
        List<MeterDTO> meters = entitiesToDTOs(devices);
        Map<String, T> result = meters.parallelStream().
                collect(HashMap::new, (map, meter) -> map.put(meter.getId(), sValue.apply(meter)), HashMap::putAll);

        return result;
    }

    @Override
    public Map<String, MeterDTO> queryMeterByDeviceIds(Collection<String> deviceIds) {
        if (ObjectUtils.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        List<DeviceInfo> devices = this.lambdaQuery().in(DeviceInfo::getDeviceType, WATER_METER.type, ELECTRICITY_METER.type)
                .in(DeviceInfo::getDeviceId, deviceIds).list();
        List<MeterDTO> meters = entitiesToDTOs(devices);
        Map<String, MeterDTO> result = meters.parallelStream().
                collect(Collectors.toMap(MeterDTO::getDeviceId, Function.identity()));
        return result;
    }


    @Override
    public Collection<String> getMeterDeviceIdByCondition(MeterQuery query) {
        query.setParkOrgIds(Sets.newHashSet(query.getParkOrgId()));
        LambdaQueryWrapper<DeviceInfo> wrapper = getMeterConditionWrapper(query, DeviceInfo::getDeviceId);
        List<DeviceInfo> list = list(wrapper);
        Set<String> deviceIds = list.parallelStream().map(DeviceInfo::getDeviceId).collect(Collectors.toSet());
        return deviceIds;
    }

    /**
     * 保存或更新数据
     *
     * @return
     */
    @Override
    public boolean saveOrUpdateMeter(MeterReqDTO reqDTO) {
        DeviceInfoReqDTO deviceInfoReqDTO = MyBeanUtils.copy(reqDTO, DeviceInfoReqDTO::new);
        if (StringUtils.isBlank(reqDTO.getDeviceId())) {
            String deviceId = generateDeviceId();
            deviceInfoReqDTO.setDeviceId(deviceId);
        }
        MeterInfoDTO infoDTO = MyBeanUtils.copy(reqDTO, MeterInfoDTO::new);
        Map<String, Object> map = BeanUtil.beanToMap(infoDTO);
        deviceInfoReqDTO.setExtension(map);
        super.saveOrUpdateDevice(deviceInfoReqDTO);
        return true;
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeterDTO getMeterById(String id) {
        DeviceInfo entity = Optional.ofNullable(super.getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    @Override
    public MeterDTO getMeterByDeviceId(String parkOrgId, String deviceId) {
        DeviceInfo entity = super.lambdaQuery().eq(DeviceInfo::getParkOrgId, parkOrgId).eq(DeviceInfo::getDeviceId, deviceId).one();
        return Objects.isNull(entity) ? null : entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeterDTO> listMeterPage(MeterQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.setParkOrgIds(parkOrgIds);
        query.startPage();
        LambdaQueryWrapper<DeviceInfo> wrapper = getMeterConditionWrapper(query);
        List<DeviceInfo> list = list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<MeterDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean exchangeMeter(MeterExchangeReqDTO reqDTO) {
        DeviceInfo oldMeter = this.getById(reqDTO.getOldMeterId());
        if (!DISCARD.getStatus().equals(oldMeter.getUsingStatus())) {
            throw new ResolinkRuntimeException("10020");
        }
        DeviceInfo newMeter = MyBeanUtils.copy(oldMeter, DeviceInfo::new, "id", "createTime");
        newMeter.setDeviceType(oldMeter.getDeviceType());
        String deviceId;
        if (StringUtils.isNotBlank(reqDTO.getNewDeviceId())) {
            deviceId = reqDTO.getNewDeviceId();
            this.checkDeviceId(null, oldMeter.getParkOrgId(), oldMeter.getDeviceType(), deviceId);
        } else {
            String oldDeviceId = oldMeter.getDeviceId();
            char charOld = oldDeviceId.charAt(DEVICE_ID.length() + 1);
            deviceId = StringUtils.replaceChars(oldDeviceId, charOld, (char) (charOld + 1));
        }
        oldMeter.setUsingStatus(DISCARD.getStatus());
        oldMeter.setUpdateTime(null);
        EntityUserUtil.setUpdateBy(oldMeter);
        meterConsumptionRecordService.exchangeMeterSaveConsumptionRecord(oldMeter, reqDTO.getOldReading());
        this.updateById(oldMeter);
        newMeter.setDeviceId(deviceId);
        Map<String, Object> extension = oldMeter.getExtension();
        extension.put("maxRange", reqDTO.getNewMaxRange());
        extension.put("initialReading", reqDTO.getNewReading());
        newMeter.setExtension(extension);
        EntityUserUtil.setAll(newMeter);
        newMeter.setUsingStatus(USING.getStatus());
        return this.save(newMeter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateMeterStatus(MeterStatusUpdateReqDTO reqDTO) {
        String id = reqDTO.getId();
        Integer updateStatus = reqDTO.getUsingStatus();
        DeviceInfo deviceInfo = this.getById(id);
        AssertUtil.requireNotNull(deviceInfo, "100071");
        Integer deviceCurStatus = Optional.ofNullable(deviceInfo.getUsingStatus()).orElseThrow(() -> new ResolinkRuntimeException("10004", "水电表状态null"));
        boolean matchFlag = (deviceCurStatus == USING.status && updateStatus == STOP_USING.status)
                || (deviceCurStatus == STOP_USING.status && (updateStatus == USING.status || updateStatus == DISCARD.status))
                || (deviceCurStatus == DISCARD.status && updateStatus == REPLACE.status);
        AssertUtil.requireTrue(matchFlag, "10020");
        String errMsg = null;
        if (updateMeterStatus(id, updateStatus)) {
            if (ELECTRICITY_METER.type.equals(deviceInfo.getDeviceType())) {
                String url = USING.getStatus().equals(updateStatus) ? electricEnableUrl : electricDisableUrl;
                AssertUtil.requireTrue(StrUtil.isNotBlank(url), "10003");
                url = StrUtil.format(url, deviceInfo.getDeviceId());
                log.info("控制电表url: {}", url);
                try {
                    String respStr = HttpUtil.get(url);
                    log.info("控制电表响应: {}", respStr);
                    JSONObject respJson = JSONUtil.parseObj(respStr);
                    if (!Objects.equals(respJson.getInt("code"), HttpStatus.OK.value())) {
                        errMsg = respJson.getStr("message");
                        updateMeterStatus(id, deviceCurStatus);
                    }
                } catch (Exception e) {
                    log.error("", e);
                    errMsg = "请求远程电表失败";
                    updateMeterStatus(id, deviceCurStatus);
                }
            }
        } else {
            errMsg = "修改状态失败";
        }
        return StrUtil.isBlank(errMsg) ? ResponseResult.success() : ResponseResult.errorMsg(errMsg);
    }

    private boolean updateMeterStatus(String id, Integer status) {
        return lambdaUpdate().set(DeviceInfo::getUsingStatus, status)
                .set(DeviceInfo::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .set(DeviceInfo::getUpdateTime, LocalDateTime.now())
                .eq(DeviceInfo::getId, id).update();
    }

    @Override
    public Boolean removeMeterById(String id) {
        return removeById(id);
    }


    @Override
    public void download(MeterQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        query.setParkOrgIds(parkOrgIds);
        String fileName = query.getDeviceType() == ELECTRICITY_METER.type ? E_METER_EXPORT_FILENAME : W_METER_EXPORT_FILENAME;
        ExcelExportUtils.export(() -> list(getMeterConditionWrapper(query)),
                entities -> {
                    List<MeterDTO> meterDTOS = entitiesToDTOs(entities);
                    return MyBeanUtils.copyList(meterDTOS, MeterExcelExportDTO::new, (s, t) -> {
                        if (query.getDeviceType() == WATER_METER.type) {
                            t.setEffectType(7);
                        }
                    });
                },
                fileName, MeterExcelExportDTO.class);
    }

    @Override
    public FileImportErrorInfoDTO importMeters(MultipartFile file, Integer deviceTye, String parkOrgId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<MeterExcelImportDTO> importListener = new ExcelImportRollbackListener<>(MeterExcelImportDTO.class,
                    //业务参数校验方法,并单行保存数据
                    t -> {
                        t.setDeviceType(deviceTye);
                        t.setParkOrgId(parkOrgId);
                        return validAndOperateExcelDTO(t);
                    },
                    null
            );
            if (deviceTye.equals(WATER_METER.type)) {
                importListener.setExcludeHeads(Lists.newArrayList("*表计类别"));
            }
            ExcelImportUtils.importExcel(file, MeterExcelImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }

    /**
     * v13：同一园区下，若设备名称相同、或者表计号码相同，则以系统已有数据为准，对应行将不做导入处理
     *
     * @param dto
     * @return
     */
    private String validAndOperateExcelDTO(MeterExcelImportDTO dto) {
        if (dto.getDeviceType() == ELECTRICITY_METER.type && Objects.isNull(dto.getEffectType())) {
            return "表计类别不能为空！";
        }
        if (dto.getMeterType() == GENERAL_METER.type) {
            String deviceId = generateDeviceId();
            dto.setDeviceId(deviceId);
        }
        if (dto.getMeterType() == INTELLIGENT_METER.type) {
            try {
                checkDeviceId(dto.getId(), dto.getParkOrgId(), dto.getDeviceType(), dto.getDeviceId());
            } catch (ResolinkRuntimeException e) {
                return e.getErrorMessage().getDisplayText();
            }
        }
        try {
            checkDeviceName(dto.getId(), dto.getParkOrgId(), dto.getDeviceType(), dto.getDeviceName());
        } catch (ResolinkRuntimeException e) {
            return e.getErrorMessage().getDisplayText();
        }
        //填充楼栋
        String buildingId = Optional.ofNullable(dto.getBuildingId())
                .map(t -> commonProvider.getIdsByNamesAndType(Sets.newHashSet(t), SimpleNode.TypeEnum.BUILDING.getCode(), dto.getParkOrgId()).get(t))
                .orElse(null);
        if (Objects.isNull(buildingId)) {
            return "该楼栋不存在";
        }
        dto.setBuildingId(buildingId);
        int totalFloor = buildingProvider.getBuildingTotalFloor(buildingId).getData().intValue();
        List<Integer> floors = dto.getFloors();
        //所属楼层不能超过所属楼栋的总楼层
        Set<Integer> errorFloors = floors.parallelStream().filter(t -> t > (totalFloor)).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(errorFloors)) {
            return "计量楼层错误,最高楼层为：" + totalFloor;
        }
        List<String> houseIds = dto.getHouseIds();
        if (Objects.nonNull(houseIds)) {
            Map<String, String> houseIdsByNames = hoseProvider.getHouseIdsByNames(Sets.newHashSet(houseIds), buildingId, dto.getParkOrgId());
            if (Objects.nonNull(houseIdsByNames) && houseIdsByNames.size() == houseIds.size()) {
                //输入的每个房屋都存在
                dto.setHouseIds(Lists.newArrayList(houseIdsByNames.values()));
            } else {
                return "房屋不存在";
            }
        }
        MeterReqDTO afterMeter = MyBeanUtils.copy(dto, MeterReqDTO::new);
        afterMeter.setInitialTime(LocalDateTime.of(dto.getInitialTime(), LocalTime.now()));
        return this.saveOrUpdateMeter(afterMeter) ? null : "插入数据失败！";
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */


    private LambdaQueryWrapper<DeviceInfo> getMeterConditionWrapper(MeterQuery query, SFunction<DeviceInfo, ?>... functions) {
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<DeviceInfo>();
        if (ArrayUtil.isNotEmpty(functions)) {
            wrapper.select(functions);
        }
        wrapper.eq(StringUtils.isNotBlank(query.getMeterId()), DeviceInfo::getId, query.getMeterId());
        wrapper.eq(StringUtils.isNotBlank(query.getDeviceId()), DeviceInfo::getDeviceId, query.getDeviceId());
        wrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), DeviceInfo::getParkOrgId, query.getParkOrgId());
        wrapper.eq(DeviceInfo::getDeviceType, query.getDeviceType());
        wrapper.in(DeviceInfo::getParkOrgId, query.getParkOrgIds());
        wrapper.eq(Objects.nonNull(query.getStatus()), DeviceInfo::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getUsingStatus()), DeviceInfo::getUsingStatus, query.getUsingStatus());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), DeviceInfo::getDeviceName, query.getDeviceName());
        if (StringUtils.isNotBlank(query.getBuildingId())) {
            String sql = new StringBuilder(" JSON_CONTAINS(").append(LambdaUtil.getFieldName(DeviceInfo::getExtension)).append(",")
                    .append("JSON_QUOTE('").append(query.getBuildingId()).append("')").append(",")
                    .append("'$.").append(LambdaUtil.getFieldName(MeterInfoDTO::getBuildingId)).append("'").append(")").toString();
            wrapper.apply(sql);
        }
        if (Objects.nonNull(query.getMeterType())) {
            String sql = new StringBuilder(" JSON_CONTAINS(").append(LambdaUtil.getFieldName(DeviceInfo::getExtension)).append(",")
                    .append("CAST(").append(query.getMeterType()).append(" AS JSON),")
                    .append("'$.").append(LambdaUtil.getFieldName(MeterInfoDTO::getMeterType)).append("'").append(")").toString();
            wrapper.apply(sql);
        }
        if (Objects.nonNull(query.getEffectProperty())) {
            String sql = new StringBuilder(" JSON_CONTAINS(").append(LambdaUtil.getFieldName(DeviceInfo::getExtension)).append(",")
                    .append("'" + query.getEffectProperty() + "'").append(",")
                    .append("'$.").append(LambdaUtil.getFieldName(MeterInfoDTO::getEffectProperty)).append("'").append(")").toString();
            wrapper.apply(sql);
        }
        if (Objects.nonNull(query.getFloor())) {
            //JSON_CONTAINS(JSON_EXTRACT(extension,'$.floor'),floor)
            String sql = new StringBuilder(" JSON_CONTAINS(").append("JSON_EXTRACT(").append(LambdaUtil.getFieldName(DeviceInfo::getExtension)).append(",")
                    .append("'$.").append(LambdaUtil.getFieldName(MeterInfoDTO::getFloors)).append("'").append(")")
                    .append(",").append(query.getFloor()).append(")").toString();
            wrapper.apply(sql);
        }
        if (StringUtils.isNotBlank(query.getHouseId())) {
            String sql = new StringBuilder(" JSON_CONTAINS(").append("JSON_EXTRACT(").append(LambdaUtil.getFieldName(DeviceInfo::getExtension)).append(",")
                    .append("'$.").append(LambdaUtil.getFieldName(MeterInfoDTO::getHouseIds)).append("'").append(")")
                    .append(",").append("JSON_QUOTE('").append(query.getHouseId()).append("')")
                    .append(")").toString();
            wrapper.apply(sql);
        }
        wrapper.orderByDesc(DeviceInfo::getCreateTime);
        return wrapper;
    }


    private String generateDeviceId() {
        log.info("获取设备ID!");
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(DEVICE_ID);
        String format = numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        String deviceId = DEVICE_ID + "_" + "A" + format + numberSeq.getValue();
        log.info("设备ID，deviceId:{}", deviceId);
        return deviceId;
    }

    public static void main(String[] args) {

    }

}
