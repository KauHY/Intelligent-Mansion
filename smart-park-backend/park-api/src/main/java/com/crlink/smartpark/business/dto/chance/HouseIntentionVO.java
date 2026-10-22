package com.crlink.smartpark.business.dto.chance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 意向房源
 * @Created by ZHOUXINJIAN3
 */
@Data
public class HouseIntentionVO implements Serializable {
    @ApiModelProperty("楼栋")
    private String buildingName;
    @ApiModelProperty("招商房屋ID")
    private String houseInfoId;
    @ApiModelProperty("房屋")
    private String name;

}
