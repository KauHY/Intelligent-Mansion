package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
public class ReserveAuditDetailDTO implements Serializable {

    @ApiModelProperty(value = "会议室预定id")
    @NotNull(message = "会议室预定id不能为空")
    private String meetingReserveId;

    @ApiModelProperty(value = "详情类型 0：预定审核详情 1我的预定详情")
    private Integer type;


}
