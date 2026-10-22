package com.crlink.smartpark.contract.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 租赁合同费用
 * </p>
 * */
@Data
public class MeterLateBasic implements Serializable {

    @ApiModelProperty(value = "费用类型 1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer contractType;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "入驻企业id")
    private String companyId;

    @ApiModelProperty(value = "入驻企业名称")
    private String companyName;

    @ApiModelProperty(value = "收费项名称")
    private String chargeName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "所在账期")
    private String settledMonth;

}
