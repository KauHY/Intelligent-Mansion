package com.crlink.smartpark.visitor.service.impl;

import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *
 * </p>
 * */
@Service
public class ApartmentAsyncAuthImpl {

    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    /**
     * 公寓对应设备授权
     *
     * @param employeeId
     */
    @Async
    public void apartmentDevicesAuth(String employeeId) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        deviceOperateProvider.refreshEmployeeDoorAccessAuthorities(employeeId);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        deviceOperateProvider.refreshSmartLockAuth(employeeId, false);
    }
}
