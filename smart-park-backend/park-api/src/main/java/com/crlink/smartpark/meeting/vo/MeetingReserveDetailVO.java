package com.crlink.smartpark.meeting.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.*;

/**
 * <p>
 * 会议室预订表
 * </p>
 * */
@Data
public class MeetingReserveDetailVO implements Serializable {

    @ApiModelProperty(value = "申请人id")
    private String employeeId;

    @ApiModelProperty(value = "申请人")
    @Id2objectField(code = EMPLOYEE_NAME_CODE)
    private String employeeName;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "会议室名字")
    @Id2objectField(code = MEETING_NAME_CODE,idField = "meetingId")
    private String meetingName;

    @ApiModelProperty(value = "会议类型 1：普通会议 2：视频会议")
    private Integer meetingType;

    @ApiModelProperty(value = "与会人数")
    private Integer meetingPeopleNum;

    @ApiModelProperty(value = "与会人list")
    private String attendees;

    @ApiModelProperty(value = "与会人姓名list")
    private String attendeesName;

    @ApiModelProperty(value = "会议开始时间")
    private LocalDateTime meetingTimeStart;

    @ApiModelProperty(value = "会议结束时间")
    private LocalDateTime meetingTimeEnd;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "审核人姓名")
    private String reviewName;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime reviewTime;

    @ApiModelProperty(value = "会议审核状态 1预定待审核、2预定成功、3候补待审核、4候补中、5手动取消 6超时取消 7审核不通过")
    private Integer meetingStatus;

    @ApiModelProperty(value = "审核意见")
    private String reviewMsg;

    @ApiModelProperty(value = "会议取消时间")
    private LocalDateTime meetingCancelTime;

    @ApiModelProperty(value = "会议纪要")
    private String meetingSummary;

    @ApiModelProperty(value = "会议纪要附件")
    private String meetingSummaryFile;

    @ApiModelProperty(value = "预订是否需审核 1是 0否")
    private Integer needReview;

}
