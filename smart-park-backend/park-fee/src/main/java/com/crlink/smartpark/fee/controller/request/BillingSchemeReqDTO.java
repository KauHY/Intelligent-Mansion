package com.crlink.smartpark.fee.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 计费方案
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BillingSchemeReq对象", description = "计费方案")
public class BillingSchemeReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;


    @ApiModelProperty(value = "园区")
    @NotBlank(message = "园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "方案名称")
    @NotBlank(message = "方案名称不能为空")
    private String schemeName;

    @ApiModelProperty(value = "收费项")
    @NotBlank(message = "收费项不能为空")
    private String chargeItemId;

    @ApiModelProperty(value = "计费周期,计费周期：1-一次性；2-月；3-季；4-年")
    @NotNull(message = "计费周期不能为空")
    private Integer billingCycle;

    @ApiModelProperty(value = "保留位数,保留位数：0-取整数；1-保留一位小数；2-保留两位小数")
    @NotNull(message = "保留位数不能为空")
    private Integer reservedDigits;

    @ApiModelProperty(value = "进位方式：4-四舍五入；0-向上取整；1-向下取整")
    @NotNull(message = "进位方式不能为空")
    private Integer carryMode;

    @ApiModelProperty(value = "公式类型")
    @NotNull(message = "公式类型不能为空")
    private Integer formulaType;

    @ApiModelProperty(value = "含税金额")
    private BigDecimal amountWithTax;

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal amountWithoutTax;

    @ApiModelProperty(value = "计算标准")
    private Integer calculationStandard;

    @ApiModelProperty(value = "单价元（不含税）")
    private BigDecimal unitPriceWithoutTax;

    @ApiModelProperty(value = "单价元（含税））")
    private BigDecimal unitPriceWithTax;

    @ApiModelProperty(value = "收费方式")
    @NotNull(message = "收费方式不能为空")
    private Integer chargeType;

    @ApiModelProperty(value = "收费周期：1-与计费周期一致；2-多计费周期")
    @NotNull(message = "收费周期不能为空")
    private Integer chargingCycle;

    @ApiModelProperty(value = "累计周期")
//    @NotNull(message = "累计周期不能为空")
    private Integer cumulativeCycle;

    @ApiModelProperty(value = "账单生成日期:每月第x天生成账单")
    private Integer dayOfMonth;

    @ApiModelProperty(value = "	最晚缴纳日期，账单后的第几天")
    private Integer daysForDelay;

    @ApiModelProperty(value = "是否收取滞纳金 1是 0否")
    private Integer lateCharge;

    @ApiModelProperty(value = "滞纳率")
    private BigDecimal lateRate;

    @ApiModelProperty(value = "账单生成日期:第几月")
    private Integer monthBy;

    @ApiModelProperty(value = "滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积")
    private Integer lateType;

    @ApiModelProperty(value = "每日滞纳金")
    private BigDecimal lateDayAmount;

    @ApiModelProperty(value = "豁免天数")
    //@NotNull
    private Integer exemptDays;

    //private LocalDateTime createTime;
    //
    //private String createBy;
    //
    //private LocalDateTime updateTime;
    //
    //private String updateBy;
    //
    //private Boolean deleted;
    //@ApiModelProperty(value = "ids")
    //private List<String> ids;


}
