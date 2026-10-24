package com.crlink.smartpark.device.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 卡片设备DTO
 *
 */
@Data
public class DeviceCardDTO {

    private Long id;
    private String cardNo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String personId;

}
