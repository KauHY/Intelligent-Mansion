package com.crlink.smartpark.contract.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractLeaveListVO响应对象", description = "离场结算记录")
public class RentContractLeaveListVO implements Serializable {

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

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    @ApiModelProperty(value = "结算房间")
    private String houseName;

    @ApiModelProperty(value = "（RENT_FEE_LEAVE_TYPE）计费类型: 1离场结算")
    private Integer type;

    @ApiModelProperty(value = "实收总金额（元）")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime createTime;

    private String createBy;

    @ApiModelProperty(value = "操作人")
//    @Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    private String createByName;

}
