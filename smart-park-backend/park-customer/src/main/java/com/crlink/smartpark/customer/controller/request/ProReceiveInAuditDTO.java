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
public class ProReceiveInAuditDTO implements Serializable {

    @ApiModelProperty(value = "物品领用id")
    private String id;

    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(value = "审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消")
    private Integer auditState;

    @ApiModelProperty(value = "审核意见")
    @NotEmpty(message = "审核意见不能为空")
    private String auditMsg;

    @ApiModelProperty(value = "是否需归还 1是 0否")
    private Integer needReturn;


}
