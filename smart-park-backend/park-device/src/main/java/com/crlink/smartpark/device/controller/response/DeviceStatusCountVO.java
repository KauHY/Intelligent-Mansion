package com.crlink.smartpark.device.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class DeviceStatusCountVO implements Serializable {
    private Object deviceType;
    private int onlineCount;
    private int offlineCount;
    private int totalCount;
    private int warningCount;

    public DeviceStatusCountVO(Object deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceStatusCountVO(Integer onlineCount, Integer offlineCount, Integer totalCount) {
        this.onlineCount = onlineCount;
        this.offlineCount = offlineCount;
        this.totalCount = totalCount;
    }
}
