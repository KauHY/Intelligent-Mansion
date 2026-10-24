package com.crlink.smartpark.system.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePwdDTO {

    @ApiModelProperty(value = "id", required = true)
//    @NotBlank(message = "id")
    private String id;

    @ApiModelProperty(value = "原密码", required = true)
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String newPasswordAgain;
}
