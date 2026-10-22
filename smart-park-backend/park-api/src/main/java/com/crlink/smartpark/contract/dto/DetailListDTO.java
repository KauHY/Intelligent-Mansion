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
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DetailListDTO", description = "明细")
public class DetailListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

//    @ApiModelProperty(value = "所含收费项：1租金 2 履约保证金 3滞纳金")
//    private List<Integer> feeType;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "入住人员id")
    private List<String> employeeIds;

    @ApiModelProperty(value = "入住人员")
    private List<String> employeeIdNames;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "待收金额(元)")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "待收滞纳金(元)")
    private BigDecimal receiveFine;
}
