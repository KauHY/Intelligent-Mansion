package com.crlink.smartpark.system.dto.user;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
public class RsUserPwdResetDTO implements Serializable {

    @ApiModelProperty(value = "用户名id")
    @NotBlank(message = "用户名id不能为空")
    private String userId;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String newPasswordAgain;

}
