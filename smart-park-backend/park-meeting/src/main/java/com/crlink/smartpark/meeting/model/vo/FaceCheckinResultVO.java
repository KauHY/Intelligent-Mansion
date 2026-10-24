package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Face check-in verify result returned to terminal.
 */
@Data
@ApiModel(value = "FaceCheckinResultVO", description = "人脸识别签到核验结果")
public class FaceCheckinResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结果状态 1签到成功 2已签到 3无权限 4未识别 5未开始 6已结束 7处理失败")
    private Integer status;

    @ApiModelProperty(value = "结果描述")
    private String message;

    @ApiModelProperty(value = "人员姓名（隐私设置控制是否返回）")
    private String personName;


    @ApiModelProperty(value = "员工工号")
    private String employeeNo;

    @ApiModelProperty(value = "员工类型：1工作人员 2访客")
    private Integer personType;

    @ApiModelProperty(value = "人员识别分类 1与会人员 2非与会人员 3非记录人员")
    private Integer participantCategory;

    @ApiModelProperty(value = "会议ID")
    private String meetingReserveId;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;

    @ApiModelProperty(value = "请求id")
    private String requestId;
}
