package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 活动
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Activity对象", description = "活动")
public class ActivityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @Id2objectField(code = PARK_ORG_NAME_CODE, targetFieldName = "parkOrgNames")
    @ApiModelProperty(value = "推送范围")
    private List<String> parkOrgIds;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动地点")
    private String place;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityTimeStart;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime activityTimeEnd;

    @ApiModelProperty(value = "生效时间1")
    private LocalDateTime effectTime;


    @ApiModelProperty(value = "活动详情")
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
    @ApiModelProperty(value = "置顶")
    private Boolean topping;

    private Integer status;

    @ApiModelProperty(value = "自己是否报名")
    private Boolean selfRegistered;

    @ApiModelProperty(value = "报名人数")
    private Integer registeredCount;

    //private LocalDateTime createTime;

    //@JsonIgnore
    //private String createBy;
    //
    //@ApiModelProperty(value = "推送范围")
    //@Id2objectField(code = PARK_ORG_NAME_CODE)
    //private List<DictVO> parkOrgNames;
    //
    //
    //@Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    //@ApiModelProperty(value = "发布人")
    //private String createByName;


}
