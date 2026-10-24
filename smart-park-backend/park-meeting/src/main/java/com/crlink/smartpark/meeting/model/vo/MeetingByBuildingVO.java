package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 会议室预订表
 * </p>
 * */
@Data
public class MeetingByBuildingVO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "会议室类型 1：普通会议 2：视频会议")
    private Integer meetingType;

    @ApiModelProperty(value = "容纳人数")
    private Integer capacity;

}
