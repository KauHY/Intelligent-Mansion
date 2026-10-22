package com.crlink.smartpark.system.dto.user;

import com.crlink.smartpark.BaseQuery;
import lombok.Data;

@Data
public class RsUserDictQueryDTO extends BaseQuery {
    private String username;
    private String realName;
    private String orgId;
}
