package com.crlink.smartpark.visitor.dto;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 园区运营平台-车行管理-车辆管理更新DTO
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Car更新对象", description="车辆信息")
public class CarUpdateDTO implements Serializable {

	private static final long serialVersionUID=1L;

	@ApiModelProperty(value = "主键", required = true)
	@NotBlank(message = "主键不能为空")
	private String id;

	/**
	 * 车牌号
	 */
	@ApiModelProperty(value = "车牌号", required = true)
	@Pattern( regexp = Constants.REGEX_CAR_PLATE, message = "车牌号格式错误")
	private String carNum;

	/**
	 * 停车类型：1员工车辆,2公务车辆,3外部车辆
	 */
	@ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆", required = true)
	@NotNull(message = "车辆类型不能为空")
	private Integer parkType;

	/**
	 * 授权开始时间
	 */
	@ApiModelProperty(value = "授权开始时间", required = true)
	@NotNull(message = "授权开始时间不能为空")
	private LocalDateTime authStartTime;

	/**
	 * 授权结束时间
	 */
	@ApiModelProperty(value = "授权结束时间", required = true)
	@NotNull(message = "授权结束时间不能为空")
	private LocalDateTime authEndTime;

	/**
	 *园区ID
	 */
	@ApiModelProperty(value = "园区ID", required = true)
	@NotBlank(message = "园区不能为空")
	private String parkOrgId;

	/**
	 *企业ID
	 */
	@ApiModelProperty(value = "企业ID", required = true)
	@NotBlank(message = "企业不能为空")
	private String companyId;

	/**
	 *企业ID
	 */
	@ApiModelProperty(value = "员工ID", required = true)
	@NotBlank(message = "员工不能为空")
	private String employeeId;

	/**
	 * 停车场ID
	 */
	@ApiModelProperty(value = "停车场ID", required = true)
	@NotEmpty(message = "停车场不能为空")
	private Set<String> parkingLotIds;

}
