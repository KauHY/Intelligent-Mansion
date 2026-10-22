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
public class RsUserLoginVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "所属组织")
    private String orgId;

    @ApiModelProperty(value = "所属租户Id")
    private String headOrgId;

    @ApiModelProperty(value = "角色名称,Id")
    private List<DictVO> userRoles;

    //@ApiModelProperty(value = "状态")
    //private Integer status;

    @ApiModelProperty(value = "tokenId")
    private String tokenId;

    @ApiModelProperty(value = "accessToken")
    private String accessToken;

    //@ApiModelProperty(value = "refreshToken")
    //private String refreshToken;

}
