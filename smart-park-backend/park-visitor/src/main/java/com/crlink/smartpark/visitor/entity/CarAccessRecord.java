package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车牌抓拍记录
 * */
@Data
@TableName("vis_car_access_record")
public class CarAccessRecord implements Serializable {
    private static final long serialVersionUID = 4676049618656840124L;

    @TableId
    private String id;

    /**
     * 抓拍照片
     */
    private String imgUrl;

    /**
     * 车牌号
     */
    private String carNum;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    private Integer parkType;

    /**
     * 识别时间
     */
    private LocalDateTime recognizeTime;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名
     */
    private String deviceName;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 园区名
     */
    private String parkOrgName;

    /**
     * 停车场ID
     */
    private String parkingLotId;

    /**
     * 停车场名
     */
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除状态
     */
    private Boolean deleted;


}
