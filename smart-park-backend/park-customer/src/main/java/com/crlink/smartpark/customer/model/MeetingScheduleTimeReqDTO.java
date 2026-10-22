package com.crlink.smartpark.customer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 可预定时段管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingScheduleTime请求对象", description="可预定时段管理")
public class MeetingScheduleTimeReqDTO implements Serializable {

    @ApiModelProperty(value = "开始时间", required = true)
    @NotBlank
    private String startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotBlank
    private String endTime;

    @ApiModelProperty(value = "时间段类型 1上午 2下午 3晚上", required = true)
    @NotNull
    private Integer timeType;

}
