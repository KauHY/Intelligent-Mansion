package com.crlink.smartpark.visitor.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.excel.convert.ImageConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CarAccessFullRecordListVO implements Serializable {
    private static final long serialVersionUID = 3234784857324677429L;

    /**
     *
     */
    @ApiModelProperty(value = "")
    @ExcelIgnore
    private String id;

    /**
     * 进入抓拍照片
     */
    @ApiModelProperty(value = "进入抓拍照片")
    @ExcelProperty(value = "进入抓拍照片", converter = ImageConverter.class)
    private String enterImgUrl;

    /**
     * 驶出抓拍照片
     */
    @ApiModelProperty(value = "驶出抓拍照片")
    @ExcelProperty(value = "驶出抓拍照片", converter = ImageConverter.class)
    private String exitImgUrl;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    @ExcelProperty(value = "车牌号")
    private String carNum;

    @ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
    @ExcelProperty(value = "停车类型")
    private String parkTypeName;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    @ExcelIgnore
    @ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
    @ExcelProperty(value = "停车类型")
    private Integer parkType;

    /**
     * 园区名
     */
    @ApiModelProperty(value = "园区名")
    @ExcelProperty(value = "园区名")
    private String parkOrgName;

    /**
     * 驶入车道名
     */
    @ApiModelProperty(value = "驶入车道名")
    @ExcelProperty(value = "驶入车道名")
    private String enterCarLaneName;

    /**
     * 驶出车道名
     */
    @ApiModelProperty(value = "驶出车道名")
    @ExcelProperty(value = "驶出车道名")
    private String exitCarLaneName;

    /**
     * 驶入时间
     */
    @ApiModelProperty(value = "驶入时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "驶入时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime enterRecognizeTime;

    /**
     * 驶离时间
     */
    @ApiModelProperty(value = "驶离时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "驶离时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime exitRecognizeTime;

}
