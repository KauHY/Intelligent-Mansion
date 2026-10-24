package com.crlink.smartpark.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.core.mphandler.HouseMessageListTypeHandler;
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

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "con_rent_contract_bill", autoResultMap = true)
@ApiModel(value = "RentContractBill数据库对象", description = "租赁合同账单")
public class RentContractBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "费用方案id")
    private String contractBillingPlanId;

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "租赁合同费用id")
    private String rentContractFeeId;

    @ApiModelProperty(value = "费用类型 1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
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

    @ApiModelProperty(value = "收费项费用类型")
    private Integer feeType;

    @ApiModelProperty(value = "所含收费项")
    private String chargeName;

    @ApiModelProperty(value = "房源名称")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> houseName;

    @ApiModelProperty(value = "房源对象")
    @TableField(typeHandler = HouseMessageListTypeHandler.class)
    private List<HouseMessage> houseMessages;

    @ApiModelProperty(value = "公寓费用使用")
    private String houseId;

    @ApiModelProperty(value = "入住人员:公寓人员管理中房屋关联的所有员工")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> employeeIds;

    @ApiModelProperty(value = "计费周期 开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "计费周期 结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

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

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;

    @ApiModelProperty(value = "收款状态：0未收，1已收")
    private Integer status;

    @ApiModelProperty(value = "收款日期 ")
    private LocalDate checkoutDate;

    @ApiModelProperty(value = "收款类型：1正常收款，2离场结算")
    private Integer checkoutType;

    @ApiModelProperty(value = "支付方式:1线下银行转账")
    private Integer payType;

    @ApiModelProperty(value = "面积（㎡）")
    private Double totalArea;

    @ApiModelProperty(value = "状态 1计算中 2已缴清 3已免除")
    private Integer lateFeeStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "企业端上传凭证")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String companyFiles;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    private Boolean deleted;


}
