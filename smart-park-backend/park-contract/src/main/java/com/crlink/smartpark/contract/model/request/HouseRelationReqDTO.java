package com.crlink.smartpark.contract.model.request;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 合同/计费房屋关联
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HouseRelationReqDTO对象", description="合同/计费房屋关联")
public class HouseRelationReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    /**
     * 类型为1时合同表主键；类型为2时合同计费信息表主键
     */
    @ApiModelProperty(value = "类型为1时合同表主键；类型为2时合同计费信息表主键")
    private String pkId;

    /**
     * 类型：1-合同关联房屋；2-计费信息应用房源
     */
    @ApiModelProperty(value = "类型：1-合同关联房屋；2-计费信息应用房源")
    private Integer type;

    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id")
    private String houseId;

    @ApiModelProperty(value = "ids")
    private List<String> ids;


}
