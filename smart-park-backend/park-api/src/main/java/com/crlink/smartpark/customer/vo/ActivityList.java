package com.crlink.smartpark.customer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ActivityList implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动状态 1 待生效、2 预告中、3 进行中、4 已结束")
    private Integer status;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityTimeStart;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime activityTimeEnd;

}
