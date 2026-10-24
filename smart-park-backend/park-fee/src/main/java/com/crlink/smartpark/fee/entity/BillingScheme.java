package com.crlink.smartpark.fee.entity;

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
import java.time.LocalDateTime;

/**
 * <p>
 * 计费方案
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fee_billing_scheme")
@ApiModel(value = "BillingScheme对象", description = "计费方案")
public class BillingScheme implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String parkOrgId;

    /**
     * 方案名称
     */
    private String schemeName;

    /**
     * 收费项
     */
    private String chargeItemId;

    /**
     * 费用类型
     */
    private Integer feeType;

    /**
     * 计费周期,计费周期：1-一次性；2-月；3-季；4-年
     */
    private Integer billingCycle;

    /**
     * 保留位数,保留位数：0-取整数；1-保留一位小数；2-保留两位小数
     */
    private Integer reservedDigits;

    /**
     * 进位方式：4-四舍五入；0-向上取整；1-向下取整
     */
    private Integer carryMode;

    /**
     * 公式类型
     */
    private Integer formulaType;

    /**
     * 含税金额
     */
    private BigDecimal amountWithTax;

    /**
     * 不含税金额
     */
    private BigDecimal amountWithoutTax;

    /**
     * 计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价；5-房间数*单价
     */
    private Integer calculationStandard;

    /**
     * 单价元（不含税）
     */
    private BigDecimal unitPriceWithoutTax;

    /**
     * 单价元（含税）
     */
    private BigDecimal unitPriceWithTax;

    /**
     * 收费方式
     */
    private Integer chargeType;

    /**
     * 收费周期：1-与计费周期一致；2-多计费周期
     */
    private Integer chargingCycle;

    /**
     * 累计周期
     */
    private Integer cumulativeCycle;

    /**
     * 账单生成日期:每月第x天生成账单
     */
    private Integer dayOfMonth;

    /**
     * 最晚缴纳日期		最晚缴纳日期		最晚缴纳日期，账单后的第几天
     */
    private Integer daysForDelay;

    /**
     * 滞纳率
     */
    private BigDecimal lateRate;

    /**
     * 豁免天数
     */
    private Integer exemptDays;

    /**
     * 状态：0-关闭；1-开启
     */
    private Integer status;

    @ApiModelProperty(value = "账单生成日期:第几月")
    private Integer monthBy;

    @ApiModelProperty(value = "滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积")
    private Integer lateType;

    @ApiModelProperty(value = "每日滞纳金")
    private BigDecimal lateDayAmount;

    @ApiModelProperty(value = "是否收取滞纳金 1是 0否")
    private Integer lateCharge;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
