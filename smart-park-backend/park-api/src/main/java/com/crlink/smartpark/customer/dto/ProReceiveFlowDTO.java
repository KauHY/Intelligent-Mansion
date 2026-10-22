package com.crlink.smartpark.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物品领用流程
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceiveFlow响应对象", description="物品领用流程")
public class ProReceiveFlowDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private String id;

    @ApiModelProperty(value = "领用id")
    private String receiveId;

    @ApiModelProperty(value = "审核人")
    private String auditUserId;

    @ApiModelProperty(value = "审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消")
    private Integer auditState;

    @ApiModelProperty(value = "审核意见")
    private String auditMsg;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "备注")
    private String auditRemark;

    @ApiModelProperty(value = "出入库状态 1未出库 2已出库 3部分已入库 4全部已入库")
    private Integer stockState;

    @ApiModelProperty(value = "出库时间")
    private LocalDateTime stockOutTime;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime stockInTime;

    @ApiModelProperty(value = "本次归还数量")
    private Integer returnNumCurrent;

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
