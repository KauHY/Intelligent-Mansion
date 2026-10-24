package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractBill查询对象", description = "租赁合同账单")
public class RentContractBillQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同费用类型: 3-房屋租赁合同；4-物业合同；5-公寓合同；", required = true)
    @NotNull(message = "合同费用类型不能为空")
    private Integer contractType;

    @ApiModelProperty(value = "园区组织id")
    private String parkOrgId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "应收日期:开始日期")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate dueDateStart;

    @ApiModelProperty(value = "应收日期:结束日期")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate dueDateEnd;

    @ApiModelProperty(value = "最晚缴纳日期:开始日期")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate latestPayDateStart;

    @ApiModelProperty(value = "最晚缴纳日期:结束日期")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate latestPayDateEnd;

    @ApiModelProperty(value = "收款状态：0未收，1已收")
    private Integer status;

    @ApiModelProperty(value = "合同编号：押金抵扣费用使用")
    private String contractNo;

}
