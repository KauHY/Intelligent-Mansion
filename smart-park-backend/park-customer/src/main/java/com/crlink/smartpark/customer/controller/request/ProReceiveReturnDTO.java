package com.crlink.smartpark.customer.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceiveManager请求对象", description="物品领用管理")
public class ProReceiveReturnDTO implements Serializable {

    @ApiModelProperty(value = "物品领用id")
    private String id;

    @ApiModelProperty(value = "本次归还数量")
    @NotNull(message = "本次归还数量不能为空")
    private Integer returnNum;

    @ApiModelProperty(value = "备注")
    private String remark;


}
