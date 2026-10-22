package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CancelOrder对象", description="报事投诉、报修服务撤单")
public class CancelOrderDTO {

    @ApiModelProperty(value = "报事报修工单Id")
    @NotBlank(message = "报事报修工单Id不能为空")
    private String orderId;

    @ApiModelProperty(value = "报事报修撤销理由:1 问题已自行解决，2 误会了，没有这个问题，3已有人现场处理，4 其他原因")
    @NotNull(message = "报事报修撤销理由不能为空")
    private Integer cancelReason;
}
