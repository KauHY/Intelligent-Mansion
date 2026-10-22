package com.crlink.smartpark.system.dto.user;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
public class UserReqDTO implements Serializable {


    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20个字")
    private String username;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 20, message = "姓名不能超过20个字")
    private String realName;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    private String mobile;

    @ApiModelProperty(value = "所属组织")
    private String orgId;

    @ApiModelProperty(value = "角色列表")
    private List<String> roleIds;

}
