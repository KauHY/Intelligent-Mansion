package com.crlink.smartpark.business.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WxBuildingVO implements Serializable {

    private String buildingId;

//    @Id2objectField(code = Id2objCodeConstant.BUILDING_NAME_CODE)
    private String buildingName;

    @ApiModelProperty("空置率")
    private BigDecimal vacancyRate;

    @ApiModelProperty("已租")
    private int rentedCount;
    @ApiModelProperty("待租")
    private int rentingCount;
    @ApiModelProperty("已售")
    private int soldCount;
    @ApiModelProperty("待售")
    private int sellingCount;

    @JsonIgnore
    private int totalCount;

    public WxBuildingVO(String buildingId) {
        this.buildingId = buildingId;
    }
}
