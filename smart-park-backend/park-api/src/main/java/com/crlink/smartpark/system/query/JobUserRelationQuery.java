package com.crlink.smartpark.system.query;

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
 * 岗位用户关系
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "JobUserRelation对象", description = "岗位用户关系")
public class JobUserRelationQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String jobId;

    private String userId;

    private Boolean deleted;
    @ApiModelProperty(value = "ids")
    private List<String> ids;


}
