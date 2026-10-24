package com.crlink.smartpark.contract.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同操作记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractProcessRecordDTO对象", description="合同操作记录")
public class ContractProcessRecordDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "合同表主键")
    private String contractId;

    @ApiModelProperty(value = "操作：1-编辑；2-删除；3-作废；4-发起审核；5-变更；6-续约；7-退约；8-终止")
    private Integer operate;

    @ApiModelProperty(value = "流程no")
    private String processNo;

    @ApiModelProperty(value = "上一节点流程no")
    private String preProcessNo;

    @ApiModelProperty(value = "合同状态")
    private Integer state;

    @ApiModelProperty(value = "上一节点合同状态")
    private Integer preState;

    @ApiModelProperty(value = "备注")
    private String remark;

    private Extension extension;
}
