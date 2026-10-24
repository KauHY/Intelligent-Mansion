package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 合同/计费房屋关联
 * </p>
 * */
@Data
@ApiModel(value="DeductionMessage", description="抵扣内容")
public class DeductionMessage implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private String id;

    @ApiModelProperty(value = "收费项名称")
    private String chargeName;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "待抵扣金额", required = true)
    private BigDecimal deAmount;

}
