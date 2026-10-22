package com.crlink.smartpark.business.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 客户管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomerInfoImportDTO响应对象", description = "客户管理")
public class CustomerInfoImportDTO implements Serializable {

    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String parkOrgId;

    @ExcelProperty(value = "*客户名称")
    @NotBlank(message = "客户名称不能为空")
    @Length(max = 30, message = "客户名称最长30个字符")
    private String name;

    @ExcelProperty(value = "*统一社会信用代码")
    @NotBlank(message = "统一社会信用代码不能为空")
    @Length(min = 18, max = 18, message = "统一社会信用代码最长18个字符")
    private String code;

    @ExcelProperty(value = "*客户类型", converter = DictConverter.class)
    @DictField(dictType = "COMPANY_TYPE")
    @NotNull(message = "客户类型不能为空")
    private Integer type;


    @ExcelProperty(value = "*客户状态", converter = DictConverter.class)
    @DictField(dictType = "CUSTOMERINFO_STATUS")
    @NotNull(message = "客户状态不能为空")
    private Integer status;

    @ExcelProperty(value = "*联系人")
    @NotBlank(message = "联系人不能为空")
    @Length(max = 30, message = "联系人不能超过30字")
    private String contacts;

    @ExcelProperty(value = "*联系电话")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    @NotBlank(message = "联系电话不能为空")
    private String contactsTel;

    @ExcelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String remark;


}
