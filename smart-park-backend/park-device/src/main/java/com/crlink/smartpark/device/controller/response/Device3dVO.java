package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Device3dVO implements Serializable {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("名称")
    private String deviceName;
    @ApiModelProperty("编号")
    private String code;
    @ApiModelProperty("设备类型")
    private Integer deviceType;
    @ApiModelProperty("位置")
    private String location;
    @ApiModelProperty("状态")
    private Integer status;
}
