package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "Extension对象", description = "合同")
public class Extension {
    //入园协议
    @ApiModelProperty(value = "乙方联系人身份证号码")
    private String contactIdCardNoB;
    //招引合同
    @ApiModelProperty(value = "年纳税额（万元）")
    private BigDecimal annualTaxTenThousand;
    @ApiModelProperty(value = "经营范围")
    @Length(max = 200)
    private String businessScope;

    //租赁合同\物业合同
    @ApiModelProperty(value = "装修开始日期")
    private String renovationStartDate;
    @ApiModelProperty(value = "装修结束日期")
    private String renovationEndDate;
    @ApiModelProperty(value = "租期（年）")
    private Integer leaseTermYear;
    @ApiModelProperty(value = "租金（元）")
    private BigDecimal rentAmount;
    @ApiModelProperty(value = "物业费（元）")
    private BigDecimal propertyCosts;
    @ApiModelProperty(value = "履约保证金（元）")
    private BigDecimal performanceBond;

    //公寓合同
    @ApiModelProperty(value = "租期（天）")
    private Integer leaseTermDay;
    @ApiModelProperty(value = "客房数")
    private Integer numberOfRooms;
    @ApiModelProperty(value = "单间数")
    private Integer numberOfSingleRooms;
    @ApiModelProperty(value = "标间数")
    private Integer numberOfTwinRooms;
    @ApiModelProperty(value = "套间数")
    private Integer numberOfSuite;
    @ApiModelProperty(value = "押金")
    private BigDecimal deposit;


    @ApiModelProperty(value = "原合同编号")
    private String originContractNo;
    @ApiModelProperty(value = "原合同id")
    private String originContractId;
    @ApiModelProperty(value = "变更说明")
    private String changeExplanation;
    @ApiModelProperty(value = "变更类型")
    private Integer changeType;


    @ApiModelProperty(value = "审核结果:1-通过；2-不通过")
    private Integer auditResult;
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;
}