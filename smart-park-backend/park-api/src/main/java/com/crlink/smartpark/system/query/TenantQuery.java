package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TenantQuery extends BaseQuery {
    @ApiModelProperty(value = "租户名称")
    private String orgName;
    @ApiModelProperty(value = "状态")
    private Integer status;
}
