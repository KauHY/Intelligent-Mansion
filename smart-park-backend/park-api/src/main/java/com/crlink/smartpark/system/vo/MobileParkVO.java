package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MobileParkVO {

    @ApiModelProperty(value = "距离最近的园区")
    private DictVO nearestPark;

    @ApiModelProperty(value = "我的园区")
    private List<DictVO> myParks;

    @ApiModelProperty(value = "园区列表")
    private List<DictVO> parks;

}
