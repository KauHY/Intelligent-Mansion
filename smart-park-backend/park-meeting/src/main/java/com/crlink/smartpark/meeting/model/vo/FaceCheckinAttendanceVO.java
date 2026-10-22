package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Face check-in attendance view object.
 */
@Data
@ApiModel(value = "FaceCheckinAttendanceVO", description = "人脸识别签到记录")
public class FaceCheckinAttendanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "会议预约id")
    private String meetingReserveId;

    @ApiModelProperty(value = "人员id")
    private String personId;

    @ApiModelProperty(value = "人员姓名")
    private String personName;


    @ApiModelProperty(value = "员工工号")
    private String employeeNo;

    @ApiModelProperty(value = "员工类型")
    private Integer personType;

    @ApiModelProperty(value = "请求ID")
    private String requestId;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "签到时间")
    private LocalDateTime checkinTime;

    @ApiModelProperty(value = "签到状态 1成功 2已签到 3无权限 4未识别 5未开始 6已结束 7处理失败")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "人员识别分类 1与会人员 2非与会人员 3非记录人员")
    private Integer participantCategory;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
