package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WxBusinessInfoQuery extends BaseQuery {
    @ApiModelProperty(value = "园区")
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "楼栋")
    private String buildingId;

    @ApiModelProperty(value = "面积")
    private Integer coverArea;

    @ApiModelProperty(value = "租金")
    private Integer unitRent;
}

