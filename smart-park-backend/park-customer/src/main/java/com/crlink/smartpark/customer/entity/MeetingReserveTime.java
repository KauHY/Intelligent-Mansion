package com.crlink.smartpark.customer.entity;

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

/**
 * <p>
 * 租赁会议室预订时间管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_meeting_reserve_time")
@ApiModel(value="MeetingReserveTime数据库对象", description="租赁会议室预订时间管理")
public class MeetingReserveTime implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "会议室预定id")
    private String meetingReserveId;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "申请人id")
    private String employeeId;

    @ApiModelProperty(value = "当前日期")
    private LocalDate nowDate;

    @ApiModelProperty(value = "时间段类型 1上午 2下午 3晚上")
    private Integer timeType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "会议可用状态：0不可预定 1可预订")
    private Integer meetingState;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;


}
