package com.crlink.smartpark.system.dto.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 组织的角色
 * @Created by ZHOUXINJIAN3
 */
@Data
public class CreateRoleReqDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "角色名称")
    @Length(max = 30, message = "角色名称不能超过30个字")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    @Length(max = 200, message = "请输入不能超过200字")
    private String roleDescription;

    /**
     * 创建系统角色时关联租户，创建机构角色时关联机构ID
     */
    @ApiModelProperty(value = "组织的ID,创建系统角色时关联租户，创建机构角色时关联机构ID")
    private String orgId;


    @ApiModelProperty(value = "当前角色要关联的userID")
    private List<String> userIds;

}
