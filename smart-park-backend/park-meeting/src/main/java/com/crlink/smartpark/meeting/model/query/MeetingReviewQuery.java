package com.crlink.smartpark.meeting.controller.request.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 会议室审核人关联
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReview查询对象", description="会议室审核人关联")
public class MeetingReviewQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "关联审批员工id")
    private String employeeId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


}
