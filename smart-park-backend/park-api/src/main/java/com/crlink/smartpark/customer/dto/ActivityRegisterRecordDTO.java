package com.crlink.smartpark.customer.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报名记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ActivityRegisterRecord对象", description = "报名记录")
public class ActivityRegisterRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private String id;

    @ApiModelProperty(value = "活动ID")
    @ExcelIgnore
    private String activityId;

    @ApiModelProperty(value = "姓名")
    @ExcelProperty("姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String mobile;

    @ExcelProperty(value = "报名时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime createTime;


}
