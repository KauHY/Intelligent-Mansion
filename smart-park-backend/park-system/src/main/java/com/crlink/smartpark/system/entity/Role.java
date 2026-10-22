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
 * 系统角色
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属当前组织的ID
     */
    private String orgId;


    @ApiModelProperty(value = "当前组织的顶级组织的id,即租户")
    private String headOrgId;

    /**
     * 角色类型，0，系统用户，1平台用户
     */
    //private Integer roleType;
    /**
     * 是否内置的admin角色
     */
    private Boolean adminFlag;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDescription;

    /**
     * 状态:0-已禁用,1-正常
     */
    private Integer status;

    /**
     * 是否删除：0否1是
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 修改者
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
