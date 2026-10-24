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
 * Meeting member list for face check-in.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mee_face_checkin_member")
@ApiModel(value = "FaceCheckinMeetingMember", description = "人脸识别签到会议成员")
public class FaceCheckinMeetingMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "会议预约id")
    private String meetingReserveId;

    @ApiModelProperty(value = "人员id")
    private String personId;

    @ApiModelProperty(value = "资格状态 1有效 0取消")
    private Integer status;

    @ApiModelProperty(value = "请假状态 0未请假 1已请假")
    private Integer leaveStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
