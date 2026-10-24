package com.crlink.smartpark.system.dto.platform;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class TenantReqDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "租户名称")
    @NotBlank(message = "租户名称不能为空")
    @Length(max = 30, message = "租户名称不能超过30字")
    private String orgName;

    @ApiModelProperty(value = "地址")
    @NotBlank(message = "地址不能为空")
    @Length(max = 30, message = "地址不能超过30字")
    private String address;

    @ApiModelProperty(value = "统一社会信用代码")
    @NotBlank(message = "统一社会信用代码不能为空")
    @Length(min = 18, max = 18, message = "统一社会信用代码为18位数字")
    private String creditCode;

    @ApiModelProperty(value = "营业执照电子版")
    @NotBlank(message = "营业执照电子版")
    private String licenseFileKey;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20字")
    private String tenantUserName;

    //@ApiModelProperty(value = "密码:自动生成")
    //@JsonIgnore
    //private String password;

    @ApiModelProperty(value = "联系人姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 20, message = "联系人姓名不能超过20字")
    private String contactUserName;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String contactUserMobile;

    @ApiModelProperty(value = "租户对应的C端小程序appId")
    //@NotEmpty
    private String cAppId;
    @ApiModelProperty(value = "租户对应的C端小程序Secret")
    //@NotEmpty
    private String cAppSecret;
    @ApiModelProperty(value = "租户对应的运营端小程序appId")
    //@NotEmpty
    private String bAppId;
    @ApiModelProperty(value = "租户对应的运营端小程序Secret")
    //@NotEmpty
    private String bAppSecret;


}
