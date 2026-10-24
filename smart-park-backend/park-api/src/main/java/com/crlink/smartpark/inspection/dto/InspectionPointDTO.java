package com.crlink.smartpark.inspection.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 巡检点表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InspectionPoint对象", description = "巡检点表")
public class InspectionPointDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "巡检点名称")
    private String pointName;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "巡检点类型")
    private Integer type;

    @ApiModelProperty(value = "巡检位置")
    private String location;

    @ApiModelProperty(value = "备注")
    private String remark;

//    @ApiModelProperty(value = "修改时间")
//    private LocalDateTime updateTime;

    @ApiModelProperty(value = "修改日期")
    private LocalDate updateTime;


}
