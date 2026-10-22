package com.crlink.smartpark.contract.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.crlink.smartpark.contract.dto.ChargeItemMessage;
import com.crlink.smartpark.core.mphandler.FastjsonFilterNullTypeHandler;
import com.crlink.smartpark.fee.dto.ChargeItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 合同计费信息
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "con_contract_billing_plan", autoResultMap = true)
@ApiModel(value="ContractBillingPlan对象", description="合同计费信息")
public class ContractBillingPlan implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 合同表主键
     */
    private String contractId;

    /**
     * 收费方案Id
     */
    private String feeBillingSchemeId;

    @ApiModelProperty(value = "收费方案名称")
    private String feeBillingSchemeName;

    /**
     * 计费周期：1-一次性；2-月；3-季；4-年
     */
    private Integer billingCycle;

    /**
     * 保留位数：0-取整数；1-保留一位小数；2-保留两位小数
     */
    private Integer reservedDigits;

    /**
     * 进位方式：4-四舍五入；0-向上取整；1-向下取整
     */
    private Integer carryMode;

    /**
     * 公式类型：1-固定金额；2-计算公式
     */
    private Integer formulaType;

    /**
     * 固定金额元（不含税）
     */
    private BigDecimal fixedAmountWithoutTax;

    /**
     * 固定金额元（含税）
     */
    private BigDecimal fixedAmountWithTax;

    /**
     * 金额元（不含税）
     */
    private BigDecimal amountWithoutTax;

    /**
     * 金额元（含税）
     */
    private BigDecimal amountWithTax;

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
     * 收费类型：1-预付费；2-后付费
     */
    private Integer chargeType;

    /**
     * 收费周期：1-与计费周期一致；2-多计费周期
     */
    private Integer chargingCycle;

    /**
     * 累计x个计费周期（当收费周期为多计费周期）
     */
    private Integer cumulativeCycle;

    /**
     * 账单生成日期:每月第x天生成账单
     */
    private Integer dayOfMonth;

    /**
     * 最晚缴纳日期，生成日期后第x天
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
     * 计费开始日期
     */
    private LocalDate billingStartDate;

    /**
     * 计费结束日期
     */
    private LocalDate billingEndDate;

    /**
     * 免租期（天）
     */
    private Integer freeRentDay;

    @ApiModelProperty(value = "账单生成日期:第几月")
    private Integer monthBy;

    @ApiModelProperty(value = "滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积")
    private Integer lateType;

    @ApiModelProperty(value = "每日滞纳金")
    private BigDecimal lateDayAmount;

    @ApiModelProperty(value = "是否收取滞纳金 1是 0否")
    private Integer lateCharge;

    /**
     * 删除状态
     */
    private Boolean deleted;

    /**
     * 扩展字段json字符串(收费项详细属性)
     */
    @TableField(typeHandler = FastjsonFilterNullTypeHandler.class)
    private ChargeItemMessage chargeItemMessage;
}
