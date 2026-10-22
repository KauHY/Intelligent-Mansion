package com.crlink.smartpark.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel(value="MeetingReserveTime响应对象", description="租赁会议室预订时间管理")
public class MeetingReserveTimeDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private String id;

    @ApiModelProperty(value = "会议室预定id")
    private String meetingReserveId;

    @ApiModelProperty(value = "当前日期")
    private LocalDate nowDate;

    @ApiModelProperty(value = "时间段类型 1上午 2下午 3晚上")
    private Integer timeType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;


}
