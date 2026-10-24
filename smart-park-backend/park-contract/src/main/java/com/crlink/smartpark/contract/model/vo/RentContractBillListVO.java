package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractBillDTO响应对象", description = "租赁合同账单")
public class RentContractBillListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "租赁合同费用id")
    private String rentContractFeeId;

    @ApiModelProperty(value = "园区组织id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

//    @ApiModelProperty(value = "所含收费项：1租金 2 履约保证金 3滞纳金")
//    private String feeType;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "入住人员id")
//    @Id2objectField(code = EMPLOYEE_NAME_CODE, targetFieldName = "employeeIdNames")
    private List<String> employeeIds;

    @ApiModelProperty(value = "入住人员")
    private List<String> employeeIdNames;

//    @ApiModelProperty(value = "面积（㎡）")
//    private Double totalArea;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;

    @ApiModelProperty(value = "数据来源 1合同生成")
    private Integer dataSource;

    @ApiModelProperty(value = "应收金额(元)")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "待收金额(元)")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "已收金额(元)")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "减免金额（元）")
    private BigDecimal deductionAmount;

    @ApiModelProperty(value = "收款状态：0未收，1已收")
    private Integer status;

    @ApiModelProperty(value = "收款日期 ")
    private LocalDate checkoutDate;

    @ApiModelProperty(value = "合同编号：押金抵扣费用使用")
    private String contractNo;

}
