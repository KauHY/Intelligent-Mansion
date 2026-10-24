package com.crlink.smartpark.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 库存出入库记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProStockRecord响应对象", description="库存出入库记录")
public class ProStockRecordDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属园区名称")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "物品ID")
    private String goodsId;

    @ApiModelProperty(value = "物品类别")
    private Integer goodsType;

    @ApiModelProperty(value = "物品编码")
    private String goodsNo;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "单位")
    private String unit;


    @ApiModelProperty(value = "出入库单号")
    private String recordNo;

    @ApiModelProperty(value = "入库数量")
    private String stockInNum;

    @ApiModelProperty(value = "入库单价（元）")
    private String stockInUnitPrice;

    @ApiModelProperty(value = "入库操作后库存")
    private String currentNum;

    @ApiModelProperty(value = "入库操作后均价（元）")
    private String currentAveragePrice;

    @ApiModelProperty(value = "出入库方式（业务使用）：1.手动入库、2.归还入库、3.导入入库、4.手动出库、5.领用出库、6.修改库存")
    private Integer stockType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "出入库方式(统计使用)：1.入库、2.出库、3.修改库存")
    private Integer stockMode;


    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "操作人")
    @Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    private String createByName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;




}
