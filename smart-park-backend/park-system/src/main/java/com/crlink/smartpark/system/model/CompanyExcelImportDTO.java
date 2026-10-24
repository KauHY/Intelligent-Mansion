package com.crlink.smartpark.system.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CompanyExcelImportDTO implements Serializable {

    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String parkOrgId;

    @ExcelProperty(index = 0, value = "*企业名称")
    @NotEmpty(message = "企业名称")
    private String name;

    @ExcelProperty(index = 1, value = "*企业类型", converter = DictConverter.class)
    @DictField(dictType = "COMPANY_TYPE")
    private Integer companyType;

    @ExcelProperty(index = 2, value = "*统一社会信用代码")
    @NotNull(message = "统一社会信用代码为空")
    private String code;

    @ExcelProperty(index = 3, value = "注册地址")
    @Length(max = 30, message = "注册地址不能超过30字！")
    private String address;

    @ExcelProperty(index = 4, value = "联系人")
    @Length(max = 30, message = "联系人不能超过30字！")
    private String contacts;

    @ExcelProperty(index = 5, value = "联系电话")
    private String tel;


}
