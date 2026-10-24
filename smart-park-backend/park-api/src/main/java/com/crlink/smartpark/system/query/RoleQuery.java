package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统角色
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Role查询对象")
public class RoleQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "状态:0-已禁用,1-正常")
    private Integer status;

    //@ApiModelProperty(value = "用户组织Id")
    //private String orgId;

}
