package com.crlink.smartpark.customer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
@ApiModel(value = "WxAdvertisementVO", description = "首页广告")
public class WxAdvertisementVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = " 广告位置 ：1 首页轮播图")
    private Integer position;

    @ApiModelProperty(value = "图片")
    private String picture;

    @ApiModelProperty(value = "跳转类型：0 无，1 URL，2 小程序AppID")
    private Integer jumpType;

    @ApiModelProperty(value = "跳转地址/需跳转的小程序AppID")
    private String jumpContent;
}
