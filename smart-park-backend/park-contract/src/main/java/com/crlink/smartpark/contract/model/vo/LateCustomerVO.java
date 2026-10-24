package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 租赁滞纳金费用
 * </p>
 * */
@Data
public class LateCustomerVO implements Serializable {

    @ApiModelProperty(value = "客户名称")
    private String companyName;

    @ApiModelProperty(value = "逾期金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "逾期天数")
    private Integer dueDays;

}
