package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class HouseQuery extends BaseQuery {
    @ApiModelProperty(value = "园区ID", required = true)
    @NotEmpty(message = "园区ID不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    @ApiModelProperty(value = "房源招商")
    private String businessInfoId;


}
