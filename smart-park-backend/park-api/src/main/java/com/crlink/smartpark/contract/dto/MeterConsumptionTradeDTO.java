package com.crlink.smartpark.contract.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.BUILDING_HOUSE_NAME_CODE;

/**
 * <p>
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterConsumptionTradeDTO响应对象", description = "租赁合同交易明细")
public class MeterConsumptionTradeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "是否可退款，0不可退款，1可退款")
    private Integer canReturn;

    @ApiModelProperty(value = "订单编号")
    private String tradeNo;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "支付方式:1线下银行转账")
    private Integer payType;

    @ApiModelProperty(value = "类型：1付款订单，2退款订单")
    private Integer type;

    @ApiModelProperty(value = "收款日期")
    private LocalDate checkoutDate;

    @ApiModelProperty(value = "账单应收金额(元)")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "实付金额(元)")
    private BigDecimal receivedAmount;

    private String createBy;

    @ApiModelProperty(value = "操作人")
    @Id2objectField(idField = "createBy", code = Id2objCodeConstant.USER_NAME_CODE)
    private String createByName;

    @ApiModelProperty(value = "支付时间/操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "房源名称")
    @Id2objectField(code = BUILDING_HOUSE_NAME_CODE, targetFieldName = "houseName")
    private List<String> houseIds;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "入住人员id")
    private List<String> employeeIds;

    @ApiModelProperty(value = "入住人员")
    private List<String> employeeIdNames;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "id2")
    private String extraTradeId;

    @ApiModelProperty(value = "单号2")
    private String extraTradeNo;

    @ApiModelProperty(value = "申请人")
    @Id2objectField(idField = "applicantId", code = Id2objCodeConstant.USER_NAME_CODE)
    private String applicantName;

    private String applicantId;

    @ApiModelProperty(value = "退款时间")
    private String returnTime;

    @ApiModelProperty(value = "退款金额(元)")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "退款原因")
    private String returnReason;


}
