package com.crlink.smartpark.contract.model.request;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.crlink.smartpark.contract.dto.Extension;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同操作记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractProcessRecordReqDTO对象", description="合同操作记录")
public class ContractProcessRecordReqDTO implements Serializable {

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
     * 上一节点流程no
     */
    @ApiModelProperty(value = "上一节点流程no")
    private String preProcessNo;

    /**
     * 合同状态
     */
    @ApiModelProperty(value = "合同状态")
    private Integer state;

    /**
     * 上一节点合同状态
     */
    @ApiModelProperty(value = "上一节点合同状态")
    private Integer preState;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "ids")
    private List<String> ids;

    private Extension extension;


    public ContractProcessRecordReqDTO(String contractId, Integer operate, String preProcessNo, String processNo, Integer preState, Integer state, String remark, Extension extension) {
        this.contractId = contractId;
        this.operate = operate;
        this.processNo = processNo;
        this.preProcessNo = preProcessNo;
        this.state = state;
        this.preState = preState;
        this.extension = extension;
        this.remark = remark;

        EntityUserUtil.setAllDynamic(this);
    }
}
