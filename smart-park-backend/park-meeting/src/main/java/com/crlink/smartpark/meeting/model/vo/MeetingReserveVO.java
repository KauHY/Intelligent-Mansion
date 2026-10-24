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
public class MeetingReserveVO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "会议室类型 1：普通会议 2：视频会议")
    private Integer meetingType;

    @ApiModelProperty(value = "开始开放时间")
    private LocalTime openTimeStart;

    @ApiModelProperty(value = "结束开放时间")
    private LocalTime openTimeEnd;

    @ApiModelProperty(value = "容纳人数")
    private Integer capacity;

    @ApiModelProperty(value = "会议室设备 1摄像机 2远程会议设备 3DVR 4电话 5投影仪 6电子白板")
    private String meetingEquipment;

    @ApiModelProperty(value = "不可预定 1是 0否")
    private Integer noPermission;

    @ApiModelProperty(value = "会议时间区段详情")
    private List<ReserveDetailVO> reserveDetailVOS;

}
