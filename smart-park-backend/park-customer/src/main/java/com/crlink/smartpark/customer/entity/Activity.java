package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 活动
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_activity")
@ApiModel(value = "Activity对象", description = "活动")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动地点")
    private String place;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityTimeStart;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime activityTimeEnd;


    @ApiModelProperty(value = "生效时间2")
    private LocalDateTime effectTime;

    @ApiModelProperty(value = "活动详情")
    private String content;

    @ApiModelProperty(value = "是否需要报名")
    private Boolean confRegister;

    @ApiModelProperty(value = "报名截止时间")
    private LocalDateTime confRegisterDeadline;

    @ApiModelProperty(value = "是否限制报名人数")
    private Boolean confRegisterLimit;

    @ApiModelProperty(value = "置顶")
    private Boolean topping;

    @ApiModelProperty(value = "置顶时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime toppingTime;

    @ApiModelProperty(value = "报名人数上限")
    private Long confRegisterLimitCount;

    @ApiModelProperty(value = "列表图片")
    private String listPicture;
    @ApiModelProperty(value = "详情图片")
    private String detailPicture;

    private Integer status;

    @ApiModelProperty(value = "0,未删除，1删除")
    private Boolean deleted;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;


}
