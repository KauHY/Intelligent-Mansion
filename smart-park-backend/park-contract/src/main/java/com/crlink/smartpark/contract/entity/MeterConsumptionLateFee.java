package com.crlink.smartpark.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 水电滞纳金费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "con_meter_consumption_late_fee",autoResultMap = true)
@ApiModel(value="MeterConsumptionLateFee数据库对象", description="水电滞纳金费用")
public class MeterConsumptionLateFee implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "租赁账单id")
    private String rentBillId;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer contractType;

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

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "逾期金额")
    private BigDecimal dueAmount;

    @ApiModelProperty(value = "逾期天数")
    private Integer dueDays;

    @ApiModelProperty(value = "待收滞纳金")
    private BigDecimal receiveFine;

    @ApiModelProperty(value = "应收滞纳金")
    private BigDecimal receivableFine;

    @ApiModelProperty(value = "已收滞纳金")
    private BigDecimal overdueFine;

    @ApiModelProperty(value = "减免滞纳金")
    private BigDecimal reduceFine;

    @ApiModelProperty(value = "状态 1计算中 2已缴清 3已免除")
    private Integer status;

    @ApiModelProperty(value = "经办人")
    private String userId;

    @ApiModelProperty(value = "处理时间")
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
