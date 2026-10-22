package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 停车场
 * */
@Data
@ApiModel(value = "停车场更新模型")
public class ParkingLotUpdateDTO implements Serializable {
	private static final long serialVersionUID = -5962576679691082046L;

	@ApiModelProperty(value = "id")
	@NotBlank(message = "不能为空")
	private String id;

	/**
	 * 停车场名称
	 */
	@ApiModelProperty(value = "停车场名称", required = true)
	@NotBlank(message = "停车场名称不能为空")
	private String parkingLot;

	/**
	 * 访客通行：0-不适应，1-适用
	 */
	@ApiModelProperty(value = "访客通行：0-不适应，1-适用", required = true)
	@NotNull(message = "访客通行不能为空")
	private Integer allowVisitor;

}
