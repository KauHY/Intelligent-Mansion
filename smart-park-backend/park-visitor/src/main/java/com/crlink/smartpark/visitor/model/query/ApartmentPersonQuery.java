package com.crlink.smartpark.visitor.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 公寓人员管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApartmentPerson查询对象", description = "公寓人员管理")
public class ApartmentPersonQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "姓名")
    private String name;

//    @ApiModelProperty(value = "手机号")
//    private String mobile;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;

    @ApiModelProperty(value = "所属企业Id")
    private String companyId;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    private Integer status;

    @ApiModelProperty(value = "房屋名称")
    private String houseName;


}
