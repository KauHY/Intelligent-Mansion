package com.crlink.smartpark.inspection.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CircuitQuery extends BaseQuery {
    @ApiModelProperty(value = "组织ID")
    private String orgId;
    @ApiModelProperty(value = "巡检路线名")
    private String circuitName;
    @ApiModelProperty(value = "巡检类型")
    private Integer type;
}
