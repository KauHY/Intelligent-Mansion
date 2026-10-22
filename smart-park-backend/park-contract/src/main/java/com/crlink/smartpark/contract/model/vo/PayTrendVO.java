package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 收费统计-缴费趋势
 * </p>
 * */
@Data
@NoArgsConstructor
public class PayTrendVO implements Serializable {

    @ApiModelProperty(value = "当前月份")
    private String month;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivableAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "已收金额")
    private BigDecimal receivedAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "待收金额")
    private BigDecimal dueAmount = BigDecimal.ZERO;

}
