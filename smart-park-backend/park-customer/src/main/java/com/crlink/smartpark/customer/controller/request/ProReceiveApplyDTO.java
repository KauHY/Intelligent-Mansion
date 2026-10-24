package com.crlink.smartpark.customer.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProReceiveManager请求对象", description = "物品领用管理")
public class ProReceiveApplyDTO implements Serializable {

    @NotEmpty(message = "库存id不能为空")
    @ApiModelProperty(value = "库存id")
    private String stockId;

    @NotEmpty(message = "所属园区不能为空")
    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @NotEmpty(message = "所属公司不能为空")
    @ApiModelProperty(value = "所属公司")
    private String companyId;

    @NotEmpty(message = "领用人不能为空")
    @ApiModelProperty(value = "领用人")
    private String employeeId;

    @NotNull(message = "领用数量不能为空")
    @ApiModelProperty(value = "领用数量")
    private Integer receiveNum;

    @ApiModelProperty(value = "备注")
    private String remark;


}
