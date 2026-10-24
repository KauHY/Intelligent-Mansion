package com.crlink.smartpark.contract.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.core.mphandler.HouseMessageListTypeHandler;
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
public class RentContractFeeBasic implements Serializable {


    @ApiModelProperty
    private Integer contractType;

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "费用方案id")
    private String contractBillingPlanId;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "收费项费用类型")
    private Integer feeType;

    @ApiModelProperty(value = "收费项名称")
    private String chargeName;

    @ApiModelProperty(value = "建筑面积（㎡）")
    private Double totalArea;

    @ApiModelProperty(value = "入驻企业id")
    private String companyId;

    @ApiModelProperty(value = "入驻企业名称")
    private String companyName;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "房源对象")
    @TableField(typeHandler = HouseMessageListTypeHandler.class)
    private List<HouseMessage> houseMessages;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

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

    @ApiModelProperty(value = "押金状态 1未缴清 2已缴清 3已结清")
    private Integer cashStatus;


}
