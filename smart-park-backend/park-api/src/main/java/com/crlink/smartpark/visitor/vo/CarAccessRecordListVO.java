package com.crlink.smartpark.visitor.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.excel.convert.ImageConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
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
@ApiModel(value = "车牌抓拍记录")
public class CarAccessRecordListVO implements Serializable {
    private static final long serialVersionUID = -8774030742487100072L;

    @ExcelIgnore
    private String id;

    /**
     * 抓拍照片
     */
    @ApiModelProperty(value = "抓拍照片")
    @ExcelProperty(value = "抓拍照片", converter = ImageConverter.class)
    private String imgUrl;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    @ExcelProperty(value = "车牌号")
    private String carNum;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    @ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
    @ExcelProperty(value = "停车类型")
    private Integer parkType;

    @ApiModelProperty(value = "停车类型名称：1员工车辆,2公务车辆,3外部车辆")
    @ExcelProperty(value = "停车类型名称")
    private String parkTypeName;

    /**
     * 识别时间
     */
    @ApiModelProperty(value = "识别时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "识别时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime recognizeTime;

    /**
     * 设备名
     */
    @ApiModelProperty(value = "设备名")
    @ExcelProperty(value = "设备名")
    private String deviceName;

    /**
     * 园区名
     */
    @ApiModelProperty(value = "园区名")
    @ExcelProperty(value = "园区名")
    private String parkOrgName;

    /**
     * 停车场名
     */
    @ApiModelProperty(value = "停车场名")
    @ExcelProperty(value = "停车场名")
    private String parkingLot;

    /**
     * 车道名
     */
    @ApiModelProperty(value = "车道名")
    @ExcelProperty(value = "车道名")
    private String carLaneName;

    /**
     * 出入口，0：入口 1：出口
     */
    @ApiModelProperty(value = "出入口，0：入口 1：出口")
    @ExcelProperty(value = "出入口")
    private String gatewayTypeName;

    /**
     * 通行结果
     */
    @ApiModelProperty(value = "通行结果，0：识别不通过 1：成功放行")
    @ExcelProperty(value = "通行结果")
    private String accessResultName;

}
