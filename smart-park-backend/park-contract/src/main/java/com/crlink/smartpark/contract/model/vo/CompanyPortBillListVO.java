package com.crlink.smartpark.contract.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.BUILDING_HOUSE_NAME_CODE;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CompanyPortBillListVO响应对象", description = "租赁合同账单")
public class CompanyPortBillListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "房源名称")
    @Id2objectField(code = BUILDING_HOUSE_NAME_CODE, targetFieldName = "houseName")
    private List<String> houseIds;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;

    @ApiModelProperty(value = "待收金额(元)")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "企业端上传凭证")
    private String companyFiles;

//    @ApiModelProperty(value = "合同编号：押金抵扣费用使用")
//    private String contractNo;

}
