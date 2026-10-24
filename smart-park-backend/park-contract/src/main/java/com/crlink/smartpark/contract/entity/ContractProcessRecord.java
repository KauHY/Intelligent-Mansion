package com.crlink.smartpark.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.crlink.smartpark.contract.dto.Extension;
import com.crlink.smartpark.core.mphandler.FastjsonFilterNullTypeHandler;
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
@TableName(value = "con_contract_process_record", autoResultMap = true)
@ApiModel(value="ContractProcessRecord对象", description="合同操作记录")
public class ContractProcessRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 合同表主键
     */
    private String contractId;

    /**
     * 操作：1-编辑；2-删除；3-作废；4-发起审核；5-变更；6-续约；7-退约；8-终止
     */
    private Integer operate;

    /**
     * 流程no
     */
    private String processNo;

    /**
     * 上一节点流程no
     */
    private String preProcessNo;

    /**
     * 合同状态
     */
    private Integer state;

    /**
     * 上一节点合同状态
     */
    private Integer preState;

    /**
     * 备注
     */
    private String remark;

    @TableField(typeHandler = FastjsonFilterNullTypeHandler.class)
    private Extension extension;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除状态
     */
    private Boolean deleted;

}
