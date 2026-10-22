package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: MobileParkInfoVO
 * @Package com.crlink.smartpark.system.vo
 * @Description: 园区介绍INFO
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MobileParkInfoVO {

    @ApiModelProperty(value = "园区组织id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String name;

    @ApiModelProperty(value = "图片")
    private String pics;

    @ApiModelProperty(value = "描述")
    private String description;

}
