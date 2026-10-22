package com.crlink.smartpark.system.dto.house;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class HouseQueryDTO implements Serializable {
    private String parkOrgId;
    private Collection<String> buildingIds;
    private Collection<String> houseIds;
    private Integer houseStatus;
    private Integer houseFunction;
    private Collection<Integer> houseFunctions;
    private Collection<Integer> houseProperties;
    private Double minCoverArea;
    private Double maxCoverArea;
    private String name;;
    private String[] columns = {};


    public static HouseQueryDTO buildingIdsBuildDTO(String parkOrgId, Collection<String> buildingIds, String... columns) {
        HouseQueryDTO hoseQueryDTO = new HouseQueryDTO();
        hoseQueryDTO.setParkOrgId(parkOrgId);
        hoseQueryDTO.setBuildingIds(buildingIds);
        hoseQueryDTO.setColumns(columns);
        return hoseQueryDTO;
    }


    public static HouseQueryDTO houseIdsBuildDTO(String parkOrgId, Collection<String> houseIds, String... columns) {
        HouseQueryDTO hoseQueryDTO = new HouseQueryDTO();
        hoseQueryDTO.setParkOrgId(parkOrgId);
        hoseQueryDTO.setHouseIds(houseIds);
        hoseQueryDTO.setColumns(columns);
        return hoseQueryDTO;
    }

}
