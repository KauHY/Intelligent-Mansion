package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RsUserInfo对象", description="系统用户表")
public class RsUserInfoQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "对应tb_user表")
    private String userId;

    @ApiModelProperty(value = "所属组织")
    private String orgId;

    @ApiModelProperty(value = "用户类型: sys系统用户, ldap用户")
    private String userType;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "性别：1男；2女")
    private Integer sex;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty(value = "微信号")
    private String wechat;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像URL")
    private String avatarUrl;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "状态：0-正常；1-禁用；2-过期")
    private Integer status;

    @ApiModelProperty(value = "是否删除：0-否；1-是")
    private Integer deleted;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "ids")
    private List<String> ids;


}
