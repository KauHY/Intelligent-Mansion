package com.crlink.smartpark.system.dto.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 组织机构扩展信息
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrganizationInfo对象", description = "组织机构扩展信息")
public class OrganizationInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "对应organization表主键")
    private String orgId;

    @ApiModelProperty(value = "上级公司ID ")
    private String parentId;

    @ApiModelProperty(value = "公司名称")
    private String orgName;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "公司地址")
    private String address;

    @ApiModelProperty(value = "银行卡号")
    private String bankCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "注册人手机")
    private String registerPersonPhoneTel;

    @ApiModelProperty(value = "注册人")
    private String registerPersonName;

    @ApiModelProperty(value = "注册资本")
    private String registerCapital;

    @ApiModelProperty(value = "注册地址码")
    private String registerAddressCode;

    @ApiModelProperty(value = "注册日期")
    private LocalDateTime registerDate;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "公司安全允许")
    private String safeAllow;

    @ApiModelProperty(value = "抬头")
    private String title;

    @ApiModelProperty(value = "公司邮箱")
    private String email;

    @ApiModelProperty(value = "logo")
    private String logo;

    @ApiModelProperty(value = "icon,菜单图标")
    private String icon;

    @ApiModelProperty(value = "背景图")
    private String bgImage;

    @ApiModelProperty(value = "营业执照截止日期")
    private LocalDateTime businessLicenseEndDate;

    @ApiModelProperty(value = "营业执照注册码")
    private String businessLicenseRegisterCode;

    @ApiModelProperty(value = "商业号码")
    private String businessNumber;

    @ApiModelProperty(value = "法人电话")
    private String legalPersonTel;

    @ApiModelProperty(value = "公司法人")
    private String legalPersonName;

    @ApiModelProperty(value = "法人身份证")
    private String legalPersonIdCard;

    @ApiModelProperty(value = "资质编号")
    private String qualificationNumber;

    @ApiModelProperty(value = "公司资质")
    private String qualification;

    @ApiModelProperty(value = "邮政编码")
    private String postalCode;

    @ApiModelProperty(value = "社会号码")
    private String socialCode;

    @ApiModelProperty(value = "版权")
    private String copyright;

    @ApiModelProperty(value = "企业(系统)账号")
    private String sysAccount;

    @ApiModelProperty(value = "订阅类型")
    private Integer subscribeType;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "是否删除：0否1是")
    private Integer deleted;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
