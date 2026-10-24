package com.crlink.smartpark.inspection.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanQuery extends BaseQuery {
    @ApiModelProperty(value = "组织Id")
    private String orgId;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    private Integer status;

}
