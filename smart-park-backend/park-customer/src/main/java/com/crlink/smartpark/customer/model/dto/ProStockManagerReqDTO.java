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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 库存管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "StockInManager请求对象", description = "库存管理")
public class ProStockManagerReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", required = true)
    private String id;

    @ApiModelProperty(value = "所属园区", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "物品ID", required = true)
    @NotBlank(message = "物品ID不能为空")
    private String goodsId;

    @ApiModelProperty(value = "入库数量", required = true)
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量最小为1")
    private Integer stockInNum;

    @ApiModelProperty(value = "入库单价（元）", required = true)
    private BigDecimal stockInUnitPrice;

    @ApiModelProperty(value = "出入库方式(统计使用)：1.入库、2.出库、3.修改库存", required = true)
    @NotNull(message = "出入库方式不能为空")
    private Integer stockMode;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

}
