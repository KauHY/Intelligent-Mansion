package com.crlink.smartpark.device.service.task;

import com.crlink.smartpark.device.service.DeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 更新水表、电表设备的状态
 * 每5分钟执行一次
 *
 */
@Slf4j
@Service
public class DeviceStatusTask {

    @Autowired
    private DeviceInfoService deviceInfoService;

    public void updateDeviceStatus() {
        log.info("更新水电表的状态Start");
        deviceInfoService.updateDeviceStatus();
        log.info("更新水电表的状态End");
    }
}
