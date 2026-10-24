package com.crlink.smartpark.customer.entity;

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
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_pro_receive_manager")
@ApiModel(value="ProReceiveManager数据库对象", description="物品领用管理")
public class ProReceiveManager implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "库存id")
    private String stockId;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属公司")
    private String companyId;

    @ApiModelProperty(value = "申请单号")
    private String applyNo;

    @ApiModelProperty(value = "领用人")
    private String employeeId;

    @ApiModelProperty(value = "领用数量")
    private Integer receiveNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否需要归还 1是 0否")
    private Integer needReturn;

    @ApiModelProperty(value = "本次归还数量")
    private Integer returnNumCurrent;

    @ApiModelProperty(value = "已归还数量")
    private Integer returnNumTotal;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消")
    private Integer auditState;

    @ApiModelProperty(value = "出入库状态 1未出库 2已出库 3部分已入库 4全部已入库")
    private Integer stockState;

    @ApiModelProperty(value = "出库时间")
    private LocalDateTime stockOutTime;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime stockInTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;


}
