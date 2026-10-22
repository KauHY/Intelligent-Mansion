package com.crlink.smartpark.device.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Device3dPageQuery extends BaseQuery {
    @ApiModelProperty("园区id")
    @NotEmpty
    private String parkOrgId;
    @ApiModelProperty("设备类型")
    @NotNull
    private Integer deviceType;
}
