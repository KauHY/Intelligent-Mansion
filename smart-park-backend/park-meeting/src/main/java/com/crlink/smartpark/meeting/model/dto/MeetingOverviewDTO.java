package com.crlink.smartpark.meeting.model.dto;

import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.meeting.model.vo.ReserveTimeVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 会议室使用时间段概览
 * @Created by ZHOUXINJIAN3
 */
@Data
public class MeetingOverviewDTO implements Serializable {
    @ApiModelProperty("当前时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS, timezone = Constants.TIMEZONE)
    private LocalDateTime now;

    @ApiModelProperty("会议室名称")
    private String meetingName;

    @ApiModelProperty("会议室使用状态")
    private Integer meetingStatus;

    @ApiModelProperty("会议时间")
    private List<ReserveTimeVO> meetingTimes;

}
