package com.crlink.smartpark.system.dto.role;

import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    private String orgId;

    @ApiModelProperty(value = "所属当前组织")
    private String orgName;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "是否超级管理员")
    private Boolean adminFlag;

    @ApiModelProperty(value = "角色描述")
    private String roleDescription;

    @ApiModelProperty(value = "状态:0-已禁用,1-正常")
    private Integer status;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "用户")
    private List<DictVO> users;
}
