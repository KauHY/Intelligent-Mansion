package com.crlink.smartpark.system.dto.organization;

import com.crlink.smartpark.BaseQuery;
import lombok.Data;

@Data
public class OrgQuery extends BaseQuery {
    private String parentId;
    private String orgName;
}
