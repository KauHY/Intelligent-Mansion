package com.crlink.smartpark.system.dto.organization;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrgRespVO implements Serializable {
    private String id;
    private String orgName;
    private Integer orgType;
    private String parentId;
    private String parentOrgName;
    private String description;
    private LocalDateTime createTime;
}
