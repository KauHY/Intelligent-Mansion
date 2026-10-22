package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Absent person view object.
 */
@Data
@ApiModel(value = "FaceCheckinAbsentVO", description = "未签到人员")
public class FaceCheckinAbsentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "人员id")
    private String personId;

    @ApiModelProperty(value = "人员姓名")
    private String personName;

    @ApiModelProperty(value = "请假状态 0未请假 1已请假")
    private Integer leaveStatus;
}
