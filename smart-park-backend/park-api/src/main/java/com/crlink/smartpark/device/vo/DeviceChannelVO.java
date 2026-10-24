package com.crlink.smartpark.device.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备VO对象
 * */
@Data
@ApiModel(value = "DeviceChannelVO对象")
public class DeviceChannelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联房屋id")
    private String houseId;

    /**
     * 通道名称
     */
    @ApiModelProperty(value = "通道名称")
    private String channelName;

    @ApiModelProperty(value = "通道编码(用作key)")
    private String channelNo;

    /**
     * 通道唯一标识码
     */
    @ApiModelProperty(value = "通道唯一标识码")
    private String channelSn;

//    private String ownerCode;

//    /**
//     * 通道序号
//     */
//    @ApiModelProperty(value = "通道序号")
//    private int channelSeq;

//    private int isVirtual;

//    @ApiModelProperty(value = "通道名称")
//    private int isOnline;
//    /**
//     * 通道类型
//     */
//    @ApiModelProperty(value = "通道类型")
//    private String channelType;
//    /**
//     * 是否已接入：0-未接入， 1-接入
//     */
//    @ApiModelProperty(value = "通道名称")
//    private int access;
//    /**
//     * 通道能力集
//     */
//    private String capability;
//    /**
//     * 经度
//     */
//    private String gpsX;
//    /**
//     * 纬度
//     */
//    private String gpsY;
//    /**
//     * Z轴
//     */
//    private String gpsZ;
//    /**
//     * 通道描述
//     */
//    private String memo;
//    /**
//     * 状态： 0-不启用，1-启用
//     */
//    private int stat;
//    /**
//     * 通道扩展属性 自定义
//     */
//    private Map<String, Object> chExt;

}
