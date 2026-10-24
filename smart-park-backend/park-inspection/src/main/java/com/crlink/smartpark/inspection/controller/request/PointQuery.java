package com.crlink.smartpark.inspection.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointQuery extends BaseQuery {
    @ApiModelProperty(value = "巡检点名称")
    private String pointName;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "巡检点类型")
    private Integer type;
}
