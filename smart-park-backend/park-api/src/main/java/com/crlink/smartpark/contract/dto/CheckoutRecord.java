package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 租赁合同账单减免记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CheckoutRecord响应对象", description = "CheckoutRecord")
public class CheckoutRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "订单编号")
    private String tradeNo;

    @ApiModelProperty(value = "类型：1付款订单，2退款订单")
    private Integer type;

    @ApiModelProperty(value = "实收金额（元）")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付方式:1线下银行转账")
    private Integer payType;

    @ApiModelProperty(value = "收款日期")
    private LocalDate checkoutDate;

    private String createBy;

    @ApiModelProperty(value = "操作人员")
    private String createByName;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
