package com.crlink.smartpark.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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

/**
 * <p>
 * 会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mee_meeting_reserve")
@ApiModel(value="MeetingReserve数据库对象", description="会议室预订")
public class MeetingReserve implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "申请人id")
    private String employeeId;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "会议类型 1：普通会议 2：视频会议")
    private Integer meetingType;

    @ApiModelProperty(value = "预订类型 1预订 2候补")
    private Integer type;

    @ApiModelProperty(value = "与会人数")
    private Integer meetingPeopleNum;

    @ApiModelProperty(value = "与会人list")
    private String attendees;

    @ApiModelProperty(value = "会议开始时间")
    private LocalDateTime meetingTimeStart;

    @ApiModelProperty(value = "会议结束时间")
    private LocalDateTime meetingTimeEnd;

    @ApiModelProperty(value = "考勤开始时间")
    private LocalDateTime attendanceStartTime;

    @ApiModelProperty(value = "考勤结束时间")
    private LocalDateTime attendanceEndTime;

    @ApiModelProperty(value = "会议实际开始时间")
    private LocalDateTime meetingRealStart;

    @ApiModelProperty(value = "会议实际结束时间")
    private LocalDateTime meetingRealEnd;

    @ApiModelProperty(value = "会议纪要")
    private String meetingSummary;

    @ApiModelProperty(value = "会议纪要附件")
    private String meetingSummaryFile;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;

    @ApiModelProperty(value = "会议室名称")
    @TableField(exist = false)
    private String meetingName;


}
