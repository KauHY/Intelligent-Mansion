package com.crlink.smartpark.business.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CustomerInfoExcelVO {

//    private String id;

    @ApiModelProperty(value = "客户编码")
    @ExcelProperty(value = "客户编码")
    @ColumnWidth(20)
    private String customerCode;

    @ApiModelProperty(value = "所属园区")
    @ExcelProperty(value = "所属园区")
    @ColumnWidth(30)
    private String parkOrgName;

    @ApiModelProperty(value = "客户名称")
    @ExcelProperty(value = "客户名称")
    @ColumnWidth(30)
    private String name;

//    @ApiModelProperty(value = "统一社会信用代码")
//    @ExcelProperty(value = "统一社会信用代码")
//    private String code;

    @ApiModelProperty(value = "客户标签名")
    @ExcelProperty(value = "客户标签")
    @ColumnWidth(30)
    private String tagName;

    @ApiModelProperty(value = "客户类型 0：物业公司，1：普通企业")
    @ExcelProperty(value = "客户类型", converter = DictConverter.class)
    @DictField(dictType = "COMPANY_TYPE")
    private Integer type;

    @ApiModelProperty(value = "客户状态,1，已成交，2未成交，3历史成交")
    @ExcelProperty(value = "客户状态", converter = DictConverter.class)
    @DictField(dictType = DictConstant.CUSTOMERINFO_STATUS)
    private Integer status;

    @ApiModelProperty(value = "联系人")
    @ExcelProperty(value = "联系人")
    @ColumnWidth(30)
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @ExcelProperty(value = "联系电话")
    @ColumnWidth(20)
    private String tel;

//    @ApiModelProperty(value = "联系人邮箱")
//    @ExcelProperty(value = "联系人邮箱")
//    private String contactsMail;
//
//    @ApiModelProperty(value = "联系人部门")
//    @ExcelProperty(value = "联系人部门")
//    private String contactsDepartment;
//
//    @ApiModelProperty(value = "联系人职位")
//    @ExcelProperty(value = "联系人职位")
//    private String contactsPosition;
//
//    @ApiModelProperty(value = "跟进人名称")
//    @ExcelProperty(value = "跟进人名称")
//    private String employeeName;
//
//    @ApiModelProperty(value = "行业领域")
//    @ExcelProperty(value = "行业领域", converter = DictConverter.class)
//    @DictField(dictType = DictConstant.INDUSTRY_FIELD)
//    private Integer industryField;
//
//    @ApiModelProperty(value = "企业规模(人数)")
//    @ExcelProperty(value = "企业规模(人数)")
//    private Integer companyScale;
//
//    @ApiModelProperty(value = "法人代表")
//    @ExcelProperty(value = "法人代表")
//    private String legalRepresentative;
//
//    @ApiModelProperty(value = "企业邮箱")
//    @ExcelProperty(value = "企业邮箱")
//    private String companyMail;
//
//    @ApiModelProperty(value = "企业纳税类型")
//    @ExcelProperty(value = "企业纳税类型", converter = DictConverter.class)
//    @DictField(dictType = DictConstant.COMPANY_TAXES_TYPE)
//    private Integer companyTaxesType;
//
//    @ApiModelProperty(value = "企业经营状态")
//    @ExcelProperty(value = "企业经营状态", converter = DictConverter.class)
//    @DictField(dictType = DictConstant.COMPANY_MANAGEMENT_STATUS)
//    private Integer companyManagementStatus;
//
//    @ApiModelProperty(value = "地址")
//    @ExcelProperty(value = "地址")
//    private String address;
//
//    @ApiModelProperty(value = "注册资本")
//    @ExcelProperty(value = "注册资本")
//    private Integer registeredCapital;
//
//    @ApiModelProperty(value = "营业期限")
//    @ExcelProperty(value = "营业期限")
//    private Integer businessTerm;

}
