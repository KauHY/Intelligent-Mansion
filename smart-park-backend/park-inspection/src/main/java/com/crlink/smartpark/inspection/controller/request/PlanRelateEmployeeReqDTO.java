package com.crlink.smartpark.inspection.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class PlanRelateEmployeeReqDTO implements Serializable {
    @ApiModelProperty("计划Id")
    private String planId;
    @ApiModelProperty("巡检人ID")
    @Size(min = 1)
    private List<String> employeeIds;
}
