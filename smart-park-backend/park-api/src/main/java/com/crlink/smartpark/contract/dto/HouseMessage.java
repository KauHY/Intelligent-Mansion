package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "HouseMessage对象", description = "合同")
public class HouseMessage {
    @ApiModelProperty(value = "房屋id")
    private String houseId;
    @ApiModelProperty(value = "楼栋名")
    private String buildingName;
    @ApiModelProperty(value = "房屋名")
    private String houseName;
    @ApiModelProperty(value = "建筑面积（㎡）")
    private String buildArea;
    /**
     * 套内面积
     */
    @ApiModelProperty(value = "套内面积（㎡）")
    private Double coverArea;
    @ApiModelProperty(value = "总楼层数")
    private Integer totalFloor;
    @ApiModelProperty(value = "所在楼层")
    private Integer floor;
    @ApiModelProperty(value = "房屋功能，字典的label")
    private String houseFunction;
}