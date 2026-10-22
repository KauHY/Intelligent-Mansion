package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 停车场
 * */
@Data
@ApiModel(value = "停车场查询模型")
public class ParkingLotListQuery extends BaseQuery implements Serializable {
	private static final long serialVersionUID = 3187920613375666855L;

	/**
	 * 停车场名称
	 */
	@ApiModelProperty(value = "停车场名称")
	private String parkingLot;

	/**
	 * 园区ID
	 */
	@ApiModelProperty(value = "园区ID")
	private String parkOrgId;







}
