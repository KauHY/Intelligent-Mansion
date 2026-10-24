package com.crlink.smartpark.customer.model;

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
 * 租赁会议室预订时间管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserveTime请求对象", description="租赁会议室预订时间管理")
public class MeetingReserveTimeReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id", required = true)
      private String id;

    @ApiModelProperty(value = "会议室预定id", required = true)
    private String meetingReserveId;

    @ApiModelProperty(value = "会议室id", required = true)
    private String meetingId;

    @ApiModelProperty(value = "申请人id", required = true)
    private String employeeId;

    @ApiModelProperty(value = "当前日期", required = true)
    private LocalDate nowDate;

    @ApiModelProperty(value = "时间段类型 1上午 2下午 3晚上", required = true)
    private Integer timeType;

    @ApiModelProperty(value = "开始时间", required = true)
    private String startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    private String endTime;

    @ApiModelProperty(value = "会议可用状态：0不可预定 1可预订", required = true)
    private Integer meetingState;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除 1是 0否", required = true)
    private Boolean deleted;


}
