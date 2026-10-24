package com.crlink.smartpark.device.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusVO implements Serializable {
    private String deviceName;
    private Integer status;
}
