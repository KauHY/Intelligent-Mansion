package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 入驻企业表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 统一社会信用代码
     */
    private String code;

    /**
     * 营业执照图片路径
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String licenses;
    /**
     * 企业类型
     * 0：物业企业，1：入驻企业
     */
    private Integer companyType;

    @ApiModelProperty(value = "行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他")
    private Integer industryField;

    @ApiModelProperty(value = "企业纳税类型:1-一般纳税人；2-小规模纳税人")
    private Integer companyTaxesType;

    @ApiModelProperty(value = "企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业")
    private Integer enterpriseSize;

    @ApiModelProperty(value = "附件路径，多个逗号隔开")
    private String atts;

    @ApiModelProperty(value = "企业税收")
    private BigDecimal corporateTax;

    private String remark;

    /**
     * 联系人
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String contacts;

    /**
     * 联系电话
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String tel;

    /**
     * 绑定房屋个数
     */
    private Integer houseCount;

    /**
     * 绑定园区ID
     */
    private String parkOrgId;

    /**
     * 地址
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String address;

    /**
     * 企业绑定访客客户端类型，0：无 1：小程序 2：超链接
     */
    private Integer visitorClientType;

    /**
     * 访客客户端小程序appId
     */
    private String visitorClientAppid;

    /**
     * 访客客户端H5链接地址
     */
    private String visitorClientUrl;

    /**
     * 访客企业信息的配置时间
     */
    private LocalDateTime visitorClientTime;

    /**
     * 访客客户端图片地址
     */
    private String visitorClientPicUrl;

    @ApiModelProperty(value = "数据来源：1-管理员录入 2-企业申请")
    private Integer source;

    @ApiModelProperty(value = "企业状态:0-已禁用,1-正常")
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除状态
     */
    private Boolean deleted;


}
