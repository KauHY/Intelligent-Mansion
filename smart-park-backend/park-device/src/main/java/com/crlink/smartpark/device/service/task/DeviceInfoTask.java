package com.crlink.smartpark.device.service.task;

import com.crlink.smartpark.device.service.DeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceInfoTask {

    @Autowired
    private DeviceInfoService deviceInfoService;

    public void updateScreenDeviceStatus() {
        log.info("更新信息屏状态开始！");
        deviceInfoService.updateScreenDeviceStatus();
        log.info("更新信息屏状态结束！");
    }
}
