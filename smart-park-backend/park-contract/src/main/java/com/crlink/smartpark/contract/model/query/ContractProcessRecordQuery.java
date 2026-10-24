package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;

import java.io.Serializable;
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
@ApiModel(value="ContractProcessRecordQuery对象", description="合同操作记录")
public class ContractProcessRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    /**
     * 合同表主键
     */
    @ApiModelProperty(value = "合同表主键")
    private String contractId;

    /**
     * 操作：1-编辑；2-删除；3-作废；4-发起审核；5-变更；6-续约；7-退约；8-终止
     */
    @ApiModelProperty(value = "操作：1-编辑；2-删除；3-作废；4-发起审核；5-变更；6-续约；7-退约；8-终止")
    private Integer operate;

    /**
     * 流程no
     */
    @ApiModelProperty(value = "流程no")
    private String processNo;

    /**
     * 合同状态
     */
    @ApiModelProperty(value = "合同状态")
    private Integer state;

}
