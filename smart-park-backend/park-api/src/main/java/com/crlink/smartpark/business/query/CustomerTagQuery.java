package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CustomerTagQuery extends BaseQuery {
    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "客户名称")
    private String name;
}
