package com.crlink.smartpark.system.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 入驻企业表
 * */
@Data
@ApiModel(value = "入驻企业客户端信息列表响应模型")
public class CompanyBindListVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String id;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 绑定园区名
     */
    @ApiModelProperty(value = "绑定园区名")
    private String parkOrgName;

    /**
     * 企业绑定访客客户端类型，0：无 1：小程序 2：超链接
     */
    @ApiModelProperty(value = "企业绑定访客客户端类型，0：无 1：小程序 2：超链接")
    private Integer visitorClientType;

    /**
     * 企业绑定访客客户端类型，0：无 1：小程序 2：超链接
     */
    @ApiModelProperty(value = "企业绑定访客客户端类型，0：无 1：小程序 2：超链接")
    private String visitorClientTypeLabel;

    /**
     * 访客客户端小程序appId/H5
     */
    @ApiModelProperty(value = "访客客户端小程序appId/H5")
    private String visitorClientValue;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime createTime;

    /**
     * 访客企业信息的配置时间
     */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime visitorClientTime;

}
