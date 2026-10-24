package com.crlink.smartpark.contract.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 费用统计
 * </p>
 * */
@Data
public class ChargeTopStatisticDTO implements Serializable {

    @ApiModelProperty(value = "园区id")
    @NotEmpty(message = "园区id不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "类型 1本月 2累计")
    @NotNull(message = "类型不能为空!")
    private Integer type;


}
