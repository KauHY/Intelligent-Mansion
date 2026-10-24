package com.crlink.smartpark.system.dto.platform;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TenantOrganizationVO implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;


    @ApiModelProperty(value = "租户名称")
    private String orgName;

    @ApiModelProperty(value = "公司地址")
    private String address;


    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "营业执照电子版")
    private String licenseFileKey;

    @ApiModelProperty(value = "用户名")
    private String tenantUserName;

    @ApiModelProperty(value = "联系人姓名")
    private String contactUserName;

    @ApiModelProperty(value = "手机号")
    private String contactUserMobile;

    @ApiModelProperty(value = "状态:0-已禁用,1-正常")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "租户对应的C端小程序appId")
    private String cAppId;
    @ApiModelProperty(value = "租户对应的C端小程序Secret")
    private String cAppSecret;
    @ApiModelProperty(value = "租户对应的运营端小程序appId")
    private String bAppId;
    @ApiModelProperty(value = "租户对应的运营端小程序Secret")
    private String bAppSecret;

}
