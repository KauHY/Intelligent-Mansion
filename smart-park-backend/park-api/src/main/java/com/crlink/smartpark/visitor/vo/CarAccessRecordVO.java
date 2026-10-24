package com.crlink.smartpark.visitor.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车牌抓拍记录
 * */
@Data
@ApiModel(value = "车牌抓拍记录")
public class CarAccessRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "抓拍照片")
    private String imgUrl;


    @ApiModelProperty(value = "车牌号")
    private String carNum;

    @ApiModelProperty(value = "车辆类型，1:员工车辆、2:公务车辆、3:外部车辆、4:红名单")
    private Integer carType;


    @ApiModelProperty(value = "是否在册,0：不在册，1：在册")
    @ExcelProperty(value = "是否在册")
    private Integer carRegistered;


    @ApiModelProperty(value = "识别时间")
    private LocalDateTime recognizeTime;

    @ApiModelProperty(value = "出场时间")
    private LocalDateTime departureTime;

    //@ApiModelProperty(value = "设备名")
    //private String deviceName;

    @ApiModelProperty(value = "停车场名")
    @ExcelProperty(value = "停车场名")
    private String parkingLotName;


    @ApiModelProperty(value = "车道名")
    @ExcelProperty(value = "车道名")
    private String carLaneName;

    @ApiModelProperty(value = "出入口，0：入口 1：出口")
    @ExcelProperty(value = "出入口")
    private Integer gatewayType;


    @ApiModelProperty(value = "通行结果，0：识别不通过 1：成功放行")
    @ExcelProperty(value = "通行结果")
    private Integer accessResult;

}
