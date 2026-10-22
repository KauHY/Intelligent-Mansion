package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CustomerInfoQuery extends BaseQuery {

    @ApiModelProperty(value = "园区id：原来为orgId")
    private String parkOrgId;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "客户类型：0：物业公司，1：普通企业")
    private Integer type;

    @ApiModelProperty(value = "客户状态,1，已成交，2未成交，3历史成交")
    private Integer status;

    @ApiModelProperty(value = "0 全部客户，1 我的客户")
    @Min(0)
    @Max(1)
    private Integer dataType;

}
