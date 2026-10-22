package com.crlink.smartpark.business.dto;

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
@ApiModel(value = "BusinessHouseRelation对象", description = "")
public class BusinessHouseRelationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "招商信息Id")
    private String businessInfoId;

    @ApiModelProperty(value = "招商房屋信息Id")
    private String houseInfoId;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
