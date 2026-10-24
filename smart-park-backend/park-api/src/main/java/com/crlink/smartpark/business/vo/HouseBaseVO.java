package com.crlink.smartpark.business.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.*;

@Data
public class HouseBaseVO implements Serializable {
    @ApiModelProperty("房屋Id")
    @Id2objectField(code = HOUSE_NAME_CODE, targetFieldName = "name")
    private String houseId;
    @ApiModelProperty("房屋名")
    private String name;
    @ApiModelProperty("园区名")
    private String parkOrgName;
    private String parkOrgId;
    @ApiModelProperty("楼栋名")
    private String buildingName;
    @Id2objectField(code = BUILDING_NAME_CODE, targetFieldName = "buildingName")
    private String buildingId;
}
