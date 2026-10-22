package com.crlink.smartpark.device.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 设备信息分页查询对象
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value = "DeviceInfo分页查询对象", description = "设备信息")
public class ScreenDeviceQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;


    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "状态：0离线,1休眠，2运行")
    private Integer screenDeviceStatus;

}
