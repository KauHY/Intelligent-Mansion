package com.crlink.smartpark.contract.dto;

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
 * 租赁合同账单减免记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "LateFeeDetail响应对象", description = "LateFeeDetail")
public class LateFeeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "逾期金额/待收金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "逾期天数")
    private Integer dueDays;

    @ApiModelProperty(value = "应收滞纳金")
    private BigDecimal receivableFine;

    @ApiModelProperty(value = "待收滞纳金")
    private BigDecimal receiveFine;

    @ApiModelProperty(value = "减免滞纳金")
    private BigDecimal reduceFine;

    @ApiModelProperty(value = "已收滞纳金")
    private BigDecimal overdueFine;

    @ApiModelProperty(value = "总金额（元）")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "入住人员id")
    private List<String> employeeIds;

    @ApiModelProperty(value = "入住人员")
    private List<String> employeeIdNames;
}
