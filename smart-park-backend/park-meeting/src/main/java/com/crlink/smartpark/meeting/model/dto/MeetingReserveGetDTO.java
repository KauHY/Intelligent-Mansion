package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserve请求对象", description="会议室预订")
public class MeetingReserveGetDTO implements Serializable {

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属楼栋单元")
    private String buildingId;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "会议开始时间")
    @NotNull(message = "会议开始时间不能为空")
    private LocalDateTime meetingTimeStart;

    @ApiModelProperty(value = "会议结束时间")
    @NotNull(message = "会议结束时间不能为空")
    private LocalDateTime meetingTimeEnd;


}
