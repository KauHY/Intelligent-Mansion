package com.crlink.smartpark.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Person registered for face check-in.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mee_face_checkin_person")
@ApiModel(value = "FaceCheckinPerson", description = "人脸识别签到人员")
public class FaceCheckinPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "人员姓名")
    private String personName;

    @ApiModelProperty(value = "工号（内部员工）")
    private String employeeNo;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "人员类型 1内部员工 2访客")
    private Integer personType;

    @ApiModelProperty(value = "人脸照片文件路径")
    private String photoPath;

    @ApiModelProperty(value = "人脸特征向量（base64）")
    private String faceVector;

    @ApiModelProperty(value = "模型版本")
    private String modelVersion;

    @ApiModelProperty(value = "状态 1启用 0停用")
    private Integer status;

    @ApiModelProperty(value = "审核状态 0待审核 1通过 2驳回")
    private Integer auditStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;
}
