package com.crlink.smartpark.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDTO implements Serializable {
    @ApiModelProperty(value = "地址名称")
    private String name;
    @ApiModelProperty(value = "经度")
    private String longitude;
    @ApiModelProperty(value = "纬度")
    private String latitude;
}
