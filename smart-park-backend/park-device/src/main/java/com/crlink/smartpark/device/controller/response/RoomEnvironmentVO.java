package com.crlink.smartpark.device.controller.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomEnvironmentVO implements Serializable {
    private String temperature;
    private String humidity;
    private String PM;
    private String bright;
    //空气质量
    private String comfort;
}
