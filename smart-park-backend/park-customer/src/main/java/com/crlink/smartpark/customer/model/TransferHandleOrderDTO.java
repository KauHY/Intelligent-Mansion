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
public class TransferHandleOrderDTO {

    @ApiModelProperty(value = "工单Id", required = true)
    @NotBlank(message = "工单Id不能为空")
    private String id;

    @ApiModelProperty(value = "处理结果", required = true)
    @NotBlank(message = "处理结果不能为空")
    @Length(max = 200, message = "处理结果不能超过200字")
    private String result;
}
