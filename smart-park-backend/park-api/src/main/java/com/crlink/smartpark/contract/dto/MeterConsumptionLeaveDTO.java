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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.BUILDING_HOUSE_NAME_CODE;

/**
 * <p>
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterConsumptionLeave响应对象", description = "租赁合同交易明细")
public class MeterConsumptionLeaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "结算日期")
    private LocalDate leaveDate;

    @ApiModelProperty(value = "房屋ID")
    @Id2objectField(code = BUILDING_HOUSE_NAME_CODE, targetFieldName = "houseName")
    private String houseId;

    @ApiModelProperty(value = "结算房间")
    private String houseName;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime createTime;

    private String createBy;

    @ApiModelProperty(value = "操作人")
    @Id2objectField(idField = "createBy", code = Id2objCodeConstant.USER_NAME_CODE)
    private String createByName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "费用明细")
    private List<ChargeDetail> chargeDetail;

    @ApiModelProperty(value = "总金额（元）")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "合同费用类型: 3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer contractType;

}
