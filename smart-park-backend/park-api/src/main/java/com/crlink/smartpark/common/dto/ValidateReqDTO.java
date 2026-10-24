package com.crlink.smartpark.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 公众号用户openId与手机号关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ValidateReqDTO请求对象", description="校验请求对象")
public class ValidateReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    private String validateCode;

    @ApiModelProperty(value = "验证码key")
    private String validateCodeKey;

    @ApiModelProperty(value = "短信验证码")
    private String telValidateCode;

}
