package com.crlink.smartpark.business.dto.chance;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class HouseReqDTO implements Serializable {
    private String parkOrgId;
    private Set<String> houseInfoIds;
}
