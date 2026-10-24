package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class HouseIntentionQuery extends BaseQuery {
    @NotEmpty
    private String parkOrgId;
    private String buildingId;
    private String houseId;
}
