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
 * 合同计费信息
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractBillingPlanQuery对象", description="合同计费信息")
public class ContractBillingPlanQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    /**
     * 合同表主键
     */
    @ApiModelProperty(value = "合同表主键")
    private String contractId;

    /**
     * 收费方案Id
     */
    @ApiModelProperty(value = "收费方案Id")
    private String feeBillingSchemeId;


}
