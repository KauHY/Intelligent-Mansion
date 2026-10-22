package com.crlink.smartpark.business.vo;

import com.crlink.smartpark.business.vo.BusinessHouseVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WxHouseVO implements Serializable {
    @ApiModelProperty("楼栋")
    private String buildingName;
    @ApiModelProperty("房屋总数")
    private Integer totalHouse;
    @ApiModelProperty("总面积")
    private Double totalArea;
    @ApiModelProperty("已租面积")
    private Double rentedArea;
    @ApiModelProperty("待租面积")
    private Double rentingArea;
    @ApiModelProperty("已售面积")
    private Double soldArea;
    @ApiModelProperty("待售面积")
    private Double sellingArea;

//    private PageInfo<BusinessHouseVO> businessHouseVOS;
}
