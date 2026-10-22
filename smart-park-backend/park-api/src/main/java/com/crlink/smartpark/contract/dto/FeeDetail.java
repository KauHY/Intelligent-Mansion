package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 租赁合同账单减免记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FeeDetail响应对象", description = "FeeDetail响应对象")
public class FeeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

//    @ApiModelProperty(value = "收费项")
//    private Integer feeType;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "待收金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "已收金额")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "减免金额（元）")
    private BigDecimal deductionAmount;
}
