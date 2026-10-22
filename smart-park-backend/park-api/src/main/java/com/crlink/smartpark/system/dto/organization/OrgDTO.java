package com.crlink.smartpark.system.dto.organization;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @Description 组织结构的组织
 * @Created by ZHOUXINJIAN3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgDTO implements Serializable {
    private String id;

    @ApiModelProperty(value = "组织名称")
    @Length(max = 30, message = "组织名称不能超过30字")
    private String orgName;

    @ApiModelProperty(value = "组织类型")
    private Integer orgType;

    @ApiModelProperty(value = "上级组织")
    private String parentId;

    @ApiModelProperty(value = "描述")
    @Length(max = 200, message = "描述不能超过200字")
    private String description;

    public OrgDTO(String id, String orgName, Integer orgType) {
        this.id = id;
        this.orgName = orgName;
        this.orgType = orgType;
    }
}
