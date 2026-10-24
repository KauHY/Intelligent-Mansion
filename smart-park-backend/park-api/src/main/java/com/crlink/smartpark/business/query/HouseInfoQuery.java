package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class HouseInfoQuery extends BaseQuery {
    @ApiModelProperty(value = "招商时效类型,1:长期有效")
    private Integer effectType;


    @ApiModelProperty(value = "咨询顾问名")
    private String employeeName;

    @ApiModelProperty(value = "组织Id")
    private String orgId;

    @JsonIgnore
    private Set<String> parkOrgIds;

    @ApiModelProperty(value = "楼栋id")
    private String buildingId;

    @ApiModelProperty(value = "房号id")
    private String houseId;

    @JsonIgnore
    private Set<String> employeeIds;

    @ApiModelProperty(value = "招商状态")
    private Integer businessStatus;

}
