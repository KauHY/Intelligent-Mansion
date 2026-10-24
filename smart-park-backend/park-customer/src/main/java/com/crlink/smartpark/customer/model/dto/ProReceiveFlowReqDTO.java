package com.crlink.smartpark.customer.model.dto;

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
 * 物品领用流程
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceiveFlow请求对象", description="物品领用流程")
public class ProReceiveFlowReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id", required = true)
      private String id;

    @ApiModelProperty(value = "领用id", required = true)
    private String receiveId;

    @ApiModelProperty(value = "审核人", required = true)
    private String employeeId;

    @ApiModelProperty(value = "审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消", required = true)
    private Integer auditState;

    @ApiModelProperty(value = "审核意见", required = true)
    private String auditMsg;

    @ApiModelProperty(value = "审核时间", required = true)
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "备注", required = true)
    private String auditRemark;

    @ApiModelProperty(value = "出入库状态 1未出库 2已出库 3部分已入库 4全部已入库", required = true)
    private Integer stockState;

    @ApiModelProperty(value = "出库时间", required = true)
    private LocalDateTime stockOutTime;

    @ApiModelProperty(value = "入库时间", required = true)
    private LocalDateTime stockInTime;

    @ApiModelProperty(value = "本次归还数量", required = true)
    private Integer returnNumCurrent;

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
