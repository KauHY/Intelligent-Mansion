package com.crlink.smartpark.visitor.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.excel.convert.ImageConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.common.excel.convert.RecognizeResultConverter;
import com.crlink.smartpark.common.excel.convert.StatusConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 园区运营平台-人行管理-通行记录ListVO
 * */
@Data
@ApiModel(value = "人行抓拍记录ListVO对象")
@ContentRowHeight(150)
public class AccessRecordListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @ExcelIgnore
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String mobile;

    /**
     * 登记照片url
     */
    @ApiModelProperty(value = "登记照片url")
    @ExcelProperty(value = "登记照片", converter = ImageConverter.class)
    @ColumnWidth(30)
    private String faceImageUrl;

    /**
     * 抓拍照片url
     */
    @ApiModelProperty(value = "抓拍照片url")
    @ExcelProperty(value = "抓拍照片", converter = ImageConverter.class)
    @ColumnWidth(30)
    private String sceneImageUrl;

    /**
     * 健康码，0：绿色 1：黄色 2：红色
     */
    @ApiModelProperty(value = "健康码，0：绿色 1：黄色 2：红色")
    @ExcelProperty(value = "健康码", converter = DictConverter.class)
    @DictField(dictType = DictConstant.HEALTH_CODE)
    private Integer healthCode;

    /**
     * 体温是否正常
     */
    @ApiModelProperty(value = "体温是否正常")
    @ExcelProperty("体温是否正常")
    private String isOverTemp;

    /**
     * 用户标签(逗号隔开)
     */
    @ApiModelProperty(value = "用户标签(逗号隔开)")
    @ExcelProperty("标签")
    @ColumnWidth(20)
    private String labels;

    /**
     * 识别时间
     */
    @ApiModelProperty(value = "识别时间")
    @ExcelProperty(value = "通行时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime recognizeTime;

    /**
     * 设备名
     */
    @ApiModelProperty(value = "设备名称")
    @ColumnWidth(20)
    @ExcelProperty("设备名称")
    private String deviceName;

    /**
     * 园区名
     */
    @ApiModelProperty(value = "园区名")
    @ExcelProperty("园区名称")
    @ColumnWidth(20)
    private String parkOrgName;

    /**
     * 设备位置
     */
    @ApiModelProperty(value = "设备位置")
    @ExcelProperty("设备位置")
    @ColumnWidth(20)
    private String deviceLocation;

    /**
     * 是否通过：识别不通过，识别通过
     */
    @ApiModelProperty(value = "是否通过：识别不通过，识别通过")
    @ColumnWidth(20)
    @ExcelProperty(value = "通行结果", converter = RecognizeResultConverter.class)
    private Integer isPass;

}
