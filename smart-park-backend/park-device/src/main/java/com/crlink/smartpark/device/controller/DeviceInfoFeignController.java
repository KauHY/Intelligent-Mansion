package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.service.DeviceInfoService;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * 园区运营平台-设备管理feign
 * */
@Slf4j
@RestController
@Api(tags = "设备管理")
@RequestMapping("/deviceInfo_feign")
public class DeviceInfoFeignController {

    /**
     * 设备信息表
     */
    @Autowired
    private DeviceInfoService deviceInfoService;

    /**
     * 通过ids获取名字
     *
     * @description
     */
    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.POST)
    public Map<String, String> getNamesByIds(@RequestBody Set<String> list) {
        return deviceInfoService.id2Value(list, DeviceInfo::getDeviceName);
    }

    @RequestMapping(value = "/{deviceId}/getByDeviceId", method = RequestMethod.GET)
    public DeviceInfoVO getByDeviceId(@PathVariable(value = "deviceId") String deviceId) {
        return deviceInfoService.getByDeviceId(deviceId);
    }

    @RequestMapping(value = "/getDeviceIdByIds", method = RequestMethod.POST)
    public Map<String, String> getDeviceIdByIds(@RequestBody Set<String> ids) {
        return deviceInfoService.id2Value(ids, DeviceInfo::getDeviceId);
    }

    @RequestMapping(value = "/getIdByDeviceIds", method = RequestMethod.GET)
    Map<String, String> getIdByDeviceIds(@RequestParam("deviceIds") Set<String> deviceIds,
                                         @RequestParam("deviceTye") Integer deviceType) {
        Map<String, String> result = deviceInfoService.getIdByDeviceIds(deviceIds, deviceType);
        return result;
    }

    @RequestMapping(value = "/getIdByDeviceNames", method = RequestMethod.GET)
    Map<String, String> getIdByDeviceNames(@RequestParam("deviceNames") Set<String> deviceNames,
                                           @RequestParam("deviceTye") Integer deviceType,
                                           @RequestParam("parkOrgId") String parkOrgId) {
        Map<String, String> result = deviceInfoService.getIdByDeviceNames(deviceNames, deviceType, parkOrgId);
        return result;
    }

    @RequestMapping(value = "/getDeviceStatusByIds", method = RequestMethod.POST)
    public Map<String, Integer> getDeviceStatusByIds(@RequestBody Set<String> ids) {
        Map<String, Integer> result = deviceInfoService.getDeviceStatusByIds(ids);
        return result;
    }

    @RequestMapping(value = "/findDeviceIdsByHouseIds", method = RequestMethod.GET)
    public Map<String, Set<String>> findDeviceIdsByHouseIds(@RequestParam(value = "houseIds") Set<String> houseIds) {
        return deviceInfoService.findChannelNosByHouseIds(houseIds);
    }

}

