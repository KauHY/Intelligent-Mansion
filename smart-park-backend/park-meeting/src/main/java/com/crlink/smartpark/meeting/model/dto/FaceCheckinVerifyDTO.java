package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "FaceCheckinVerifyDTO", description = "人脸识别签到核验请求")
public class FaceCheckinVerifyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id", required = true)
    @NotBlank(message = "设备id不能为空")
    private String deviceId;

    @ApiModelProperty(value = "设备令牌")
    private String deviceToken;

    @ApiModelProperty(value = "请求id", required = true)
    @NotBlank(message = "请求id不能为空")
    private String requestId;

    @ApiModelProperty(value = "会议预约id", required = true)
    @NotBlank(message = "会议预约id不能为空")
    private String meetingReserveId;

    @ApiModelProperty(value = "人脸照片base64编码")
    private String photoBase64;
}
