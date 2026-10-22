package com.crlink.smartpark.meeting.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Face check-in person view object.
 */
@Data
@ApiModel(value = "FaceCheckinPersonVO", description = "人脸识别签到人员")
public class FaceCheckinPersonVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "人员姓名")
    private String personName;

    @ApiModelProperty(value = "工号")
    private String employeeNo;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "人员类型 1内部员工 2访客")
    private Integer personType;

    @ApiModelProperty(value = "人脸照片URL")
    private String photoUrl;

    @ApiModelProperty(value = "状态 1启用 0停用")
    private Integer status;

    @ApiModelProperty(value = "审核状态 0待审核 1通过 2驳回")
    private Integer auditStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
