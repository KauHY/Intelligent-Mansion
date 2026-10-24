package com.crlink.smartpark.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomerDistributionReqDTO {

    @ApiModelProperty(value = "客户Id")
    private String customerId;

    @ApiModelProperty(value = "跟进人")
    private String employeeId;
}
