package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 收费统计-收入分布
 * </p>
 * */
@Data
@NoArgsConstructor
public class FeeTopStatisticVO implements Serializable {

    @ApiModelProperty(value = "账单收入（元）")
    private BigDecimal billReceived = BigDecimal.ZERO;

    @ApiModelProperty(value = "收缴率")
    private BigDecimal payRate = BigDecimal.ZERO;

    @ApiModelProperty(value = "已收金额（元）")
    private BigDecimal receivedAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "待收金额（元）")
    private BigDecimal dueAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "逾期金额（元）")
    private BigDecimal overDateAmount = BigDecimal.ZERO;


}
