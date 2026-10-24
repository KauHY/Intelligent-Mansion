package com.crlink.smartpark.meeting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel(value="MeetingReserve响应对象", description="会议室预订")
public class MeetingReserveDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
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

    @ApiModelProperty(value = "会议室状态")
    private Boolean roomStatus;

    @ApiModelProperty(value = "会议开始时间")
    private LocalDateTime meetingTimeStart;

    @ApiModelProperty(value = "会议结束时间")
    private LocalDateTime meetingTimeEnd;

    @ApiModelProperty(value = "会议实际开始时间")
    private LocalDateTime meetingRealStart;

    @ApiModelProperty(value = "会议实际结束时间")
    private LocalDateTime meetingRealEnd;

    @ApiModelProperty(value = "会议预定状态：1已预定 2可预订 3可候补")
    private Integer meetingState;

    @ApiModelProperty(value = "会议状态：0候补中（不展示） 1未开始、2进行中、3已结束、4已取消")
    private Boolean meetingStatus;

    @ApiModelProperty(value = "会议取消时间")
    private LocalDateTime meetingCancelTime;

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


}
