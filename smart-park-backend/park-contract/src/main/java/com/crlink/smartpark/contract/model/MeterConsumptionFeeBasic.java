package com.crlink.smartpark.contract.model;

import com.crlink.smartpark.contract.dto.ChargeDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 租赁合同费用
 * </p>
 * */
@Data
public class MeterConsumptionFeeBasic implements Serializable {


    @ApiModelProperty
    private Integer contractType;

    @ApiModelProperty(value = "费用方案id")
    private String contractBillingPlanId;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "收费项名称")
    private String chargeName;

    @ApiModelProperty(value = "入驻企业id")
    private String companyId;

    @ApiModelProperty(value = "入驻企业名称")
    private String companyName;

    @ApiModelProperty(value = "房源Ids")
    private List<String> houseIds;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;

    @ApiModelProperty(value = "数据来源 1合同生成")
    private Integer dataSource;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "待收金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "已收金额")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "减免金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty(value = "费用状态 0未收 1已收")
    private Integer feeStatus;

    @ApiModelProperty(value = "费用明细")
    private List<ChargeDetail> chargeDetail;


}
