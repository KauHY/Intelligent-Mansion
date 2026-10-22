package com.crlink.smartpark.device.service;

import com.crlink.smartpark.device.controller.request.Device3dPageQuery;
import com.crlink.smartpark.device.controller.response.Device3dVO;
import com.crlink.smartpark.device.controller.response.DeviceStatusCountVO;
import com.crlink.smartpark.device.controller.response.DeviceStatusVO;
import com.crlink.smartpark.device.controller.response.LiquidVO;
import com.crlink.smartpark.device.controller.response.RoomEnvironmentVO;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface Device3dService {
    int METER_DEVICE = 1;
    int FIRE_DEVICE = 2;

    String getWeather(String location);

    /**
     * 获取空气质量
     *
     * @param location
     * @return
     */
    String getAirQuality(String location);

    Integer getDeviceTotal(String parkOrgId);

    DeviceStatusCountVO getDeviceTypeStatus(String parkOrgId, Integer deviceTypes);

    PageInfo<Device3dVO> list3dDevice(Device3dPageQuery reqDTO);

    List<DeviceStatusCountVO> getDeviceWarningTotal(String parkOrgId);

    DeviceInfoVO getDeviceByDeviceId(String parkOrgId, String deviceId);

    List<DeviceStatusVO> getRoomDeviceStatus();

    RoomEnvironmentVO getRoomEnvironment();

    List<LiquidVO> getLiquidList();

}
