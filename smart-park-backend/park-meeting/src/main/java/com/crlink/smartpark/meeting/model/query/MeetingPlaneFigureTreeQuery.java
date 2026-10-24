package com.crlink.smartpark.meeting.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MeetingPlaneFigureTreeQuery
 * @Package com.crlink.smartpark.meeting.model.query
 * @Description: 会议室分布图组织树查询
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingPlaneFigureTree查询对象", description="会议室平面图")
public class MeetingPlaneFigureTreeQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}
