package com.crlink.smartpark.system.dto.company;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CompanySaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "企业名称")
    @Length(max = 30, min = 1, message = "企业名称不能为空且在30个字符以内")
    private String name;

    @ApiModelProperty(value = "企业类型")
    @NotNull(message = "企业类型不能为空")
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

    @ApiModelProperty(value = "统一社会信用代码")
    @NotBlank(message = "统一社会信用代码不能为空")
//    @Pattern(regexp = Constants.REGEXP_ONLY_18_NUMBER, message = "统一社会信用代码格式为18位数字")
    @Length(max = 18, message = "统一社会信用代码不能超过18位")
    private String code;

    @ApiModelProperty(value = "营业执照")
    private String licenses;

    @ApiModelProperty(value = "联系人")
    @Length(max = 30, message = "联系人长度30字符以内")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @Pattern(regexp = Constants.REGEXP_ONLY_NUMBER, message = "联系电话格式应为纯数字")
    private String tel;

    @ApiModelProperty(value = "绑定园区ID")
    @NotBlank(message = "绑定园区ID不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "地址")
    @Length(max = 30, message = "地址长度30字符以内")
    private String address;

    @ApiModelProperty(value = "创建人")
    @JsonIgnore
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonIgnore
    private String updateBy;

}
