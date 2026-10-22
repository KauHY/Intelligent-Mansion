package com.crlink.smartpark.contract.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 租赁合同费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RentContractFee响应对象", description="租赁合同费用")
public class RentContractFeeDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "费用类型 1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer contractType;

    @ApiModelProperty(value = "费用方案id")
    private String contractBillingPlanId;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "收费项")
    private Integer feeType;

    @ApiModelProperty(value = "收费项名称")
    private String chargeName;

    @ApiModelProperty(value = "建筑面积（㎡）")
    private Double totalArea;

    @ApiModelProperty(value = "入驻企业id")
    private String companyId;

    @ApiModelProperty(value = "入驻企业名称")
    private String companyName;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

    @ApiModelProperty(value = "房源对象")
    private List<HouseMessage> houseMessages;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;

    @ApiModelProperty(value = "数据来源 1合同生成")
    private Integer dataSource;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "待收金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "已收金额")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "减免金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty(value = "出账状态 1待出账 2已出账 3已废除")
    private Integer billingStatus;

    @ApiModelProperty(value = "费用状态 0未收 1已收")
    private Integer feeStatus;

    @ApiModelProperty(value = "押金状态 1未缴清 2已缴清 3已结清")
    private Integer cashStatus;

    @ApiModelProperty(value = "是否抵扣 1是 0否")
    private Integer deduction;

    @ApiModelProperty(value = "抵扣内容")
    private List<DeductionMessage> deductionMsg;

    @ApiModelProperty(value = "已抵扣金额")
    private BigDecimal offsetAmount;

    @ApiModelProperty(value = "已退款金额")
    private BigDecimal refundedAmount;

    @ApiModelProperty(value = "退租日期")
    private LocalDate leaseDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "经办人")
    private String userId;

    @ApiModelProperty(value = "经办人名称")
    @Id2objectField(code = USER_NAME_CODE)
    private String userName;

    @ApiModelProperty(value = "办理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
