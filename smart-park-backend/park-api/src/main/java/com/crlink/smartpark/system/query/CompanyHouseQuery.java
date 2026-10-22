package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CompanyHouseQuery extends BaseQuery {

    @ApiModelProperty(value = "入驻企业ID")
    @NotBlank(message = "入驻企业ID不能为空")
    private String companyId;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

}
