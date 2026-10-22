package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 收费统计-收入分布
 * </p>
 * */
@Data
@NoArgsConstructor
public class IncomeDistributionVO implements Serializable {

    @ApiModelProperty(value = "账单数据")
    private Map<Integer, BigDecimal> billData;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount = BigDecimal.ZERO;

}
