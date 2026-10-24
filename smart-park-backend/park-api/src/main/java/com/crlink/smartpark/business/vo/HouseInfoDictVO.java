package com.crlink.smartpark.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class HouseInfoDictVO extends HouseBaseVO implements Serializable {
    @ApiModelProperty("招商房屋Id")
    private String houseInfoId;
    @ApiModelProperty("建筑面积")
    private Double buildArea;
    //private LocalDateTime createTime;
    @ApiModelProperty("最早租期")
    private LocalDate earliestRentTime;
    @ApiModelProperty("单位租金")
    private String unitRent;
}
