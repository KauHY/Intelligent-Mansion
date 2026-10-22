package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ReserveDetailVO implements Serializable {

    @ApiModelProperty(value = "预定id")
    private String meetingReserveId;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;

    @ApiModelProperty(value = "会议开始时间")
    private String startTime;

    @ApiModelProperty(value = "会议结束时间")
    private String endTime;

    @ApiModelProperty(value = "会议状态")
    private Integer meetingState;


}
