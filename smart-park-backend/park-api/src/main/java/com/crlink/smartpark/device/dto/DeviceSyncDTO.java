package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeviceSyncDTO {

    @ApiModelProperty(value = "设备类型")
    private List<Integer> deviceTypes;

    /**
     * 设备大类
     */
    @ApiModelProperty(value = "设备大类，0：门禁设备")
    private Integer deviceCategory;

}
