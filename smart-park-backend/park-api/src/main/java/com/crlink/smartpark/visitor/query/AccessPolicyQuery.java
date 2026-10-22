package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * 园区运营平台-人行管理-通行策略分页查询对象
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AccessPolicy分页查询对象", description="通行策略")
public class AccessPolicyQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;


    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String policyName;

    /**
     *园区ID
     */
    @ApiModelProperty(value = "园区ID" )
    private String parkOrgId;

    /**
     *企业ID
     */
    @ApiModelProperty(value = "企业ID" )
    private String companyId;

}
