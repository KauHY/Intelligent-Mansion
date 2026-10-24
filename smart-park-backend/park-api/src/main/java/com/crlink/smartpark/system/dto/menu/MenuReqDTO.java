package com.crlink.smartpark.system.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MenuReqDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "父节点id")
    @NotEmpty
    private String parentId;

    @NotNull
    @ApiModelProperty(value = "名称")
    @Length(max = 30, message = "名称不能超过30个字")
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    @NotNull
    private Double sort;

    @ApiModelProperty(value = "接口路径")
    @Length(max = 200)
    private String apiUrl;

    @ApiModelProperty(value = "类型：0：菜单 1：按钮")
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "路由")
    private String route;

    @ApiModelProperty(value = "路由名")
    @Length(max = 100, message = "路由名不能超过100个字")
    private String routeName;

}
