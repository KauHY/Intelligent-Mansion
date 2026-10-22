package com.crlink.smartpark.customer.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 领用权限设置
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceivePermission请求对象", description="领用权限设置")
public class ProReceivePermissionAddDTO implements Serializable {


    @ApiModelProperty(value = "所属园区")
    @NotEmpty(message = "所属园区不能为空！")
    private String parkOrgId;

    @ApiModelProperty(value = "公司id,逗号分隔")
    private String companyId;

    @ApiModelProperty(value = "员工id,逗号分隔")
    private String employeeId;




}
