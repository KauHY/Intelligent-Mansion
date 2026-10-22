package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
public class RsUserInfoVO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "用户名id")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "是否admin用户")
    private Boolean adminFlag;

    @ApiModelProperty(value = "所属组织")
    private String orgId;

    @ApiModelProperty(value = "所属组织名称")
    private String orgName;

    @ApiModelProperty(value = "角色列表")
    private List<String> roleIds;

    @ApiModelProperty(value = "角色列表")
    private List<String> roleNames;


    @ApiModelProperty(value = "状态")
    private Integer status;

}
