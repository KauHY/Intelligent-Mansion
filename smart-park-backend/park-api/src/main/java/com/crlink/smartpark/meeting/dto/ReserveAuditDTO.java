package com.crlink.smartpark.meeting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReserveAudit响应对象", description="会议室预订审核")
public class ReserveAuditDTO implements Serializable {

    @ApiModelProperty(value = "申请人ID")
    private String employeeId;

    @ApiModelProperty(value = "会议室预定id")
    private String meetingReserveId;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "容纳人数")
    private Integer capacity;

    @ApiModelProperty(value = "会议室设备 1摄像机 2远程会议设备 3DVR 4电话 5投影仪 6电子白板")
    private String meetingEquipment;

    @ApiModelProperty(value = "当前日期")
    private LocalDate nowDate;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "会议审核状态 1预定待审核、2预定成功/审核通过、3候补待审核、4候补中、5手动取消 6超时取消 7审核不通过")
    private Integer meetingStatus;



}
