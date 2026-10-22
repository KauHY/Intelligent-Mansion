package com.crlink.smartpark.visitor.query;

import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 车辆通行抓拍记录关联关系
 * */
@Data
@ApiModel(value = "车辆通行抓拍记录关联关系")
public class CarAccessFullRecordListQuery extends BaseQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 车牌号
	 */
	@ApiModelProperty(value = "车牌号")
	private String carNum;

	/**
	 * 停车类型：1员工车辆,2公务车辆,3外部车辆
	 */
	@ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
	@ExcelProperty(value = "停车类型")
	private Integer parkType;

	/**
	 * 识别开始时间
	 */
	@ApiModelProperty(value = "驶入识别开始时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime enterRecognizeTimeStartTime;

	/**
	 * 识别结束时间
	 */
	@ApiModelProperty(value = "驶入识别结束时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime enterRecognizeTimeEndTime;

	/**
	 * 识别开始时间
	 */
	@ApiModelProperty(value = "驶出识别开始时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime exitRecognizeTimeStartTime;

	/**
	 * 识别结束时间
	 */
	@ApiModelProperty(value = "驶出识别结束时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime exitRecognizeTimeEndTime;

	/**
	 * 园区ID
	 */
	@ApiModelProperty(value = "园区ID")
	private String parkOrgId;

	/**
	 * 车道ID
	 */
	@ApiModelProperty(value = "车道ID")
	private String carLaneId;

	@JsonIgnore
	private Set<String> parkOrgIds;

}
