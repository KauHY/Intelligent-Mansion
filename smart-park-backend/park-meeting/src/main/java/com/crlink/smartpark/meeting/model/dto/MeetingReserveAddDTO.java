package com.crlink.smartpark.meeting.model.dto;

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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserve请求对象", description="会议室预订")
public class MeetingReserveAddDTO implements Serializable {

    @ApiModelProperty(value = "会议主题")
    @NotBlank(message = "会议主题不能为空")
    private String meetingSubject;

    @ApiModelProperty(value = "会议室id")
    @NotBlank(message = "会议室id不能为空")
    private String meetingId;

    @ApiModelProperty(value = "会议类型 1：普通会议 2：视频会议")
    @NotNull(message = "会议类型不能为空")
    private Integer meetingType;

    @ApiModelProperty(value = "会议开始时间")
    @NotNull(message = "会议开始时间不能为空")
    private LocalDateTime meetingTimeStart;

    @ApiModelProperty(value = "会议结束时间")
    @NotNull(message = "会议结束时间不能为空")
    private LocalDateTime meetingTimeEnd;

    @ApiModelProperty(value = "与会人数")
    private Integer meetingPeopleNum;

    @ApiModelProperty(value = "与会人list")
    private String attendees;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "预订类型 1预订 2候补")
    @NotNull(message = "预订类型不能为空")
    private Integer type;


}
