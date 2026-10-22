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
public class ElectricityConsumptionVO implements Serializable {
    @ApiModelProperty("累计用量")
    private BigDecimal count;
    @ApiModelProperty("类别")
    //@JsonIgnore
    //private Integer effectType;
    private String effectTypeName;
}
