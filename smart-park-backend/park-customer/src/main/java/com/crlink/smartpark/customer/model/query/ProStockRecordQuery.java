package com.crlink.smartpark.customer.controller.request.query;

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
 * 库存出入库记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProStockRecord查询对象", description = "库存出入库记录")
public class ProStockRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "物品ID")
    private String goodsName;

    @ApiModelProperty(value = "出入库方式（业务使用）：1.手动入库、2.归还入库、3.手动出库、4.领用出库、5.修改库存")
    private Integer stockType;

    @ApiModelProperty(value = "出入库开始时间")
    private LocalDateTime stockInTimeStart;

    @ApiModelProperty(value = "出入库结束时间")
    private LocalDateTime stockInTimeEnd;

}
