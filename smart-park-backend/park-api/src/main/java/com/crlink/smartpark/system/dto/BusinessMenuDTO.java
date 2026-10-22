package com.crlink.smartpark.system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业端菜单/按钮表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BusinessMenu响应对象", description="企业端菜单/按钮表")
public class BusinessMenuDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "租户菜单对应的原始菜单ID")
    private String originId;

    @ApiModelProperty(value = "组织的顶级组织的id,即租户，0表示租户管理平台的模板菜单，-1表示租户管理平台的系统菜单")
    private String headOrgId;

    @ApiModelProperty(value = "父节点id，0表示菜单最上级节点")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Double sort;

    @ApiModelProperty(value = "状态：0：不显示，1：显示")
    private Integer status;

    @ApiModelProperty(value = "前端路由")
    private String route;

    @ApiModelProperty(value = "前端路由名")
    private String routeName;

    @ApiModelProperty(value = "接口路径")
    private String apiUrl;

    @ApiModelProperty(value = "类型：0：菜单 1：按钮")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
