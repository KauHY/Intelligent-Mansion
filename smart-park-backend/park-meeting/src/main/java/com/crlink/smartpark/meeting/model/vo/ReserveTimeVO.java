package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
@NoArgsConstructor
public class ReserveTimeVO implements Serializable {

    @ApiModelProperty(value = "会议开始时间")
    private String startTime;

    @ApiModelProperty(value = "会议结束时间")
    private String endTime;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;


    public ReserveTimeVO(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ReserveTimeVO(String startTime, String endTime, String meetingSubject) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingSubject = meetingSubject;
    }
}
