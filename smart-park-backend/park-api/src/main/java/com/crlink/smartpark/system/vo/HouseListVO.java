package com.crlink.smartpark.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HouseListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "楼栋名")
    private String buildingName;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "楼层房号")
    private String name;

    @ApiModelProperty(value = "房屋编码")
    private String code;

    @ApiModelProperty(value = "所在楼层")
    private Integer floor;

    @ApiModelProperty(value = "套内面积")
    private Double coverArea;

    @ApiModelProperty(value = "建筑面积")
    private Double buildArea;

//    @ApiModelProperty(value = "房屋状态：自用，待租， 已租，代售，已售")
//    private Integer houseStatus;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;


    @ApiModelProperty(value = "楼层属性")
    private Integer floorType;

    @ApiModelProperty(value = "房屋属性")
    private Integer houseProperty;

    @ApiModelProperty(value = "房屋功能")
    private Integer houseFunction;

    @ApiModelProperty(value = "总层数")
    private Integer totalFloor;

    @ApiModelProperty(value = "朝向")
    private Integer orientation;

    @ApiModelProperty(value = "创建人")
    @JsonIgnore
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonIgnore
    private String updateBy;

    @ApiModelProperty(value = "备注")
    private String remark;
}
