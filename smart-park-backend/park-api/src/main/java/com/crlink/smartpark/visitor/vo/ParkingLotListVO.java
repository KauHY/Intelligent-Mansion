package com.crlink.smartpark.visitor.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停车场
 * */
@Data
@ApiModel(value = "停车场列表模型")
public class ParkingLotListVO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String id;

	/**
	 * 停车场编码
	 */
	@ApiModelProperty(value = "停车场编码")
	private String parkingLotCode;
	/**
	 * 停车场名称
	 */
	@ApiModelProperty(value = "停车场名称")
	private String parkingLot;

	/**
	 * 园区名
	 */
	@ApiModelProperty(value = "园区名")
	private String parkOrgName;

	/**
	 * 车位数量
	 */
	@ApiModelProperty(value = "车位总数")
	private Integer totalLot;

	/**
	 * 修改
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime updateTime;


}
