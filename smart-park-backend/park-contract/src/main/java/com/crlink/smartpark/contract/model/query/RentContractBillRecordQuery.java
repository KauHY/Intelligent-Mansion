package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;
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
import java.util.List;

/**
 * <p>
 * 租赁合同账单减免记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RentContractBillRecord查询对象", description="租赁合同账单减免记录")
public class RentContractBillRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "减免金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty(value = "附件key")
    private String file;

    @ApiModelProperty(value = "备注")
    private String remark;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
