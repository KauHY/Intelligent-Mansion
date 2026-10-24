package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 车道
 * */
@Data
@ApiModel(value = "车道")
public class CarLaneListQuery extends BaseQuery implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 车道名
	 */
	@ApiModelProperty(value = "车道名")
	private String laneName;

	/**
	 * 园区ID
	 */
	@ApiModelProperty(value = "园区ID")
	private String parkOrgId;

	/**
	 * 停车场ID
	 */
	@ApiModelProperty(value = "停车场ID")
	private String parkingLotId;







}
