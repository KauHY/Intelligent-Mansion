package com.crlink.smartpark.contract.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 租赁合同账单减免记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractBillRecordDTO响应对象", description = "RentContractBillRecordDTO")
public class RentContractBillRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "收费项名称")
    private String chargeName;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "减免金额（元）")
    private BigDecimal deductionAmount;

    private String createBy;

    @ApiModelProperty(value = "操作人员")
    @Id2objectField(idField = "createBy", code = Id2objCodeConstant.USER_NAME_CODE)
    private String createByName;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "附件")
    private List<DeductionFileDTO> fileList;

    @ApiModelProperty(value = "备注")
    private String remark;
}
