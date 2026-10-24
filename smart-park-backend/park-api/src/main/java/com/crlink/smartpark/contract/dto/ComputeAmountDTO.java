package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ComputeAmountDTO {

    @ApiModelProperty(value = "类型 9电费/公摊电费、10水费/公摊水费,")
    @NotNull(message = "类型不能为空！")
    private Integer type;

    @ApiModelProperty(value = "上月底数")
    @NotNull(message = "上月底数不能为空！")
    private BigDecimal lastReading;

    @ApiModelProperty(value = "分摊房间数")
    @NotNull(message = "分摊房间数不能为空！")
    private int shareCount;

    @ApiModelProperty(value = "true需要分摊，false不需要分摊")
    @NotNull(message = "是否分摊不能为空！")
    private Boolean needShare;

    @ApiModelProperty(value = "当前读数")
    @NotNull(message = "当前读数不能为空！")
    @Min(0)
    private BigDecimal thisReading;

    @ApiModelProperty(value = "园区id")
    @NotBlank(message = "园区id不能为空！")
    private String parkOrgId;
}
