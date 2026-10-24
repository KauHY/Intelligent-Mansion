package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
public class ReserveAuditCancelDTO implements Serializable {

    @ApiModelProperty(value = "会议室预定id")
    @NotNull(message = "会议室预定id不能为空")
    private String meetingReserveId;

    @ApiModelProperty(value = "会议审核状态 1预定待审核、2预定成功、3候补待审核、4候补中、5手动取消 6超时取消 7审核不通过")
    private Integer meetingStatus;


}
