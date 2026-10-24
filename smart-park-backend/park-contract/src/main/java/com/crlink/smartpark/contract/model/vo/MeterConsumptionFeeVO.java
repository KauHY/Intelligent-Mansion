package com.crlink.smartpark.contract.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectNestingField;
import com.crlink.smartpark.contract.dto.MeterConsumptionBillRecordDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionFeeDTO;
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
 * 水电费用详情
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterConsumptionFeeVO响应对象", description = "水电费用详情")
public class MeterConsumptionFeeVO implements Serializable {

    @ApiModelProperty(value = "费用明细")
    private MeterConsumptionFeeDTO meterConsumptionFeeDTO;

    @ApiModelProperty(value = "减免记录")
    @Id2objectNestingField
    private List<MeterConsumptionBillRecordDTO> deductionRecordList;

}
