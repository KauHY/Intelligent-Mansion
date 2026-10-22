package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MeterReadingStatisticsVO implements Serializable {
    @ApiModelProperty("年累计用量")
    private BigDecimal yearCount;
    @ApiModelProperty("年同比或环比")
    private BigDecimal yearPercent;


    @ApiModelProperty("月累计用量")
    private BigDecimal monthCount;
    @ApiModelProperty("月环比")
    private BigDecimal lastMonthPercent;
    @ApiModelProperty("月同比")
    private BigDecimal lastYearMonthPercent;


    @ApiModelProperty("日累计用量")
    private BigDecimal dayCount;
    @ApiModelProperty("日环比")
    private BigDecimal lastDayPercent;
    @ApiModelProperty("日同比")
    private BigDecimal lastYearDayPercent;
}
