package com.crlink.smartpark.inspection.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.crlink.smartpark.inspection.common.Constant.INSPECTION_TYPE;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class InspectionPointExcelImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String parkOrgId;

    //    @ExcelProperty(value = "*巡检点名称")
    @ExcelProperty(index = 0, value = "*巡检点名称")
    @NotEmpty(message = "巡检点名称不能为空")
    private String pointName;

    //    @ExcelProperty(value = "*巡检点类型", converter = DictConverter.class)
    @ExcelProperty(index = 1, value = "*巡检点类型", converter = DictConverter.class)
    @DictField(dictType = INSPECTION_TYPE)
    @NotNull(message = "巡检点类型不能为空")
    private Integer type;

    //    @ExcelProperty(value = "*巡检点位置")
    @ExcelProperty(index = 2, value = "*巡检点位置")
    @NotEmpty(message = "巡检点位置不能为空")
    private String location;

    //    @ExcelProperty(value = "备注")
    @ExcelProperty(index = 3, value = "备注")
    @ColumnWidth(100)
    @Length(max = 99, message = "备注长度不能超过99")
    private String remark;
}
