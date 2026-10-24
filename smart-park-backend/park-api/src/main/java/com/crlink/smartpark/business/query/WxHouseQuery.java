package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WxHouseQuery extends BaseQuery {
    @ApiModelProperty(value = "楼栋Id")
    @NotEmpty
    private String buildingId;

    @ApiModelProperty(value = "房屋功能")
    private Integer houseFunction;

    @ApiModelProperty(value = "房屋状态")
    private Integer houseStatus;
}
