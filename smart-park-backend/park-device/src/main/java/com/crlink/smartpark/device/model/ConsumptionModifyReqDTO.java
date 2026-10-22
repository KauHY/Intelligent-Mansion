package com.crlink.smartpark.device.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ConsumptionModifyReqDTO implements Serializable {
    @NotEmpty
    private String id;

    @ApiModelProperty("用电或用水")
    @NotNull
    private Integer deviceType;

    @ApiModelProperty("应摊用量")
    private BigDecimal shareAmount;
}
