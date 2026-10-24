package com.crlink.smartpark.system.dto.wxuser;

import com.crlink.smartpark.common.annotations.Id2objectField;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

@Data
public class EmployeeInfoVO implements Serializable {
    private String employeeId;
    private String name;
    private String companyId;
    @Id2objectField(code = COMPANY_NAME_CODE, idField = "companyId")
    private String companyName;
    private String parkOrgId;
    @Id2objectField(code = PARK_ORG_NAME_CODE, idField = "parkOrgId")
    private String parkOrgName;
    private Set<Integer> jobCodes;

}
