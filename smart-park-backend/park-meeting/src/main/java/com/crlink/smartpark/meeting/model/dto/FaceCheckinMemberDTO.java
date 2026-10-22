package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * Face check-in meeting member list request.
 */
@Data
@ApiModel(value = "FaceCheckinMemberDTO", description = "人脸识别签到会议成员请求")
public class FaceCheckinMemberDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议预约id", required = true)
    @NotBlank(message = "会议预约id不能为空")
    private String meetingReserveId;

    @ApiModelProperty(value = "人员id列表")
    private List<String> personIds;
}
