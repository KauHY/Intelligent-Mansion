package com.crlink.smartpark.visitor.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 访客记录列表查询VO
 * */
@Data
@ApiModel(value = "访客记录列表模型")
public class VisitorRecordSubScribeListVO implements Serializable {
    private static final long serialVersionUID = 5182668130666873995L;

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
     * 访客类型标签，1:普通访客 2：vip 3：供应商 4：面试人员
     */
    @ApiModelProperty(value = "访客类型标签，1:普通访客 2：vip 3：供应商 4：面试人员")
    @ExcelProperty("访客类型")
    private String visitorTypeName;

    /**
     * 被访人姓名
     */
    @ApiModelProperty(value = "被访人姓名")
    @ExcelProperty("被访人姓名")
    private String invitorName;

    /**
     * 来访日期
     */
    @ApiModelProperty(value = "来访日期")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "来访日期", converter = LocalDateConverter.class)
    private LocalDate visitDate;

    /**
     * 来访事由 1:商务交流 2:面试 3:其他
     */
    @ApiModelProperty(value = "来访事由 1:商务交流 2:面试 3:其他")
    @ExcelProperty("来访事由")
    private Integer visitReason;

}
