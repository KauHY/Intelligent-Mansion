package com.crlink.smartpark.system.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 忘记密码验证身份DTO
 * </p>
 * */
@Data
public class RsUserValidateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    //@Pattern(regexp = Constants.REGEX_MOBILE_EXACT,message = "请输入正确的手机号")
    private String mobilePhone;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String validateCode;

    @ApiModelProperty(value = "验证码key")
    @NotBlank(message = "验证码key不能为空")
    private String validateCodeKey;

    @ApiModelProperty(value = "短信验证码")
    @NotBlank(message = "短信验证码不能为空")
    private String telValidateCode;

}
