package com.crlink.smartpark.system.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.LocalDateExcelConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

import static com.crlink.smartpark.common.constants.Constants.EXCEL_DATE_PATTERN;
import static com.crlink.smartpark.system.enums.Constant.BUILDING_PROPERTY;
import static com.crlink.smartpark.system.enums.Constant.HAVE_OR_NOT_HAVE;

@Data
public class BuildingExcelImportDTO implements Serializable {
    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String parkOrgId;

    //    @ExcelProperty("*楼栋单元名称")
    @ExcelProperty(index = 0, value = "*楼栋单元名称")
    @NotBlank(message = "楼栋单元名称不能为空")
    @Length(max = 30, message = "楼栋名称不能超过30字")
    private String name;

    //    @ExcelProperty("楼栋单元编码")
    @ExcelProperty(index = 1, value = "楼栋单元编码")
    private String code;

    //    @ExcelProperty("*总楼层")
    @ExcelProperty(index = 2, value = "*总楼层")
    @NotNull(message = "总楼层不能为空")
    private Integer totalFloor;

    @ExcelProperty(index = 3, value = "*楼栋属性", converter = DictConverter.class)
    @DictField(dictType = BUILDING_PROPERTY)
    @NotNull(message = "楼栋属性不能为空！")
    private Integer property;

    //    @ExcelProperty(value = "建成时间", converter = LocalDateExcelConverter.class)
    @ExcelProperty(index = 4, value = "建成时间", converter = LocalDateExcelConverter.class)
    @DateTimeFormat(EXCEL_DATE_PATTERN)
    private LocalDate buildTime;

    //    @ExcelProperty(value = "有无电梯", converter = DictConverter.class)
    @ExcelProperty(index = 5, value = "*有无电梯", converter = DictConverter.class)
    @DictField(dictType = HAVE_OR_NOT_HAVE)
    @NotNull(message = "有无电梯不能为空")
    private Integer elevator;

    //    @ExcelProperty("总占地面积(㎡)")
    @ExcelProperty(index = 6, value = "总占地面积(㎡)")
    private Double coverArea;

    //    @ExcelProperty("总建筑面积(㎡)")
    @ExcelProperty(index = 7, value = "总建筑面积(㎡)")
    private Double buildArea;

    @ExcelProperty(index = 8, value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String remark;


    //@ExcelProperty("*套内面积(㎡)")
    //@NotNull(message = "套内面积不能为空")
    //private Double coverArea;

    //@ExcelProperty("房屋功能")
    //private Integer houseFunction;
    //
    //@ExcelProperty("房屋朝向")
    //private Integer orientation;
    //
    //@ExcelProperty("*房屋属性")
    //@NotNull(message = "房屋属性不能为空")
    //private Integer houseProperty;


    /**
     * 电梯：0：无，1：有
     */
    //private Integer elevator;

    /**
     * 占地面积
     */
    //private Double coverArea;

    /**
     * 建筑面积
     */
    //private Double buildArea;

}
