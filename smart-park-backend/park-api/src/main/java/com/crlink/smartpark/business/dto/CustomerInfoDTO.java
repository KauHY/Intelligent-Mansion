package com.crlink.smartpark.business.dto;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机管理—客户表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomerInfo对象", description = "商机管理—客户表")
public class CustomerInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称", required = true)
    @NotBlank(message = "名称不能为空！")
    @Length(max = 30, message = "客户名称不能超过30字")
    private String name;

    @ApiModelProperty(value = "园区id", required = true)
    @NotBlank(message = "园区不能为空！")
    private String parkOrgId;

    @ApiModelProperty(value = "统一社会信用代码", required = true)
    @NotBlank(message = "统一社会信用代码不能为空！")
    @Length(min = 18, max = 18)
    private String code;

    @ApiModelProperty(value = "客户类型（COMPANY_TYPE） 0物业公司、1其他、2科研院所、3事业单位、4机关单位、5科技型企业", required = true)
    @NotNull(message = "客户类型不能为空！")
    private Integer type;

    @ApiModelProperty(value = "客户状态,1，已成交，2未成交，3历史成交", required = true)
    @NotNull(message = "客户状态不能为空！")
    private Integer status;


    @ApiModelProperty(value = "联系人", required = true)
    @NotBlank(message = "联系人不能为空！")
    @Length(max = 30, message = "联系人不能超过30字")
    private String contacts;

    @ApiModelProperty(value = "联系电话", required = true)
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    private String contactsTel;

    @ApiModelProperty(value = "联系人邮箱")
    private String contactsMail;

    @ApiModelProperty(value = "联系人部门")
    @Length(max = 30, message = "联系人部门不能超过30字")
    private String contactsDepartment;

    @ApiModelProperty(value = "联系人职位")
    @Length(max = 30, message = "联系人职位不能超过30字")
    private String contactsPosition;

    @ApiModelProperty(value = "客户标签")
    private String customerTagId;

    @ApiModelProperty(value = "跟进人", required = true)
    @NotBlank(message = "跟进人不能为空！")
    private String employeeId;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String remark;

    @ApiModelProperty(value = "行业领域")
    private Integer industryField;

    @ApiModelProperty(value = "企业规模(人数)")
    private Integer companyScale;

    @ApiModelProperty(value = "法人代表")
    @Length(max = 30, message = "法人代表不能超过30字")
    private String legalRepresentative;

    @ApiModelProperty(value = "企业邮箱")
    private String companyMail;

    @ApiModelProperty(value = "企业纳税类型")
    private Integer companyTaxesType;

    @ApiModelProperty(value = "企业经营状态")
    private Integer companyManagementStatus;

    @ApiModelProperty(value = "地址")
    @Length(max = 30, message = "地址不能超过30字")
    private String address;

    @ApiModelProperty(value = "注册资本")
    private Integer registeredCapital;

    @ApiModelProperty(value = "营业期限")
    private Integer businessTerm;

    @ApiModelProperty(value = "业务联系人")
    @Size(max = 5, message = "业务联系人不能超过5个！")
    private List<BusinessContacts> businessContactsList;

}
