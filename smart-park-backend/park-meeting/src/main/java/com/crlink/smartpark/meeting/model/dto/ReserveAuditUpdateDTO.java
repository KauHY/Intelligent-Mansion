package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
public class ReserveAuditUpdateDTO implements Serializable {

    @ApiModelProperty(value = "会议室预定id")
    @NotNull(message = "会议室预定id不能为空")
    private String meetingReserveId;

    @ApiModelProperty(value = "会议开始时间")
    @NotNull(message = "会议开始时间不能为空")
    private LocalDateTime meetingTimeStart;

    @ApiModelProperty(value = "会议结束时间")
    @NotNull(message = "会议结束时间不能为空")
    private LocalDateTime meetingTimeEnd;

}
