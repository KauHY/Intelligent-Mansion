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
public class NoticeWxListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "时间")
    private String effectTimeStart;

    @ApiModelProperty(value = "是否弹窗提醒")
    private Boolean popuped;
}
