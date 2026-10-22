package com.crlink.smartpark.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("con_rent_contract_trade")
@ApiModel(value = "RentContractTrade数据库对象", description = "租赁合同交易明细")
public class RentContractTrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "订单编号")
    private String tradeNo;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "费用类型 1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer contractType;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "类型：1付款订单，2退款订单")
    private Integer type;

    @ApiModelProperty(value = "支付方式:1线下银行转账")
    private Integer payType;

    @ApiModelProperty(value = "应收金额(元)")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "实付金额（元）")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "收款日期 ")
    private LocalDate checkoutDate;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal returnAmount;

//    @ApiModelProperty(value = "支付时间")
//    private LocalDateTime payTime;

    @ApiModelProperty(value = "是否可退款，0不可退款，1可退款")
    private Integer canReturn;

    @ApiModelProperty(value = "退款原因")
    private String returnReason;

    @ApiModelProperty(value = "备注")
    private String remark;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
