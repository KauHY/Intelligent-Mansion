package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterConsumptionTradeQuery查询对象", description = "租赁合同交易明细")
public class MeterConsumptionTradeQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型:3-房屋租赁合同；4-物业合同；5-公寓合同；", required = true)
    @NotNull(message = "合同费用类型不能为空")
    private Integer contractType;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "支付时间:开始")
    private LocalDateTime payTimeStart;

    @ApiModelProperty(value = "支付时间:结束")
    private LocalDateTime payTimeEnd;

    @ApiModelProperty(value = "类型：1付款订单，2退款订单")
    private Integer type;


}
