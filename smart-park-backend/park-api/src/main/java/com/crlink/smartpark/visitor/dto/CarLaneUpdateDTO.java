package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 车道
 * */
@Data
@ApiModel(value = "车道")
public class CarLaneUpdateDTO implements Serializable {
	private static final long serialVersionUID = 1893897421339750942L;

	@ApiModelProperty(value = "id")
	@NotBlank(message = "不能为空")
	private String id;

	/**
	 * 车道名
	 */
	@ApiModelProperty(value = "车道名", required = true)
	@NotBlank(message = "车道名不能为空")
	private String laneName;

}
