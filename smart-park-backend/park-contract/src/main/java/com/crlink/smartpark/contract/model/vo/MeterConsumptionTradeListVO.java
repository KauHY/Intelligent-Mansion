package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterConsumptionTradeListVO响应对象", description = "租赁合同交易明细")
public class MeterConsumptionTradeListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "是否可退款，0不可退款，1可退款")
    private Integer canReturn;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "订单编号")
    private String tradeNo;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "支付方式:1线下银行转账")
    private Integer payType;

    @ApiModelProperty(value = "类型：1付款订单，2退款订单")
    private Integer type;

    @ApiModelProperty(value = "账单应收金额(元)")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "实付金额(元)")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "支付时间/操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
