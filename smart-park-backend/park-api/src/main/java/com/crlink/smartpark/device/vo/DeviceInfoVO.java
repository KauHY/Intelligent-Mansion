package com.crlink.smartpark.device.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 设备VO对象
 * */
@Data
@ApiModel(value = "设备VO对象")
public class DeviceInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

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

    /**
     * 设备类型：1-公共门禁；2-车辆识别设备；3-道闸；4-智能盒子；5-NVR；6-人证核验机；7-抓拍机；8-公寓门禁 9-道闸一体机 10-视频监控
     */
    @ApiModelProperty(value = "设备类型：1-公共门禁；2-车辆识别设备；3-道闸；4-智能盒子；5-NVR；6-人证核验机；7-抓拍机；8-公寓门禁 9-道闸一体机 10-视频监控")
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
     * 拓展字段，9-道闸一体机：parkingLotId-关联停车场、carLaneId-关联车道、gatewayType-出入口(0：入口 1：出口)；
     */
    @ApiModelProperty(value = "拓展字段，9-道闸一体机：parkingLotId-关联停车场、carLaneId-关联车道、gatewayType-出入口(0：入口 1：出口)；")
    private Map<String, Object> extension;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态：0离线1在线
     */
    @ApiModelProperty(value = "状态：0离线1在线")
    private Integer status;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    /**
     * 园区名
     */
    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    /**
     * 设备位置
     */
    @ApiModelProperty(value = "设备位置")
    private String location;

    /**
     * 入驻企业ID
     */
    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

    /**
     * 入驻企业名
     */
    @ApiModelProperty(value = "入驻企业名")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    private LocalDateTime createTime;


    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "楼栋名")
    @Id2objectField(code = Id2objCodeConstant.BUILDING_NAME_CODE)
    private String buildingName;

    @ApiModelProperty(value = "所在楼层")
    private Integer floor;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    @ApiModelProperty(value = "房屋名")
    @Id2objectField(code = Id2objCodeConstant.HOUSE_NAME_CODE)
    private String houseName;

//    @ApiModelProperty(value = "关联房屋")
//    private Map<String, String> houseMap;

    @ApiModelProperty(value = "智能锁通道数据")
    private List<DeviceChannelVO> lockChanelList;

}
