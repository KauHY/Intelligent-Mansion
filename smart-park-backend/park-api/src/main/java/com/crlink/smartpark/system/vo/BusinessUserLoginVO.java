package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
public class BusinessUserLoginVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "归属租户")
    private String headOrgId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "accessToken")
    private String accessToken;

    @ApiModelProperty(value = "accessId")
    private String accessId;

    @ApiModelProperty(value = "企业信用编码")
    private String code;


}
