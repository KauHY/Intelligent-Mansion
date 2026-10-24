package com.crlink.smartpark.customer.model.dto;

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
@ApiModel(value = "ProStockRecord请求对象", description = "库存出入库记录")
public class ProStockRecordReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", required = true)
    private String id;

    @ApiModelProperty(value = "所属园区", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "物品ID", required = true)
    private String goodsId;

    @ApiModelProperty(value = "出入库单号", required = true)
    private String recordNo;

    @ApiModelProperty(value = "入库数量", required = true)
    private Integer stockInNum;

    @ApiModelProperty(value = "入库单价（元）", required = true)
    private BigDecimal stockInUnitPrice;

    @ApiModelProperty(value = "入库操作后库存", required = true)
    private Integer currentNum;

    @ApiModelProperty(value = "入库操作后均价（元）", required = true)
    private BigDecimal currentAveragePrice;

    @ApiModelProperty(value = "出入库方式（业务使用）：1.手动入库、2.归还入库、3.导入入库、4.手动出库、5.领用出库、6.修改库存", required = true)
    private Integer stockType;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    @ApiModelProperty(value = "出入库方式(统计使用)：1.入库、2.出库、3.修改库存", required = true)
    private Integer stockMode;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否", required = true)
    private Boolean deleted;

}
