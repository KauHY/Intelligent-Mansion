package com.crlink.smartpark.system.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 入驻企业表
 * */
@Data
@ApiModel(value = "入驻企业绑定客户端请求模型")
public class CompanyBindUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @NotBlank(message = "不能为空")
    private String id;

    /**
     * 企业绑定访客客户端类型，0：无 1：小程序 2：超链接
     */
    @ApiModelProperty(value = "企业绑定访客客户端类型，0：无 1：小程序 2：超链接", required = true)
    @NotNull(message = "企业绑定访客客户端类型，0：无 1：小程序 2：超链接 不能为空")
    @Range(min = 0, max = 2, message = "企业绑定访客客户端类型参数异常")
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

    /**
     * 是否是新增操作
     */
    private boolean saveOperation = false;

}
