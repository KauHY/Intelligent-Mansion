package com.crlink.smartpark.contract.model.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.crlink.smartpark.contract.dto.ChargeItemMessage;
import com.crlink.smartpark.core.mphandler.FastjsonFilterNullTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * <p>
 * 合同计费信息
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractBillingPlanReqDTO对象", description="合同计费信息")
public class ContractBillingPlanReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 收费方案Id
     */
    @ApiModelProperty(value = "收费方案Id", required = true)
    @NotBlank(message = "收费方案id不能为空")
    private String feeBillingSchemeId;


    @ApiModelProperty(value = "收费方案名称")
    @NotBlank(message = "收费方案名称不能为空")
    private String feeBillingSchemeName;

    /**
     * 计费周期：1-一次性；2-月；3-季；4-年
     */
    @ApiModelProperty(value = "计费周期：1-一次性；2-月；3-季；4-年", required = true)
    @NotNull(message = "计费周期不能为空")
    private Integer billingCycle;

    /**
     * 保留位数：0-取整数；1-保留一位小数；2-保留两位小数
     */
    @ApiModelProperty(value = "保留位数：0-取整数；1-保留一位小数；2-保留两位小数", required = true)
    @NotNull(message = "保留位数不能为空")
    private Integer reservedDigits;

    /**
     * 进位方式：4-四舍五入；0-向上取整；1-向下取整
     */
    @ApiModelProperty(value = "进位方式：4-四舍五入；0-向上取整；1-向下取整", required = true)
    @NotNull(message = "进位方式不能为空")
    private Integer carryMode;

    /**
     * 公式类型：1-固定金额；2-计算公式
     */
    @ApiModelProperty(value = "公式类型：1-固定金额；2-计算公式", required = true)
    @NotNull(message = "公式类型不能为空")
    private Integer formulaType;

    /**
     * 固定金额元（不含税）
     */
    @ApiModelProperty(value = "固定金额元（不含税）", required = true)
    private BigDecimal fixedAmountWithoutTax;

    /**
     * 固定金额元（含税）
     */
    @ApiModelProperty(value = "固定金额元（含税）", required = true)
    private BigDecimal fixedAmountWithTax;

    /**
     * 金额元（不含税）
     */
    @ApiModelProperty(value = "金额元（不含税）", required = true)
    private BigDecimal amountWithoutTax;

    /**
     * 金额元（含税）
     */
    @ApiModelProperty(value = "金额元（含税）", required = true)
    private BigDecimal amountWithTax;

    @ApiModelProperty(value = "计算标准")
    private Integer calculationStandard;

    @ApiModelProperty(value = "单价元（不含税）")
    private BigDecimal unitPriceWithoutTax;

    @ApiModelProperty(value = "单价元（含税））")
    private BigDecimal unitPriceWithTax;
    /**
     * 收费类型：1-预付费；2-后付费
     */
    @ApiModelProperty(value = "收费类型：1-预付费；2-后付费", required = true)
    @NotBlank(message = "收费类型")
    private Integer chargeType;

    /**
     * 收费周期：1-与计费周期一致；2-多计费周期
     */
    @ApiModelProperty(value = "收费周期：1-与计费周期一致；2-多计费周期", required = true)
    @NotBlank(message = "收费周期")
    private Integer chargingCycle;

    /**
     * 累计x个计费周期（当收费周期为多计费周期）
     */
    @ApiModelProperty(value = "累计x个计费周期（当收费周期为多计费周期）", required = true)
    @NotBlank(message = "累计x个计费周期")
    private Integer cumulativeCycle;

    /**
     * 账单生成日期:每月第x天生成账单
     */
    @ApiModelProperty(value = "账单生成日期:每月第x天生成账单", required = true)
    @NotBlank(message = "账单生成日期")
    private Integer dayOfMonth;

    /**
     * 最晚缴纳日期，生成日期后第x天
     */
    @ApiModelProperty(value = "最晚缴纳日期，生成日期后第x天")
    private Integer daysForDelay;

    @ApiModelProperty(value = "是否收取滞纳金 1是 0否")
    private Integer lateCharge;

    /**
     * 滞纳率
     */
    @ApiModelProperty(value = "滞纳率")
    private BigDecimal lateRate;

    /**
     * 豁免天数
     */
    @ApiModelProperty(value = "豁免天数")
    private Integer exemptDays;

    @ApiModelProperty(value = "账单生成日期:第几月")
    private Integer monthBy;

    @ApiModelProperty(value = "滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积")
    private Integer lateType;

    @ApiModelProperty(value = "每日滞纳金")
    private BigDecimal lateDayAmount;

    /**
     * 计费开始日期
     */
    @ApiModelProperty(value = "计费开始日期", required = true)
    private LocalDate billingStartDate;

    /**
     * 计费结束日期
     */
    @ApiModelProperty(value = "计费结束日期", required = true)
    private LocalDate billingEndDate;

    /**
     * 免租期（天）
     */
    @ApiModelProperty(value = "免租期（天）")
    private Integer freeRentDay;

    /**
     * 应用房源ids
     */
    @ApiModelProperty(value = "应用房源ids", required = true)
    @NotEmpty(message = "应用房源不能为空")
    private Set<String> houseIds;

    /**
     * 扩展字段json字符串(收费项详细属性)
     */
    @ApiModelProperty(value = "收费项详细属性", required = true)
    @NotEmpty(message = "收费项详细属性不能为空")
    private ChargeItemMessage chargeItemMessage;
}
