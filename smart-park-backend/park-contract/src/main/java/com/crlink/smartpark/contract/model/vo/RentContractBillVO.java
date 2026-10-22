package com.crlink.smartpark.contract.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.contract.dto.CheckoutRecord;
import com.crlink.smartpark.contract.dto.FeeDetail;
import com.crlink.smartpark.contract.dto.LateFeeDetail;
import com.crlink.smartpark.contract.dto.RentContractBillRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.EMPLOYEE_NAME_CODE;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractBillVO响应对象", description = "租赁合同账单详情")
public class RentContractBillVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区组织id")
    private String parkOrgId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;

    @ApiModelProperty(value = "数据来源 1合同生成")
    private Integer dataSource;

    @ApiModelProperty(value = "收款状态：0未收，1已收")
    private Integer status;

    @ApiModelProperty(value = "入住人员id")
    @Id2objectField(code = EMPLOYEE_NAME_CODE, targetFieldName = "employeeIdNames")
    private List<String> employeeIds;

    @ApiModelProperty(value = "入住人员")
    private List<String> employeeIdNames;

    @ApiModelProperty(value = "待收金额(元)")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "凭证（企业端上传的文件key）")
    private String companyFiles;

    @ApiModelProperty(value = "费用明细")
    private List<FeeDetail> feeDetailList;

    @ApiModelProperty(value = "减免记录")
    private List<RentContractBillRecordDTO> deductionRecordList;

    @ApiModelProperty(value = "滞纳金明细")
    private List<LateFeeDetail> lateFeeDetailList;

    @ApiModelProperty(value = "收款记录")
    private List<CheckoutRecord> checkoutRecordList;
}
