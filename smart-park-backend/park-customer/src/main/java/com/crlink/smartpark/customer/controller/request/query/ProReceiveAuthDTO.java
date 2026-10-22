package com.crlink.smartpark.customer.controller.request.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProReceiveManager请求对象", description = "物品领用管理")
public class ProReceiveAuthDTO implements Serializable {

    @NotEmpty(message = "所属园区不能为空")
    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

}
