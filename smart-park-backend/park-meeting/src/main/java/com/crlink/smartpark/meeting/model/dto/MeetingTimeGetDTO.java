package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class MeetingTimeGetDTO implements Serializable {

    @ApiModelProperty(value = "会议室id")
    @NotEmpty(message = "会议室id不能为空")
    private String meetingId;

    @ApiModelProperty(value = "当前日期")
    @NotEmpty(message = "当前日期不能为空")
    private String nowDate;


}
