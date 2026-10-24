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
public class WxNoticeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "公告类型")
    private Integer type;

    @ApiModelProperty(value = "浏览量")
    private Long views;

    @ApiModelProperty(value = "时间")
    private String effectTimeStart;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;

    @ApiModelProperty(value = "是否弹窗提醒")
    private Boolean popuped;

    @ApiModelProperty(value = "附件")
    private String files;

}
