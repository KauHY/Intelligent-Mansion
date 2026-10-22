package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Consumption implements Serializable {
    @ApiModelProperty("能耗类型")
    private Integer deviceType;

    @ApiModelProperty("总用量")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @ApiModelProperty("公摊用量")
    private BigDecimal shareAmount = BigDecimal.ZERO;

    @ApiModelProperty("室内用量")
    private BigDecimal roomAmount = BigDecimal.ZERO;

    @ApiModelProperty("公共量")
    private BigDecimal publicAmount = BigDecimal.ZERO;

    public Consumption(Integer deviceType) {
        this.deviceType = deviceType;
    }


}
