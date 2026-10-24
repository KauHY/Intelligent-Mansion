package com.crlink.smartpark.customer.controller.request.query;

import com.crlink.smartpark.BaseQuery;
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
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceiveManager查询对象", description="物品领用管理")
public class ProReceiveManagerQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "领用人id")
    private String employeeId;

    @ApiModelProperty(value = "领用人名称")
    private String employeeName;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消")
    private Integer auditState;

    @ApiModelProperty(value = "出入库状态 1未出库 2已出库 3部分已入库 4全部已入库")
    private Integer stockState;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "是否需要归还 1是 0否")
    private Integer needReturn;

    @ApiModelProperty(value = "申请时间start")
    private LocalDateTime applyTimeStart;

    @ApiModelProperty(value = "申请时间end")
    private LocalDateTime applyTimeEnd;


}
