package com.crlink.smartpark.system.dto.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CompanyQueryDTO implements Serializable {
    private String parkOrgId;
    private Collection<String> companyIds;
    private String[] columns = {};


    public static CompanyQueryDTO buildingIdsBuildDTO(String parkOrgId, String... columns) {
        CompanyQueryDTO companyQueryDTO = new CompanyQueryDTO();
        companyQueryDTO.setParkOrgId(parkOrgId);
        companyQueryDTO.setColumns(columns);
        return companyQueryDTO;
    }

    public static CompanyQueryDTO buildingIdsBuildDTO(Collection<String> ids, String... columns) {
        CompanyQueryDTO companyQueryDTO = new CompanyQueryDTO();
        companyQueryDTO.setCompanyIds(ids);
        companyQueryDTO.setColumns(columns);
        return companyQueryDTO;
    }

}
