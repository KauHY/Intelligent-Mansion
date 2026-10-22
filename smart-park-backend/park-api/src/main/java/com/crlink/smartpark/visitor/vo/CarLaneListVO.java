package com.crlink.smartpark.visitor.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 车道
 * */
@Data
@ApiModel(value = "车道")
public class CarLaneListVO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String id;

	/**
	 * 车道名
	 */
	@ApiModelProperty(value = "车道名")
	private String laneName;

	/**
	 * 园区ID
	 */
	@ApiModelProperty(value = "园区名")
	private String parkOrgName;

	/**
	 * 停车场ID
	 */
	@ApiModelProperty(value = "停车场名")
	private String parkingLot;

	/**
	 * 行驶方向
	 */
	@ApiModelProperty(value = "行驶方向")
	private Integer carDirect;

	@ApiModelProperty(value = "行驶方向文本")
	private String carDirectStr;

	private Integer isOnline;
	private String isOnlineStr;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime updateTime;

}
