package com.crlink.smartpark.inspection.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CircuitPointRelation对象", description = "")
public class CircuitPointRelationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "巡检路线")
    private String circuitId;

    @ApiModelProperty(value = "巡检点")
    private String pointId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    private Boolean deleted;


}
