package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApartmentPersonHouse", description = "")
public class ApartmentPersonHouseDTO implements Serializable {

    @ApiModelProperty(value = "关联房屋id")
    private String houseId;

    @ApiModelProperty(value = "关联房屋名称：楼栋名+楼层房号")
    private String houseName;

    @ApiModelProperty(value = "房屋功能：HOUSE_FUNCTION")
    private Integer houseFunction;

}
