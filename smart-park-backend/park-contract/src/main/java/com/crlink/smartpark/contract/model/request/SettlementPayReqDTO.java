package com.crlink.smartpark.contract.model.request;

import com.crlink.smartpark.contract.dto.MeterConsumptionLeaveDTO;
import com.crlink.smartpark.contract.dto.RentContractLeaveDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

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
@ApiModel(value = "SettlementPayReqDTO请求对象", description = "结算缴费")
public class SettlementPayReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "租赁合同计算费用返回数据")
    RentContractLeaveDTO rentContractLeaveDTO;

    @ApiModelProperty(value = "水电费用计算费用返回数据")
    MeterConsumptionLeaveDTO meterConsumptionLeaveDTO;

    @ApiModelProperty(value = "收款方式:1 线下银行转账")
    private Integer payType;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;

    @ApiModelProperty(value = "收款日期", required = true)
    @NotNull(message = "收款日期不能为空")
    private LocalDate checkoutDate;

    @ApiModelProperty(value = "合同费用类型: 3-房屋租赁合同，4-公寓合同、5-公寓合同；", required = true)
    private Integer contractType;
}
