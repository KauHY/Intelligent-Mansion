package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeterConsumptionBill查询对象", description="租赁合同账单")
public class MeterConsumptionBillQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "费用方案id")
    private String contractBillingPlanId;

    @ApiModelProperty(value = "租赁合同费用id")
    private String rentContractFeeId;

    @ApiModelProperty(value = "费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer contractType;

    @ApiModelProperty(value = "园区组织id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "所在账期")
    private String accountPeriod;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "房源ids集合")
    private String houseIds;

    @ApiModelProperty(value = "公寓人员中房屋所关联的员工")
    private String employeeIds;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "数据来源 1合同生成")
    private Integer dataSource;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "结算时间")
    private LocalDateTime settledTime;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "待收金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "已收金额")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "减免金额（元）")
    private BigDecimal deductionAmount;

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;

    @ApiModelProperty(value = "收款状态：0未收，1已收")
    private Integer status;

    @ApiModelProperty(value = "收款日期 ")
    private LocalDate checkoutDate;

    @ApiModelProperty(value = "支付方式:1线下银行转账")
    private Integer payType;

    @ApiModelProperty(value = "滞纳金免除状态 1是 0否")
    private Integer lateFeeStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "费用明细")
    private String chargeDetail;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除状态，0未删除，1已删除")
    private Boolean deleted;


}
