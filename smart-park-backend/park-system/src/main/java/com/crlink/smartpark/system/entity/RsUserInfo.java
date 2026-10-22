package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_rs_user_info")
public class RsUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 对应tb_user表
     */
    @ApiModelProperty(value = "对应tb_user表id")
    private String userId;

    /**
     * 所属组织
     */
    @ApiModelProperty(value = "所属组织")
    private String orgId;


    @ApiModelProperty(value = "是否内置的admin用户")
    private Boolean adminFlag;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String realName;

    /**
     * 性别：1男；2女
     */
    @ApiModelProperty(value = "性别：1男；2女")
    private Integer sex;

    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证")
    private String idCard;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String mobile;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String telephone;

    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号")
    private String wechat;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL")
    private String avatarUrl;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 是否删除：0-否；1-是
     */
    @ApiModelProperty(value = "是否删除：0-否；1-是")
    private Boolean deleted;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    private String updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
