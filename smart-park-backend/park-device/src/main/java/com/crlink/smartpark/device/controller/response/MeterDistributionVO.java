package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MeterDistributionVO implements Serializable {
    //@Id2objectField(code = BUILDING_NAME_CODE, targetFieldName = "buildingName")
    //private String buildingId;
    @ApiModelProperty("楼栋")
    private String buildingName;
    @ApiModelProperty("电")
    private BigDecimal electricity;
    @ApiModelProperty("水")
    private BigDecimal water;

    public MeterDistributionVO(String buildingName, BigDecimal electricity, BigDecimal water) {
        this.buildingName = buildingName;
        this.electricity = electricity;
        this.water = water;
    }
}

