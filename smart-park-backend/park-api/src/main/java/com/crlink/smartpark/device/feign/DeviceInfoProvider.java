package com.crlink.smartpark.device.feign;

import com.crlink.smartpark.device.vo.DeviceInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 设备信息表 服务类
 * </p>
 * */
@FeignClient(name = "park-device", path = "/smartPark/park-device/deviceInfo_feign", contextId = "DeviceInfoProvider")
public interface DeviceInfoProvider {

    /**
     * 通过ids获取名字
     *
     * @param list
     * @return
     */
    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.POST)
    Map<String, String> getNamesByIds(@RequestBody Set<String> list);

    /**
     * 通过deviceId获取
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/{deviceId}/getByDeviceId", method = RequestMethod.GET)
    DeviceInfoVO getByDeviceId(@PathVariable(value = "deviceId") String deviceId);

    @RequestMapping(value = "/getDeviceIdByIds", method = RequestMethod.POST)
    Map<String, String> getDeviceIdByIds(@RequestBody Set<String> ids);

    @RequestMapping(value = "/getIdByDeviceIds", method = RequestMethod.GET)
    Map<String, String> getIdByDeviceIds(@RequestParam("deviceIds") Set<String> deviceIds,
                                         @RequestParam("deviceTye") Integer deviceType);

    @RequestMapping(value = "/getIdByDeviceNames", method = RequestMethod.GET)
    Map<String, String> getIdByDeviceNames(@RequestParam("deviceNames") Set<String> deviceNames,
                                           @RequestParam("deviceTye") Integer deviceType,
                                           @RequestParam("parkOrgId") String parkOrgId);

    @RequestMapping(value = "/getDeviceStatusByIds", method = RequestMethod.POST)
    Map<String, Integer> getDeviceStatusByIds(@RequestBody Set<String> ids);

}
