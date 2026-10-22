package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO implements Serializable {
    @ApiModelProperty("用电量")
    private BigDecimal electricityCount = BigDecimal.ZERO;
    @ApiModelProperty("用水量")
    private BigDecimal waterCount = BigDecimal.ZERO;


}
