package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CompanyPortBillQuery", description = "租赁合同账单")
public class CompanyPortBillQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型:租赁费用（RENT_BILL_FEE_TYPE_COMPANY） 3-房屋租赁合同费用，4-物业费用，5-公寓合同费用。" +
            "水电费用（METER_BILL_FEE_TYPE_COMPANY）3-企业水电费用，5-公寓水电费用", required = true)
    private Integer type;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "收费项")
    private String chargeName;

}
