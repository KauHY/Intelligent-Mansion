package com.crlink.smartpark.customer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class WxInformationListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型:字典 INFOMATION_TYPE")
    private Integer type;

    @ApiModelProperty(value = "图片")
    private String picture;

    @ApiModelProperty(value = "内容类型: 0 内容, 1超链接")
    private Integer contentType;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "浏览量")
    private Long views;

    @ApiModelProperty(value = "时间")
    private String effectTimeStart;
}
