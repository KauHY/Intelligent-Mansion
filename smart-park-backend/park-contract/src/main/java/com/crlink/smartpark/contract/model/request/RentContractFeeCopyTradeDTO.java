package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class RentContractFeeCopyTradeDTO implements Serializable {

    @ApiModelProperty(value = "费用类型 1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer contractType;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "应收金额(元)")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "已收金额(元)/实付金额（元）")
    private BigDecimal receivedAmount;


}
