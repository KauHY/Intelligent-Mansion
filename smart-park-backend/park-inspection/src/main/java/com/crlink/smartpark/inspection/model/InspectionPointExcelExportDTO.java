package com.crlink.smartpark.inspection.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_FORMAT;
import static com.crlink.smartpark.inspection.common.Constant.INSPECTION_TYPE;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class InspectionPointExcelExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = "关联园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ExcelProperty(value = "巡检点ID")
    private String id;

    @ExcelProperty(value = "巡检点名称")
    private String pointName;

    @ExcelProperty(value = "巡检点类型", converter = DictConverter.class)
    @DictField(dictType = INSPECTION_TYPE)
    private Integer type;

    @ExcelProperty(value = "巡检位置")
    private String location;

    @ExcelProperty(value = "更新日期", converter = LocalDateConverter.class)
    @DateTimeFormat(DEFAULT_DATE_FORMAT)
    private LocalDate updateTime;

    @ExcelProperty(value = "备注")
    @ColumnWidth(100)
    private String remark;
}
