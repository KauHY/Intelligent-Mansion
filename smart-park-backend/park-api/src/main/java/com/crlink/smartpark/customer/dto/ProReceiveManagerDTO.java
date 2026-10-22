package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProReceiveManager响应对象", description = "物品领用管理")
public class ProReceiveManagerDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "领用人id")
    private String employeeId;

    @ApiModelProperty(value = "领用人名称")
    @Id2objectField(code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    private String employeeName;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属园区名称")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "所属公司")
    private String companyId;

    @ApiModelProperty(value = "所属公司名称")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "本次归还数量")
    private Integer returnNumCurrent;

    @ApiModelProperty(value = "申请单号")
    private String applyNo;

    @ApiModelProperty(value = "领用数量")
    private String receiveNum;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消")
    private Integer auditState;

    @ApiModelProperty(value = "出入库状态 1未出库 2已出库 3部分已入库 4全部已入库")
    private Integer stockState;

    @ApiModelProperty(value = "是否需要归还 1是 0否")
    private Integer needReturn;

    @ApiModelProperty(value = "已归还数量")
    private Integer returnNumTotal;

    @ApiModelProperty(value = "领用备注")
    private String remark;

    @ApiModelProperty(value = "归还备注")
    private String returnRemark;

    @ApiModelProperty(value = "出库时间")
    private LocalDateTime stockOutTime;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime stockInTime;


}
