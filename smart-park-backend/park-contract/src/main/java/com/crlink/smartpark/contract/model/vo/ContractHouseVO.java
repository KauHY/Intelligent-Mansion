package com.crlink.smartpark.contract.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: ContractHouseVO
 * @Package com.crlink.smartpark.contract.model.vo
 * @Description: 房屋合同视图
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ContractHouseVO对象", description = "合同")
public class ContractHouseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;
    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "楼层房号主键")
    private String houseId;

    @ApiModelProperty(value = "楼层房号")
//    @Id2objectField(code = Id2objCodeConstant.HOUSE_NAME_CODE)
    private String houseName;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @Id2objectField(code = Id2objCodeConstant.BUILDING_NAME_CODE)
    @ApiModelProperty(value = "楼栋名")

    private String buildingName;


    @ApiModelProperty(value = "所在楼层")
    private Integer floor;


    @ApiModelProperty(value = "房屋状态 HOUSE_STATUS：自用，待租， 已租，代售，已售")
    private Integer houseStatus;

    @ApiModelProperty(value = "房屋功能：HOUSE_FUNCTION ")
    private Integer houseFunction;


    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;


    @ApiModelProperty(value = "合同类型 CONTRACT_TYPE：1-房屋租赁合同；2-物业合同；3-公寓合同")
    private Integer type;

    @ApiModelProperty(value = "合同状态 CONTRACT_STATE：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期")
    private Integer state;


//    @ApiModelProperty(value = "合同/协议属性：1-新签；2-续签；3-变更；4-退约；5-历史")
//    private Integer attributes;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
