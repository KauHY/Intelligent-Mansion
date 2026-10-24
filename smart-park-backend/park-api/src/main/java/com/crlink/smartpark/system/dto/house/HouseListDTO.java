package com.crlink.smartpark.system.dto.house;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;


@Data
public class HouseListDTO extends BaseQuery {

    @ApiModelProperty(value = "园区ID")
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    @ApiModelProperty(value = "楼层房号")
    private String houseName;

    @ApiModelProperty(value = "房屋状态")
    private Integer houseStatus;

    @ApiModelProperty(value = "房屋功能")
    private Integer houseFunction;

    //private

    @ApiModelProperty(value = "非房屋属性")
    private Integer notHouseProperty;

    @ApiModelProperty(value = "不在ids范围内")
    private Set<String> noIds;

    @ApiModelProperty(value = "房屋状态")
    private Set<Integer> houseStatusSet;

    @ApiModelProperty(value = "headOrgId")
    @JsonIgnore
    private String headOrgId;
    @JsonIgnore
    private Set<String> parkOrgIds;

    @ApiModelProperty(value = "是否需要楼栋名、房屋名排序")
    private Boolean needPySort = false;
}
