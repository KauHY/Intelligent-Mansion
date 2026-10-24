package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 设备能耗统计对象
 * @Created by jiangjiye
 */
@Data
public class DeviceSummaryVO {

    @ApiModelProperty("deviceId设备编码")
    private String deviceId;

    @ApiModelProperty("当日用量")
    private BigDecimal dailyConsumption;

    @ApiModelProperty("当月用量")
    private BigDecimal monthlyConsumption;

    @ApiModelProperty("年总用量")
    private BigDecimal yearConsumption;

}
