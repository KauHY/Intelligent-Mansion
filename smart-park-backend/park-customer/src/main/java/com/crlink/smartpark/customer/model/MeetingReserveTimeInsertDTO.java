package com.crlink.smartpark.customer.model;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingReserveTime新增请求对象", description = "会议室预订审核")
public class MeetingReserveTimeInsertDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前日期", required = true)
    @NotBlank
    private LocalDate nowDate;

    @ApiModelProperty(value = "时间段类型 1上午 2下午 3晚上", required = true)
    @NotBlank
    private Integer timeType;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotBlank
    private String startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotBlank
    private String endTime;


}
