package com.crlink.smartpark.system.dto.house;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class HouseRentData {

    @ApiModelProperty(value = "待租房间数")
    private Integer rentingHouseCount = 0;

    @ApiModelProperty(value = "已租房间数")
    private Integer rentedHouseCount = 0;

    @ApiModelProperty(value = "待租房间面积")
    private BigDecimal rentingBuildArea = BigDecimal.ZERO;

    @ApiModelProperty(value = "已租房间面积")
    private BigDecimal rentedBuildArea = BigDecimal.ZERO;

    @ApiModelProperty(value = "房间总数")
    private Integer totalHouseCount = 0;

    @ApiModelProperty(value = "总面积(平米)")
    private BigDecimal totalBuildArea = BigDecimal.ZERO;

    @ApiModelProperty(value = "总商机(条)")
    private Integer chanceCount = 0;

    @ApiModelProperty(value = "入驻企业数")
    private Integer companyCount = 0;

    @ApiModelProperty("入驻率")
    private BigDecimal occupancyRate;
}
