package com.crlink.smartpark.visitor.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停车场
 * */
@Data
@ApiModel(value = "停车场详细模型")
public class ParkingLotVO implements Serializable {

	private String id;

	/**
	 * 停车场编码
	 */
	private String parkingLotCode;
	/**
	 * 停车场名称
	 */
	@ApiModelProperty(value = "停车场名称")
	private String parkingLot;

	/**
	 * 总车位数
	 */
	private Integer totalLot;
	/**
	 * 使用了的车位数
	 */
	private Integer usedLot;
	/**
	 * 	余位数
	 */
	private Integer idleLot;
	/**
	 * 已停车数
	 */
	private Integer parkingNum;
	/**
	 * 绑定车场的车辆数
	 */
	private Integer carNum;

	/**
	 * 园区ID
	 */
	@ApiModelProperty(value = "园区ID")
	private String parkOrgId;
	private String parkOrgName;

	/**
	 * 租户ID
	 */
	private String tenantId;

	/**
	 * 访客通行：0-不适应，1-适用
	 */
	private Integer allowVisitor;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 修改人
	 */
	private String updateBy;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
	private LocalDateTime updateTime;

}
