package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.device.common.LiquidConfig;
import com.crlink.smartpark.device.controller.request.Device3dPageQuery;
import com.crlink.smartpark.device.controller.response.*;
import com.crlink.smartpark.device.entity.ClientDevice;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.entity.FireWarningRecord;
import com.crlink.smartpark.device.model.LiquidPosition;
import com.crlink.smartpark.device.service.ClientDeviceService;
import com.crlink.smartpark.device.service.Device3dService;
import com.crlink.smartpark.device.service.DeviceInfoService;
import com.crlink.smartpark.device.service.FireWarningRecordService;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.LIQUID_WATER_LEVEL;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.values;

@Slf4j
@Service
public class Device3dServiceImpl implements Device3dService {
    @Value("${3d.weather.api.key:9b75971656e84bcc98f00f89853e0f3b}")
    private String weatherApiKey;
    @Value("${3d.weather.api.url:https://devapi.qweather.com}")
    private String weatherUrl;
    @Value("${device.common.list.url:}")
    private String deviceListUrl;
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private ClientDeviceService clientDeviceService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private LiquidConfig liquidConfig;

    @Autowired
    private FireWarningRecordService fireWarningRecordService;

    @Override
    public PageInfo<Device3dVO> list3dDevice(Device3dPageQuery query) {
        query.startPage();
        List<DeviceInfo> list = deviceInfoService.lambdaQuery()
                .select(DeviceInfo::getId, DeviceInfo::getDeviceName, DeviceInfo::getDeviceId, DeviceInfo::getLocation, DeviceInfo::getStatus)
                .eq(DeviceInfo::getParkOrgId, query.getParkOrgId())
                .eq(DeviceInfo::getDeviceType, query.getDeviceType())
                // 按设备名倒序
                .orderByDesc(DeviceInfo::getDeviceName)
                .list();
        List<Device3dVO> result = list.parallelStream().map(t -> {
            Device3dVO device3dVO = MyBeanUtils.copy(t, Device3dVO::new);
            device3dVO.setDeviceType(query.getDeviceType());
            device3dVO.setCode(t.getDeviceId());
            return device3dVO;
        }).collect(Collectors.toList());
        return query.createPageInfo(result);
    }

    @Override
    public List<DeviceStatusCountVO> getDeviceWarningTotal(String parkOrgId) {
        //todo 报警
        DictConstant.DeviceType[] deviceTypeEnums = values();
        Set<Integer> deviceTypes = Arrays.stream(deviceTypeEnums).parallel().map(t -> t.type).collect(Collectors.toSet());
        List<DeviceInfo> list = deviceInfoService.lambdaQuery().select(DeviceInfo::getId, DeviceInfo::getStatus, DeviceInfo::getDeviceType)
                .eq(DeviceInfo::getParkOrgId, parkOrgId)
                .in(DeviceInfo::getDeviceType, deviceTypes).list();
        Map<Integer, List<DeviceInfo>> listMap = list.parallelStream().collect(Collectors.groupingBy(DeviceInfo::getDeviceType));
        List<DeviceStatusCountVO> result = Lists.newArrayList();
        listMap.forEach((k, v) -> {
            DeviceStatusCountVO countStatus = countStatus(v);
            countStatus.setDeviceType(k);
            result.add(countStatus);
        });
        if (deviceTypes.size() != listMap.size()) {
            CollUtil.subtract(deviceTypes, listMap.keySet()).forEach(deviceType -> {
                result.add(new DeviceStatusCountVO(deviceType));
            });
        }
        return result;
    }

    @Override
    public DeviceInfoVO getDeviceByDeviceId(String parkOrgId, String deviceId) {
        DeviceInfo deviceInfo = deviceInfoService.lambdaQuery().eq(DeviceInfo::getParkOrgId, parkOrgId).eq(DeviceInfo::getDeviceId, deviceId).one();
        DeviceInfoVO deviceInfoVO = MyBeanUtils.copy(deviceInfo, DeviceInfoVO::new);
        return deviceInfoVO;
    }

    @Override
    public List<DeviceStatusVO> getRoomDeviceStatus() {
        List<DeviceStatusVO> result = new ArrayList<>();
        ClientDevice lightDevice = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "0D53590107DB89").one();
        if (lightDevice != null) {
            result.add(new DeviceStatusVO("室内照明", lightDevice.getStatus()));
        }
        ClientDevice modeDevice = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "2653590201091F").one();
        if (modeDevice != null) {
            result.add(new DeviceStatusVO("电动窗帘", modeDevice.getStatus()));
        }

        ClientDevice airDevice = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "29535902025379").one();
        if (airDevice != null) {
            result.add(new DeviceStatusVO("室内空调", airDevice.getStatus()));
        }

        ClientDevice deviceDoor1 = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "1A53590101017E").one();
        if (deviceDoor1 != null) {
            result.add(new DeviceStatusVO("左侧门", deviceDoor1.getStatus()));
        }

        ClientDevice deviceDoor2 = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "1A535901010116").one();
        if (deviceDoor2 != null) {
            result.add(new DeviceStatusVO("右侧门", deviceDoor2.getStatus()));
        }
        ClientDevice deviceTV = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "2953590202538C").one();
        if (deviceTV != null) {
            result.add(new DeviceStatusVO("监控大屏", deviceTV.getStatus()));
        }
        return result;
    }

    @Override
    public RoomEnvironmentVO getRoomEnvironment() {
        ClientDevice temDevice = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "12535901010088").one();
        ClientDevice powerDevice = clientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, "3353590101404D").one();
        RoomEnvironmentVO environmentVO = new RoomEnvironmentVO();
        environmentVO.setTemperature("27.1");
        environmentVO.setHumidity("60");
        environmentVO.setBright("22");
        if (temDevice != null) {
            if (StringUtils.isNotBlank(temDevice.getRemark()) && temDevice.getRemark().contains("-")) {
                environmentVO.setTemperature(temDevice.getRemark().split("-")[0]);
                environmentVO.setHumidity(temDevice.getRemark().split("-")[1]);
            }
        }
        if (powerDevice != null) {
            if (StringUtils.isNotBlank(powerDevice.getRemark())) {
                environmentVO.setBright(powerDevice.getRemark());
            }
        }
        completeAirPm25(environmentVO);
        return environmentVO;
    }

    private void completeAirPm25(RoomEnvironmentVO environmentVO) {
        try {
            String airQuality = this.getAirQuality(null);
            com.alibaba.fastjson.JSONObject body = com.alibaba.fastjson.JSONObject.parseObject(airQuality);
            if (Objects.nonNull(body) && Objects.equals(HttpStatus.OK.value(), body.getInteger("code"))) {
                com.alibaba.fastjson.JSONObject object = body.getJSONObject("now");
                environmentVO.setComfort(object.getString("category"));
                environmentVO.setPM(object.getString("pm2p5"));
            }
        } catch (Exception e) {
            log.error("查询空气质量接口出错！", e);
        }
    }


    @Override
    public String getWeather(String location) {
        String url = weatherUrl + "/v7/weather/now";
        if (StringUtils.isBlank(location)) {
            //淮安市清江浦区
            location = "101190907";
        }
        String weatherRedisKey = "weather:url:" + url + "?" + location;
        String result = (String) redisUtil.getObject(weatherRedisKey);
        if (StringUtils.isBlank(result)) {
            result = HttpUtil.get(url, ImmutableMap.of("key", weatherApiKey, "location", location));
            redisUtil.setObject(weatherRedisKey, result, 1L, TimeUnit.HOURS);
        }
        return result;
    }

    @Override
    public String getAirQuality(String location) {
        String url = weatherUrl + "/v7/air/now";
        if (StringUtils.isBlank(location)) {
            //淮安市清江浦区
            location = "101190907";
        }
        String weatherQualityRedisKey = "weather:url:" + url + "?" + location;
        String result = (String) redisUtil.getObject(weatherQualityRedisKey);
        if (StringUtils.isBlank(result)) {
            result = HttpUtil.get(url, ImmutableMap.of("key", weatherApiKey, "location", location));
            redisUtil.setObject(weatherQualityRedisKey, result, 1L, TimeUnit.HOURS);
        }
        return result;
    }

    @Override
    public Integer getDeviceTotal(String parkOrgId) {
        Integer total = deviceInfoService.lambdaQuery().eq(DeviceInfo::getParkOrgId, parkOrgId).count();
        return total;
    }

    @Override
    public DeviceStatusCountVO getDeviceTypeStatus(String parkOrgId, Integer deviceType) {
        List<DeviceInfo> list = deviceInfoService.lambdaQuery().select(DeviceInfo::getId, DeviceInfo::getStatus)
                .eq(DeviceInfo::getParkOrgId, parkOrgId)
                .eq(Objects.nonNull(deviceType), DeviceInfo::getDeviceType, deviceType).list();
        return countStatus(list);
    }

    @Override
    public List<LiquidVO> getLiquidList() {
        List<LiquidVO> liquidList = Lists.newArrayList();
        if (StringUtils.isBlank(deviceListUrl)) {
            return liquidList;
        }
        String url = StrUtil.format(deviceListUrl, LIQUID_WATER_LEVEL.type);
        log.info("请求液位url: {}", url);
        try {
            String respStr = HttpUtil.get(url);
            log.info("请求液位的响应: {}", (StrUtil.isNotBlank(respStr) && respStr.length() > 100) ? respStr.substring(0, 100) : respStr);
            JSONObject respJson = JSONUtil.parseObj(respStr);
            if (Objects.equals(respJson.getInt("code"), HttpStatus.OK.value())) {
                JSONArray dataArr = respJson.getJSONArray("data");
                if (CollUtil.isNotEmpty(dataArr)) {
                    Map<String, LiquidPosition> liquidMap = liquidConfig.getDevices().stream().collect(Collectors.toMap(LiquidPosition::getOriginId, Function.identity()));
                    for (int i = 0; i < dataArr.size(); i++) {
                        JSONObject jsonObj = dataArr.getJSONObject(i);
                        String deviceId = jsonObj.getStr("DeviceID");
                        LiquidPosition lp = liquidMap.get(deviceId);
                        if (lp == null) {
                            continue;
                        }
                        LiquidVO liquidVO = new LiquidVO();
                        liquidVO.setDeviceId(lp.getNewId());
                        liquidVO.setPosition(lp.getPosition());
                        liquidVO.setValue(jsonObj.getStr("Value"));
                        liquidVO.setValueTime(jsonObj.getDate("ValueTime"));
                        liquidList.add(liquidVO);
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return liquidList;
    }

    private DeviceStatusCountVO countStatus(List<DeviceInfo> list) {
        int onlineCount = 0;
        int offlineCount = 0;
        for (DeviceInfo deviceInfo : list) {
            if (Objects.isNull(deviceInfo.getStatus()) || deviceInfo.getStatus() == DictConstant.DeviceStatus.OFFLINE.status) {
                offlineCount++;
            } else {
                onlineCount++;
            }
        }
        DeviceStatusCountVO result = new DeviceStatusCountVO(onlineCount, offlineCount, onlineCount + offlineCount);
        Set<Integer> deviceTypes = new HashSet<Integer>(){{
            add(DictConstant.DeviceType.SMOKE_SENSING.type);
            add(DictConstant.DeviceType.LIQUID_WATER_LEVEL.type);
        }};
        int warningCount = fireWarningRecordService.lambdaQuery().select(FireWarningRecord::getId).in(FireWarningRecord::getDeviceType, deviceTypes).count();
        result.setWarningCount(warningCount);
        return result;
    }


}
