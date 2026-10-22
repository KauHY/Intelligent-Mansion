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
 * Face check-in verification request record.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mee_face_checkin_request")
@ApiModel(value = "FaceCheckinRequest", description = "人脸识别核验请求")
public class FaceCheckinRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    @ApiModelProperty(value = "照片文件路径")
    private String photoPath;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
