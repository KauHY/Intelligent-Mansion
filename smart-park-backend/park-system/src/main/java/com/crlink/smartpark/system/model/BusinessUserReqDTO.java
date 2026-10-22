package com.crlink.smartpark.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 企业端用户表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BusinessUser请求对象", description="企业端用户表")
public class BusinessUserReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "所属租户Id")
    @NotEmpty(message = "所属租户Id不能为空")
    private String headOrgId;

    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "手机号", required = true)
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String validateCode;

    @ApiModelProperty(value = "验证码key")
    @NotBlank(message = "验证码key不能为空")
    private String validateCodeKey;

    @ApiModelProperty(value = "短信验证码")
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;


}
