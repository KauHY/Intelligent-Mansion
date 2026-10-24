package com.crlink.smartpark.visitor.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车辆通行
 *
 */
@Data
public class CarAccessRecordDTO implements Serializable {
    private static final long serialVersionUID = 5139501303010256500L;

    /**
     * 抓拍照片
     */
    private String imgUrl;

    /**
     * 车牌号
     */
    private String carNum;

    /**
     * 识别时间
     */
    private LocalDateTime recognizeTime;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 停车编码
     */
    private String parkingLotCode;

    private String parkingLot;

    /**
     * 车道ID
     */
    private String carLaneId;

    /**
     * 车道名
     */
    private String carLaneName;

    /**
     * 出入口，0：入口 1：出口
     */
    private Integer gatewayType;

    /**
     * 通行结果：0：识别不通过 1：成功放行
     */
    private Integer accessResult;

}
