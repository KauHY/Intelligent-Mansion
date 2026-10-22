package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 业务和园区多对多关系表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BusinessParkRelation对象", description = "业务和园区多对多关系表")
public class BusinessParkRelationQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String businessId;

    private Integer parkOrgId;

    private Integer businessType;

    private Integer status;

    private Boolean deleted;
    @ApiModelProperty(value = "ids")
    private List<String> ids;


}
