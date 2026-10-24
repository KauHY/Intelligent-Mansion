package com.crlink.smartpark.system.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
public class RsAuthLoginDTO implements Serializable {

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String validateCode;

    @ApiModelProperty(value = "验证码key")
    @NotBlank(message = "验证码key不能为空")
    private String validateCodeKey;


}
