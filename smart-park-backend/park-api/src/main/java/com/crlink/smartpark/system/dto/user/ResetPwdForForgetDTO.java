package com.crlink.smartpark.system.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPwdForForgetDTO {

    @ApiModelProperty(value = "身份验证token", required = true)
    @NotBlank(message = "身份验证token不能为空")
    private String validateToken;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String newPasswordAgain;
}
