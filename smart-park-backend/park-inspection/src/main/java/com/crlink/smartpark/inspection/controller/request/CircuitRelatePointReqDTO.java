package com.crlink.smartpark.inspection.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CircuitRelatePointReqDTO implements Serializable {
    @ApiModelProperty("巡检路线Id")
    private String circuitId;
    @ApiModelProperty("巡检点ID")
    private List<String> pointIds;
}
