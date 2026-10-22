package com.crlink.smartpark.device.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 卡片管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CardBoundReqDTO请求对象", description = "卡片管理")
public class CardBoundReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空！")
    private String id;

//    @ApiModelProperty(value = "操作类型: 1取消挂失,2挂失,4解绑", required = true)
//    @NotNull(message = "操作类型不能为空！")
//    @CustomValid(value = {"1", "2", "4"}, message = "操作类型值错误")
//    private Integer type;

    @ApiModelProperty(value = "所属企业ID", required = true)
    @NotBlank(message = "所属企业不能为空")
    private String companyId;

    @ApiModelProperty(value = "关联员工ID", required = true)
    @NotBlank(message = "关联员工不能为空")
    private String employeeId;

}
