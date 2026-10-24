package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 访客页面输入配置
 * */
@Data
@ApiModel(value = "访客页面输入配置列表查询bean")
public class PageConfigListQuery extends BaseQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面类型，1：访客预约 2：审核 3：员工邀约 4：访客补充信息
	 */
	@ApiModelProperty(value = "页面类型，1：访客预约 2：审核 3：员工邀约 4：访客补充信息")
	private Integer pageType;

	/**
	 * 园区ID
	 */
	@ApiModelProperty(value = "园区ID")
	private String parkOrgId;

}
