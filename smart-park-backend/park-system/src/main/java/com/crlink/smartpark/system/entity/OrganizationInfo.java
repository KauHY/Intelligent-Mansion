package com.crlink.smartpark.system.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织机构扩展信息
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_organization_info")
public class OrganizationInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 对应organization表主键
     */
    private String orgId;

    /**
     * 上级公司ID 
     */
    private String parentId;

    /**
     * 公司名称
     */
    private String orgName;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 银行卡号
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 注册人手机
     */
    private String registerPersonPhoneTel;

    /**
     * 注册人
     */
    private String registerPersonName;

    /**
     * 注册资本
     */
    private String registerCapital;

    /**
     * 注册地址码
     */
    private String registerAddressCode;

    /**
     * 注册日期
     */
    private LocalDateTime registerDate;

    /**
     * 描述
     */
    private String description;

    /**
     * 公司安全允许
     */
    private String safeAllow;

    /**
     * 抬头
     */
    private String title;

    /**
     * 公司邮箱
     */
    private String email;

    /**
     * logo
     */
    private String logo;

    /**
     * icon,菜单图标
     */
    private String icon;

    /**
     * 背景图
     */
    private String bgImage;

    /**
     * 营业执照截止日期
     */
    private LocalDateTime businessLicenseEndDate;

    /**
     * 营业执照注册码
     */
    private String businessLicenseRegisterCode;

    /**
     * 商业号码
     */
    private String businessNumber;

    /**
     * 法人电话
     */
    private String legalPersonTel;

    /**
     * 公司法人
     */
    private String legalPersonName;

    /**
     * 法人身份证
     */
    private String legalPersonIdCard;

    /**
     * 资质编号
     */
    private String qualificationNumber;

    /**
     * 公司资质
     */
    private String qualification;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 社会号码
     */
    private String socialCode;

    /**
     * 版权
     */
    private String copyright;

    /**
     * 企业(系统)账号
     */
    private String sysAccount;

    /**
     * 订阅类型
     */
    private Integer subscribeType;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否删除：0否1是
     */
    private Integer deleted;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 修改者
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
