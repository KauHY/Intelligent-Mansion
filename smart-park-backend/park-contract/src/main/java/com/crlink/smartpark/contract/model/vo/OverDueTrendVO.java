package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 租赁滞纳金费用
 * </p>
 * */
@Data
@NoArgsConstructor
public class OverDueTrendVO implements Serializable {

    @ApiModelProperty(value = "当前月份")
    private String month;

    @ApiModelProperty(value = "逾期金额")
    private BigDecimal dueAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "逾期客户数量")
    private Integer customers = 0;

}
