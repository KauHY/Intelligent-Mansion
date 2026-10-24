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
 * Face check-in attendance record.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mee_face_checkin_attendance")
@ApiModel(value = "FaceCheckinAttendance", description = "人脸识别签到记录")
public class FaceCheckinAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "会议预约id")
    private String meetingReserveId;

    @ApiModelProperty(value = "人员id")
    private String personId;

    @ApiModelProperty(value = "人员姓名")
    private String personName;

    @ApiModelProperty(value = "签到时间")
    private LocalDateTime checkinTime;

    @ApiModelProperty(value = "签到状态 1成功 2已签到 3无权限 4未识别 5未开始 6已结束 7处理失败")
    private Integer status;

    @ApiModelProperty(value = "请求id")
    private String requestId;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "照片文件路径")
    private String photoPath;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "人员识别分类 1与会人员 2非与会人员 3非记录人员")
    private Integer participantCategory;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
