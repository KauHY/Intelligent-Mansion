package com.crlink.smartpark.system.dto.house;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseVO extends HouseDTO {
    private String parkOrgName;
    private String buildingName;
    private Integer totalFloor;


}
