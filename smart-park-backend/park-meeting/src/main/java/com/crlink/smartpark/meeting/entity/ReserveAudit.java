package com.crlink.smartpark.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mee_reserve_audit")
@ApiModel(value="ReserveAudit数据库对象", description="会议室预订审核")
public class ReserveAudit implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "会议室预定id")
    private String meetingReserveId;

    @ApiModelProperty(value = "申请人id")
    private String employeeId;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "当前日期")
    private LocalDate nowDate;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "会议预定状态：1不可预定 2可预订 3已预定 4可候补")
    private Integer meetingState;

    @ApiModelProperty(value = "会议审核状态 1预定待审核、2预定成功/审核通过、3候补待审核、4候补中、5手动取消 6超时取消 7审核不通过")
    private Integer meetingStatus;

    @ApiModelProperty(value = "审核人")
    private String reviewId;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime reviewTime;

    @ApiModelProperty(value = "审核状态 0未审核 1已审核")
    private Integer reviewStatus;

    @ApiModelProperty(value = "审核意见")
    private String reviewMsg;

    @ApiModelProperty(value = "会议取消时间")
    private LocalDateTime meetingCancelTime;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;


}
