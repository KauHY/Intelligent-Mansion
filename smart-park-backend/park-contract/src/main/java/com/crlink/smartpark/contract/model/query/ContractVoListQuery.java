package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName: ContractVOListQuery
 * @Package com.crlink.smartpark.contract.model.query
 * @Description: 合同房源状态查询
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ContractVOListQuery对象", description = "合同")
public class ContractVoListQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织Id
     */
    @ApiModelProperty(value = "组织Id")
    private String orgId;

    @ApiModelProperty(value = "园区ID")
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    @ApiModelProperty(value = "房屋状态 HOUSE_STATUS")
    private Integer houseStatus;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同到期状态 1-合同30天（含）内到期,0、空-全部")
    private Integer endType;

    /**
     * 合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；
     */
    @ApiModelProperty(value = "合同类型：CONTRACT_TYPE 1-房屋租赁合同；2-物业合同；3-公寓合同")
    private Integer type;


    /**
     * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
     */
    @ApiModelProperty(value = "合同状态：CONTRACT_STATE 1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期")
    private Integer state;

    @JsonIgnore
    Boolean fromApartmentPeople;

}
