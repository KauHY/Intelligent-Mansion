package com.crlink.smartpark.fee.dto;

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
 * 计费方案
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BillingSchemeDTO对象", description = "计费方案")
public class BillingSchemeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "方案名称")
    private String schemeName;

    @ApiModelProperty(value = "收费项")
    private String chargeItemId;

    @ApiModelProperty(value = "收费类型")
    private Integer feeType;

    private ChargeItemDTO chargeItemDTO;

    @ApiModelProperty(value = "计费周期,计费周期：1-一次性；2-月；3-季；4-年")
    private Integer billingCycle;

    @ApiModelProperty(value = "保留位数,保留位数：0-取整数；1-保留一位小数；2-保留两位小数")
    private Integer reservedDigits;

    @ApiModelProperty(value = "进位方式：4-四舍五入；0-向上取整；1-向下取整")
    private Integer carryMode;

    @ApiModelProperty(value = "公式类型")
    private Integer formulaType;

    @ApiModelProperty(value = "含税金额")
    private BigDecimal amountWithTax;

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal amountWithoutTax;

    @ApiModelProperty(value = "计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价；5-房间数*单价")
    private Integer calculationStandard;

    @ApiModelProperty(value = "单价元（不含税）")
    private BigDecimal unitPriceWithoutTax;

    @ApiModelProperty(value = "单价元（含税）")
    private BigDecimal unitPriceWithTax;

    @ApiModelProperty(value = "收费方式")
    private Integer chargeType;

    @ApiModelProperty(value = "收费周期：1-与计费周期一致；2-多计费周期")
    private Integer chargingCycle;

    @ApiModelProperty(value = "累计周期")
    private Integer cumulativeCycle;

    @ApiModelProperty(value = "账单生成日期:每月第x天生成账单")
    private Integer dayOfMonth;

    @ApiModelProperty(value = "最晚缴纳日期		最晚缴纳日期		最晚缴纳日期，账单后的第几天")
    private Integer daysForDelay;

    @ApiModelProperty(value = "是否收取滞纳金 1是 0否")
    private Integer lateCharge;

    @ApiModelProperty(value = "滞纳率")
    private BigDecimal lateRate;

    @ApiModelProperty(value = "豁免天数")
    private Integer exemptDays;

    @ApiModelProperty(value = "状态：0-关闭；1-开启")
    private Integer status;

    @ApiModelProperty(value = "账单生成日期:第几月")
    private Integer monthBy;

    @ApiModelProperty(value = "滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积")
    private Integer lateType;

    @ApiModelProperty(value = "每日滞纳金")
    private BigDecimal lateDayAmount;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDate updateTime;

    private String updateBy;

    private Boolean deleted;


}
