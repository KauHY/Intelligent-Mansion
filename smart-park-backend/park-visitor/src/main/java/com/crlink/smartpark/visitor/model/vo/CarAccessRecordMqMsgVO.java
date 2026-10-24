package com.crlink.smartpark.visitor.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 车辆通行MQ消息
 *
 */
@Data
public class CarAccessRecordMqMsgVO implements Serializable {
    private static final long serialVersionUID = 6959095835504110211L;

    /**
     * 车牌号
     */
    private String carNum;
    /**
     * 出入口，0：入口 1：出口
     */
    private Integer gatewayType;
    /**
     * 抓拍照片
     */
    private String picUrl;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    private Integer parkType;
    /**
     * 抓拍时间
     */
    private String time;
    /**
     * 车道ID
     */
    private String devChnid;

    /**
     * 车道名
     */
    private String devChnname;

}
