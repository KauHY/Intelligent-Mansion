package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Face check-in request record without photo content.
 */
@Data
@ApiModel(value = "FaceCheckinRequestVO", description = "人脸识别核验请求记录")
public class FaceCheckinRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "请求id")
    private String requestId;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "会议预约id")
    private String meetingReserveId;

    @ApiModelProperty(value = "受理时间")
    private LocalDateTime acceptTime;

    @ApiModelProperty(value = "处理状态 0处理中 1完成")
    private Integer status;

    @ApiModelProperty(value = "处理结果")
    private String result;

    @ApiModelProperty(value = "识别人员id")
    private String personId;

    @ApiModelProperty(value = "识别人员姓名")
    private String personName;

    @ApiModelProperty(value = "员工工号")
    private String employeeNo;

    @ApiModelProperty(value = "员工类型")
    private Integer personType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
