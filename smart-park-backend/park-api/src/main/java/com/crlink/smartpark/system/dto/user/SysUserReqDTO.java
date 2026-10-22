package com.crlink.smartpark.system.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class SysUserReqDTO implements Serializable {

    private String userId;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20字")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 20, message = "姓名不能超过20字")
    private String realName;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @ApiModelProperty(value = "角色列表")
    private List<String> roleIds;
}
