package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

import java.util.List;
import java.util.Set;

/**
 * @description: 入驻记录列表查询传参
 */
@Data
public class HouseBindRecordQuery extends BaseQuery {

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "入驻企业名")
    private String companyName;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    @JsonIgnore
    private Set<String> parkOrgIds;

}
