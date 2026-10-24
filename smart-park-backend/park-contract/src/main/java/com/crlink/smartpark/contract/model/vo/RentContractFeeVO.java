package com.crlink.smartpark.contract.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectNestingField;
import com.crlink.smartpark.contract.dto.RentContractBillRecordDTO;
import com.crlink.smartpark.contract.dto.RentContractFeeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractFeeVO响应对象", description = "租赁合同账单详情")
public class RentContractFeeVO implements Serializable {

    @ApiModelProperty(value = "费用明细")
    @Id2objectNestingField
    private RentContractFeeDTO rentContractFeeDTO;

    @ApiModelProperty(value = "减免记录")
    @Id2objectNestingField
    private List<RentContractBillRecordDTO> deductionRecordList;

}
