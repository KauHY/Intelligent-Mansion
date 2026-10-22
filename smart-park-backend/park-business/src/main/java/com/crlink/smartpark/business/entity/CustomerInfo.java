package com.crlink.smartpark.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.crlink.smartpark.business.dto.BusinessContacts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商机管理—客户表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "bus_customer_info", autoResultMap = true)
public class CustomerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 园区id
     */
    private String parkOrgId;

    /**
     * 统一社会信用代码
     */
    private String code;

    /**
     * 客户类型（COMPANY_TYPE） 0物业公司、1其他、2科研院所、3事业单位、4机关单位、5科技型企业
     */
    private Integer type;

    /**
     * 客户状态,1，已成交，2未成交，3历史成交
     */
    private Integer status;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactsTel;

    /**
     * 联系人邮箱
     */
    private String contactsMail;

    /**
     * 联系人部门
     */
    private String contactsDepartment;

    /**
     * 联系人职位
     */
    private String contactsPosition;

    /**
     * 客户标签
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String customerTagId;

    /**
     * 跟进人
     */
    private String employeeId;


    /**
     * 备注
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 业务联系人
     */
    @TableField(typeHandler = JacksonTypeHandler.class, updateStrategy = FieldStrategy.IGNORED)
    private List<BusinessContacts> businessContactsList;


    /**
     * 行业领域
     */
    private Integer industryField;

    /**
     * 企业规模(人数)
     */
    private Integer companyScale;

    /**
     * 法人代表
     */
    private String legalRepresentative;

    /**
     * 企业邮箱
     */
    private String companyMail;

    /**
     * 企业纳税类型
     */
    private Integer companyTaxesType;

    /**
     * 企业经营状态
     */
    private Integer companyManagementStatus;

    /**
     * 地址
     */
    private String address;

    /**
     * 注册资本
     */
    private Integer registeredCapital;

    /**
     * 营业期限
     */
    private Integer businessTerm;

    /**
     * 删除状态，0未删除，1已删除
     */
    private Boolean deleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
