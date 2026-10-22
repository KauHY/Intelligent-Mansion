package com.crlink.smartpark.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessHouseVO implements Serializable {

    @ApiModelProperty(value = "招商信息id")
    private String businessInfoId;

    @ApiModelProperty(value = "招商信息标题图片")
    private String titlePicture;

    @ApiModelProperty("房屋Id")
    private String houseId;

    @ApiModelProperty("招商房源Id")
    private String houseInfoId;

    @ApiModelProperty("房屋名称（楼栋单元+楼层房号）")
    private String name;

    @ApiModelProperty("园区名称")
    private String parkOrgName;

    @ApiModelProperty("建筑面积")
    private Double buildArea;

    @ApiModelProperty(value = "房屋状态")
    private Integer houseStatus;

    @ApiModelProperty(value = "单位租金")
    private String unitRent;

    @ApiModelProperty(value = "看房时间")
    private String visitTime;

    @ApiModelProperty(value = "免租期")
    private Integer freeRentMonth;

    @ApiModelProperty(value = "工位数")
    private Integer stationNum;


    @ApiModelProperty(value = "朝向")
    private Integer orientation;
}
