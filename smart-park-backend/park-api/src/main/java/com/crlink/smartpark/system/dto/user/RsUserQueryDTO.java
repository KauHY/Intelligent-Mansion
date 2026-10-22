package com.crlink.smartpark.system.dto.user;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
public class RsUserQueryDTO extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "状态:0-已禁用,1-正常")
    private Integer status;

    //@ApiModelProperty(value = "角色列表")
    //private Set<String> roleIds;

}
