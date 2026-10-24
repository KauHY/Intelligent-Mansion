package com.crlink.smartpark.visitor.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访客记录列表查询VO
 * */
@Data
@ApiModel(value = "访客记录excel导出模型")
public class VisitorRegisterExcelExportListVO implements Serializable {
    private static final long serialVersionUID = 2134518943645783740L;

    @ApiModelProperty(value = "id")
    @ExcelIgnore
    private String id;

    /**
     * 访客名
     */
    @ApiModelProperty(value = "访客名")
    @ExcelProperty("访客名")
    private String visitorName;

    /**
     * 访客电话号码
     */
    @ApiModelProperty(value = "访客电话号码")
    @ExcelProperty("访客电话号码")
    private String visitorTel;

    /**
     * 访客类型名
     */
    @ApiModelProperty(value = "访客类型名")
    @ExcelProperty(value = "访客类型")
    private String visitorTypeName;

    /**
     * 登记类型
     */
    @ApiModelProperty(value = "登记类型:1现场登记,3门禁核验")
    @ExcelProperty(value = "登记类型", converter = DictConverter.class)
    @DictField(dictType = DictConstant.REGISTER_TYPE)
    private Integer signInType;

    /**
     * 登记核验时间
     */
    @ApiModelProperty(value = "登记核验时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "登记核验时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime signInTime;

    /**
     * 被访人姓名
     */
    @ApiModelProperty(value = "被访人姓名")
    @ExcelProperty("被访人姓名")
    private String invitorName;

}
