package com.crlink.smartpark.device.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 设备信息分页查询对象
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DeviceInfo分页查询对象", description = "设备信息")
public class DeviceInfoQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "设备类型：1-公共门禁；2-公寓门禁；3-道闸一体机；4-视频监控；5-水表；6-电表；7-会议信息屏；8-消防设备-烟感设备 9消防设备-液位设备")
    private Integer deviceType;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    /**
     * 园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     * 状态：0离线1在线
     */
    @ApiModelProperty(value = "状态：0离线1在线")
    private Integer status;

    /**
     * 设备类型：1-公共门禁；2-公寓门禁；3-道闸一体机；4-视频监控；5-水表；6-电表；7-会议信息屏；8-消防设备-烟感设备 9消防设备-液位设备
     */
    @ApiModelProperty(value = "设备类型：1-公共门禁；2-公寓门禁；3-道闸一体机；4-视频监控；5-水表；6-电表；7-会议信息屏；8-消防设备-烟感设备 9消防设备-液位设备")
    private List<Integer> deviceTypes;

    /**
     * 设备大类：0-门禁
     */
    @ApiModelProperty(value = "设备大类：0-门禁")
    private Integer deviceCategory;

}
