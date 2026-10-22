package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class TransferOrderDTO {

    @ApiModelProperty(value = "工单Id", required = true)
    @NotBlank(message = "工单Id不能为空")
    private String id;

    @ApiModelProperty(value = "转办员Id", required = true)
    @NotBlank(message = "转办员Id不能为空")
    private String employeeId;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;

}
