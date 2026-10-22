package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@ApiModel(value="指定时段对象", description="人行通行策略")
public class SpecifiedTimePeriodDTO implements Serializable {

	@ApiModelProperty(value = "指定一周里的天，阿拉伯数字,逗号隔开")
	private String weekdays;

	@ApiModelProperty(value = "指定开始时间-时分秒")
	private LocalTime accessStartTime;

	@ApiModelProperty(value = "指定结束时间-时分秒")
	private LocalTime accessEndTime;

}
