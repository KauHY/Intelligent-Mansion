package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractLeave请求对象", description = "租赁合同交易明细")
public class RentContractLeaveReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "园区组织ID", required = true)
    @NotBlank(message = "园区不能为空！")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称", required = true)
    private String parkOrgName;

    @ApiModelProperty(value = "公司ID", required = true)
    @NotBlank(message = "公司不能为空！")
    private String companyId;

    @ApiModelProperty(value = "公司名称", required = true)
    private String companyName;

    @ApiModelProperty(value = "合同id", required = true)
    @NotBlank(message = "合同id不能为空！")
    private String contractId;

    @ApiModelProperty(value = "合同编号", required = true)
    private String contractNo;

    @ApiModelProperty(value = "结算日期", required = true)
    @NotNull(message = "结算日期不能为空！")
    private LocalDate leaveDate;

    @ApiModelProperty(value = "合同费用类型: 3-房屋租赁合同；4-物业合同；5-公寓合同；", required = true)
    @NotNull(message = "合同费用类型不能为空")
    private Integer contractType;
}
