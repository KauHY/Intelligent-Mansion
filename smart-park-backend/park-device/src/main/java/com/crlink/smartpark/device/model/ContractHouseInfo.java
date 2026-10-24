package com.crlink.smartpark.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractHouseInfo implements Serializable {
    private Set<String> houseIds;
    private Map<String, Set<String>> buildingFloorHouseMap;
}
