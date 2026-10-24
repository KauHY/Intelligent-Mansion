package com.crlink.smartpark.visitor.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.excel.convert.LocalDateExcelConverter;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static com.crlink.smartpark.common.constants.Constants.EXCEL_DATE_PATTERN;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApartmentPersonImportDTO implements Serializable {

    @ExcelIgnore
    private String parkOrgId;

    @ExcelIgnore
    private String companyId;

    @ExcelProperty(value = "*姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 30, message = "姓名最长30个字符")
    private String employeeName;


    @ExcelProperty(value = "*手机号")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    @NotBlank(message = "联系电话不能为空")
    private String mobile;

    @ExcelProperty(value = "身份证号")
    private String idCardNumber;

    @ExcelProperty(value = "*入住楼栋")
    @NotBlank(message = "入住楼栋不能为空")
    @Length(max = 30, message = "入住楼栋最长30个字符")
    private String buildingName;

    @ExcelProperty(value = "*入住楼层房号")
    @NotBlank(message = "入住楼层房号不能为空")
    @Length(max = 30, message = "入住楼层房号最长30个字符")
    private String houseName;

    @ExcelProperty(value = "*通行授权", converter = CommonConvert.class)
    @NotEmpty(message = "通行授权不能为空")
    private List<String> deviceNames;

    @ExcelProperty(value = "*授权有效开始日期", converter = LocalDateExcelConverter.class)
    @DateTimeFormat(EXCEL_DATE_PATTERN)
    private LocalDate effectStartDate;

    @ExcelProperty(value = "*授权有效结束日期", converter = LocalDateExcelConverter.class)
    @DateTimeFormat(EXCEL_DATE_PATTERN)
    private LocalDate effectEndDate;

    @ExcelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String remark;

}
