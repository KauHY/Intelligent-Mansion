package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车牌抓拍记录
 * */
@Data
@ApiModel(value = "车牌抓拍记录查询bean")
public class CarAccessRecordListQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String carNum;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    @ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
    private Integer parkType;

    /**
     * 是否在册,0：不在册，1：在册
     */
    @ApiModelProperty(value = "是否在册,0：不在册，1：在册")
    private Integer carRegistered;

    /**
     * 识别开始时间
     */
    @ApiModelProperty(value = "识别开始时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime recognizeTimeStartTime;

    /**
     * 识别结束时间
     */
    @ApiModelProperty(value = "识别结束时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime recognizeTimeEndTime;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String deviceId;


    /**
     * 园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;


    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkingLotId;


    /**
     * 车道ID
     */
    @ApiModelProperty(value = "车道ID")
    private String carLaneId;


    /**
     * 出入口，0：入口 1：出口
     */
    @ApiModelProperty(value = "出入口，0：入口 1：出口")
    private Integer gatewayType;

    /**
     * 通行结果
     */
    @ApiModelProperty(value = "通行结果")
    private Integer accessResult;



}
