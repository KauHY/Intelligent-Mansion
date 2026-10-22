package com.crlink.smartpark.inspection.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 巡检计划巡检人关系表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PlanEmployeeRelation对象", description = "巡检计划巡检人关系表")
public class PlanEmployeeRelationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "巡检计划Id")
    private String planId;

    @ApiModelProperty(value = "巡检人Id")
    private String employeeId;

    private Boolean deleted;


}
