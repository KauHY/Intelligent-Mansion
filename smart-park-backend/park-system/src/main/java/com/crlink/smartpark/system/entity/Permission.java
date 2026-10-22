package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限类型
     */
    private Integer permissionType;

    /**
     * 目标id:menuId,apiUrl,buttonId,companyNo,communityNo等,floor,room',
     */
    private String destId;

    /**
     * '状态:0-已禁用,1-正常'
     */
    private Integer status;

    /**
     * 是否删除0，否，1是
     */
    private Boolean deleted;

    /**
     * 用于区分组织树的父节点勾选情况，1用户勾选，2，系统插进去
     */
    private Integer mark;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;


}
