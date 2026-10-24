package com.crlink.smartpark.customer.controller.request.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 活动
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Activity对象", description = "活动")
public class ActivityQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityTimeStart;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime activityTimeEnd;

    @ApiModelProperty(value = "生效时间1")
    private LocalDateTime effectTimeStart;
    @ApiModelProperty(value = "生效时间1")
    private LocalDateTime effectTimeEnd;


    @ApiModelProperty(value = "是否需要报名")
    private Boolean confRegister;

    private Integer status;

    @JsonIgnore
    private Set<String> currentUserParkOrgIds;


}
