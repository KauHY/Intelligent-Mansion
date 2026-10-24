package com.crlink.smartpark.system.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MenuDTO implements Serializable {
    private String id;

    @ApiModelProperty(value = "租户菜单对应的原始菜单ID")
    private String originId;

    @ApiModelProperty(value = "组织的顶级组织的id,即租户")
    private String headOrgId;

    @ApiModelProperty(value = "父节点id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Double sort;

    @ApiModelProperty(value = "前端路由")
    private String route;

    @ApiModelProperty(value = "前端路由名")
    private String routeName;

    @ApiModelProperty(value = "接口路径")
    private String apiUrl;

    @ApiModelProperty(value = " 类型：0：菜单 1：按钮")
    private Integer type;

    private List<MenuDTO> children;

}
