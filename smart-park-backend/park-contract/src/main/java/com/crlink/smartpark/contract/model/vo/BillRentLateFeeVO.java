package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 租赁滞纳金费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BillRentLateFeeVO响应对象", description = "租赁滞纳金费用")
public class BillRentLateFeeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "租赁账单id")
    private String rentBillId;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "逾期金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "逾期天数")
    private Integer dueDays;

    @ApiModelProperty(value = "应收滞纳金")
    private BigDecimal receivableFine;

    @ApiModelProperty(value = "待收滞纳金")
    private BigDecimal receiveFine;

    @ApiModelProperty(value = "已收滞纳金")
    private BigDecimal overdueFine;

    @ApiModelProperty(value = "减免滞纳金")
    private BigDecimal reduceFine;

}
