package com.crlink.smartpark.customer.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ActivityToppingReqDTO implements Serializable {
    @NotEmpty
    private String activityId;
    @ApiModelProperty("true,置顶，false，取消置顶")
    @NotNull
    private Boolean topping;
}
