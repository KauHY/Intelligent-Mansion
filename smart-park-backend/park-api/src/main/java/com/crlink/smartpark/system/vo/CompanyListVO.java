package com.crlink.smartpark.system.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CompanyListVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "企业类型")
    private Integer companyType;

    //TODO 来源
    @ApiModelProperty(value = "数据来源：1-管理员录入 2-企业申请")
    private Integer source;

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

    @ApiModelProperty(value = "统一社会信用代码")
    private String code;

    private String licenses;

    @ApiModelProperty(value = "绑定房屋个数")
    private Integer houseCount;

    @ApiModelProperty(value = "绑定园区名")
    private String parkOrgName;

    private String parkOrgId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String tel;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "企业状态:0-已禁用,1-正常")
    private Integer status;

}
