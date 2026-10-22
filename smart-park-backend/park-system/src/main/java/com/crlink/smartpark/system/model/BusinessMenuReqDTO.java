package com.crlink.smartpark.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 企业端菜单/按钮表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BusinessMenu请求对象", description="企业端菜单/按钮表")
public class BusinessMenuReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "租户菜单对应的原始菜单ID", required = true)
    private String originId;

    @ApiModelProperty(value = "组织的顶级组织的id,即租户，0表示租户管理平台的模板菜单，-1表示租户管理平台的系统菜单", required = true)
    private String headOrgId;

    @ApiModelProperty(value = "父节点id，0表示菜单最上级节点", required = true)
    private String parentId;

    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "图标", required = true)
    private String icon;

    @ApiModelProperty(value = "排序", required = true)
    private Double sort;

    @ApiModelProperty(value = "状态：0：不显示，1：显示", required = true)
    private Integer status;

    @ApiModelProperty(value = "前端路由", required = true)
    private String route;

    @ApiModelProperty(value = "前端路由名", required = true)
    private String routeName;

    @ApiModelProperty(value = "接口路径", required = true)
    private String apiUrl;

    @ApiModelProperty(value = "类型：0：菜单 1：按钮", required = true)
    private Integer type;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "修改人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "删除状态", required = true)
    private Boolean deleted;


}
