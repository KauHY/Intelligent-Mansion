package com.crlink.smartpark.system.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MenuTreeDTO implements Serializable {
    private String id;

    @ApiModelProperty(value = "父节点id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    private List<MenuTreeDTO> children;

}
