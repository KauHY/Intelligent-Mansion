package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HouseBindVO extends HouseBindListVO{

    @ApiModelProperty(value = "企业名称")
    private String companyName;
}
