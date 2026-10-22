package com.crlink.smartpark.customer.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 活动
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Activity对象", description = "活动")
public class ActivityReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "活动名称")
    @Length(max = 30)
    private String name;

    @ApiModelProperty(value = "活动地点")
    @Length(max = 30)
    private String place;

    @ApiModelProperty(value = "推送范围")
    @NotEmpty
    private List<String> parkOrgIds;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityTimeStart;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime activityTimeEnd;

    @ApiModelProperty(value = "生效时间1")
    private LocalDateTime effectTime;


    @ApiModelProperty(value = "活动详情")
    @NotBlank(message = "活动详情不能为空！")
//    @Length(max = 3000, message = "活动详情不能超过3000字")
    private String content;

    @ApiModelProperty(value = "是否需要报名")
    private Boolean confRegister;

    @ApiModelProperty(value = "报名截止时间")
    private LocalDateTime confRegisterDeadline;

    @ApiModelProperty(value = "是否限制报名人数")
    private Boolean confRegisterLimit;

    @ApiModelProperty(value = "报名人数上限")
    private Long confRegisterLimitCount;

    @ApiModelProperty(value = "列表图片")
    private String listPicture;
    @ApiModelProperty(value = "详情图片")
    private String detailPicture;


}
