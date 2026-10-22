package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CompanyClientInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业ID")
    private String id;

    @ApiModelProperty(value = "绑定园区ID")
    private String parkOrgId;

    /**
     * 企业绑定访客客户端类型，0：无 1：小程序 2：超链接
     */
    @ApiModelProperty(value = "企业绑定访客客户端类型，0：无 1：小程序 2：超链接")
    private Integer visitorClientType;

    /**
     * 访客客户端小程序appId/H5
     */
    @ApiModelProperty(value = "访客客户端小程序appId/H5")
    private String visitorClientValue;

    /**
     * 访客客户端图片地址
     */
    @ApiModelProperty(value = "访客客户端图片地址")
    private String visitorClientPicUrl;


}
