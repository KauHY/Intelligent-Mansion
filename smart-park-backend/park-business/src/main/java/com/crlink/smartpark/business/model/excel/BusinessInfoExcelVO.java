package com.crlink.smartpark.business.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@ApiModel(value = "房源招商excel导出模型")
public class BusinessInfoExcelVO implements Serializable {

    @ExcelProperty(value = "关联园区")
    @ColumnWidth(30)
    private String parkOrgName;

    @ExcelProperty(value = "招商标题")
    @ColumnWidth(30)
    private String title;

    @ExcelProperty(value = "招商数量")
    private Integer amount;

    @ExcelProperty(value = "招商类型", converter = DictConverter.class)
    @DictField(dictType = DictConstant.BUSINESS_INFO_TYPE)
    private Integer effectType;

    @ExcelProperty(value = "招商面积（㎡）")
    private Double area;

    @ExcelProperty(value = "招商状态", converter = DictConverter.class)
    @DictField(dictType = DictConstant.BUSINESS_INFO_STATUS)
    private Integer status;

    @ExcelProperty(value = "截止时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime deadline;

    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime createTime;
}
