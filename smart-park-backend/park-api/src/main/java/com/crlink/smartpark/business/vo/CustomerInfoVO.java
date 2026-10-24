package com.crlink.smartpark.business.vo;

import com.crlink.smartpark.business.dto.BusinessContacts;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CustomerInfoVO {
    private String id;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "统一社会信用代码")
    private String code;

    @ApiModelProperty(value = "客户标签")
    private String customerTagId;

    @ApiModelProperty(value = "客户标签名")
    private String customerTagName;

    @ApiModelProperty(value = "客户类型 0：物业公司，1：普通企业")
    private Integer type;

    @ApiModelProperty(value = "客户状态,1，已成交，2未成交，3历史成交")
    private Integer status;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactsTel;

    @ApiModelProperty(value = "联系人邮箱")
    private String contactsMail;

    @ApiModelProperty(value = "联系人部门")
    private String contactsDepartment;

    @ApiModelProperty(value = "联系人职位")
    private String contactsPosition;

    @ApiModelProperty(value = "跟进人")
    private String employeeId;
    @ApiModelProperty(value = "跟进人名称")
    @Id2objectField(code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    private String employeeName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "行业领域")
    private Integer industryField;

    @ApiModelProperty(value = "企业规模(人数)")
    private Integer companyScale;

    @ApiModelProperty(value = "法人代表")
    private String legalRepresentative;

    @ApiModelProperty(value = "企业邮箱")
    private String companyMail;

    @ApiModelProperty(value = "企业纳税类型")
    private Integer companyTaxesType;

    @ApiModelProperty(value = "企业经营状态")
    private Integer companyManagementStatus;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "注册资本")
    private Integer registeredCapital;

    @ApiModelProperty(value = "营业期限")
    private Integer businessTerm;

    @ApiModelProperty(value = "业务联系人")
    private List<BusinessContacts> businessContactsList;
}
