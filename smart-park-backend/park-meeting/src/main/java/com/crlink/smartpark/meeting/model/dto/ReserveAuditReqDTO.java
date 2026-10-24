package com.crlink.smartpark.meeting.model.dto;

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
import java.util.List;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReserveAudit请求对象", description="会议室预订审核")
public class ReserveAuditReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id", required = true)
      private String id;

    @ApiModelProperty(value = "会议室预定id", required = true)
    private String meetingReserveId;

    @ApiModelProperty(value = "当前日期", required = true)
    private LocalDate nowDate;

    @ApiModelProperty(value = "开始时间", required = true)
    private String startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    private String endTime;

    @ApiModelProperty(value = "会议预定状态：1不可预定 2可预订 3已预定 4可候补", required = true)
    private Integer meetingState;

    @ApiModelProperty(value = "会议审核状态 1预定待审核、2预定成功、3候补待审核、4候补中、5手动取消 6超时取消", required = true)
    private Integer meetingStatus;

    @ApiModelProperty(value = "审核状态 1通过 2拒绝", required = true)
    private Integer auditStatus;

    @ApiModelProperty(value = "审核意见", required = true)
    private String auditMsg;

    @ApiModelProperty(value = "会议取消时间", required = true)
    private LocalDateTime meetingCancelTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否", required = true)
    private Boolean deleted;


}
