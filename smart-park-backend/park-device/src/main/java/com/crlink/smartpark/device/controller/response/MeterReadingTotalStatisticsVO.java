package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MeterReadingTotalStatisticsVO implements Serializable {
    @ApiModelProperty("年累计用电量")
    private BigDecimal yearElectricityCount;
    @ApiModelProperty("年累计用水量")
    private BigDecimal yearWaterCount;
    @ApiModelProperty("折合碳排放")
    private BigDecimal carbonEmission;

}
