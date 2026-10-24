package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 菜单/按钮表
 * </p>
 * */
@Data
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 租户菜单对应的原始菜单ID
     */
    private String originId;

    /**
     * 组织的顶级组织的id,即租户
     */
    private String headOrgId;

    /**
     * 父节点id
     */
    private String parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String icon;

    /**
     * 排序
     */
    private Double sort;


//    /**
//     * 状态：0：不显示，1：显示
//     */
//    private Integer status;

    /**
     * 前端路由
     */
    private String route;

    /**
     * 前端路由名
     */
    private String routeName;

    /**
     * 接口路径
     */
    private String apiUrl;

    /**
     * 类型：0：菜单 1：按钮  2, "导航"
     */
    private Integer type;

    /**
     * 创建时间
     */
//    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
//    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除状态
     */
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
