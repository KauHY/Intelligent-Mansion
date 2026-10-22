package com.crlink.smartpark.system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业信息
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CompanyInformation响应对象", description="企业信息")
public class CompanyInformationDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "统一社会信用代码")
    private String code;

    @ApiModelProperty(value = "营业执照图片路径")
    private String licenses;

    @ApiModelProperty(value = "企业类型:0-物业公司；1-普通公司；2-科研院所；3-事业单位；4-机关单位；5-科技型企业")
    private Integer companyType;

    @ApiModelProperty(value = "行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他")
    private Integer industryField;

    @ApiModelProperty(value = "企业纳税类型:1-一般纳税人；2-小规模纳税人")
    private Integer companyTaxesType;

    @ApiModelProperty(value = "企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业")
    private Integer enterpriseSize;

    @ApiModelProperty(value = "附件路径，多个逗号隔开")
    private String atts;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "注册地址")
    private String address;

    @ApiModelProperty(value = "企业端用户id")
    private String businessUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
