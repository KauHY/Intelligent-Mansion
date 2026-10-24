package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "退单")
public class ReturnOrderDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "退单原因", required = true)
    @NotBlank(message = "退单原因不能为空")
    @Length(max = 200, message = "退单原因不能超过200字")
    private String returnOrderReason;
}
