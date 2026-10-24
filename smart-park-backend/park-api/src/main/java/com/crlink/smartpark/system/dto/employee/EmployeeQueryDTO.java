package com.crlink.smartpark.system.dto.employee;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
public class EmployeeQueryDTO implements Serializable {
    private Collection<String> employeeIds;
    private String mobile;
    private String likeName;
    private String parkOrgId;

    private String[] columns = {};
    private boolean isShowCompanyName;
    private boolean isShowParkOrgName;
    private boolean isShowJobCodes;
    private boolean isShowTagNames;


    public static EmployeeQueryDTO employeeIdsBuildDTO(Collection<String> employeeIds, String... columns) {
        EmployeeQueryDTO employeeQueryDTO = new EmployeeQueryDTO();
        employeeQueryDTO.setEmployeeIds(employeeIds);
        employeeQueryDTO.setColumns(columns);
        return employeeQueryDTO;
    }

    public EmployeeQueryDTO(String mobile) {
        this.mobile = mobile;
    }

    public static EmployeeQueryDTO mobileBuildDTO(String mobile, String... columns) {
        EmployeeQueryDTO employeeQueryDTO = new EmployeeQueryDTO();
        employeeQueryDTO.setMobile(mobile);
        employeeQueryDTO.setColumns(columns);
        return employeeQueryDTO;
    }

    public static EmployeeQueryDTO mobileAndParkOrgIdBuildDTO(String mobile, String parkOrgId, String... columns) {
        EmployeeQueryDTO employeeQueryDTO = new EmployeeQueryDTO();
        employeeQueryDTO.setMobile(mobile);
        employeeQueryDTO.setParkOrgId(parkOrgId);
        employeeQueryDTO.setColumns(columns);
        return employeeQueryDTO;
    }

    public static EmployeeQueryDTO likeNameBuildDTO(String likeName, String... columns) {
        EmployeeQueryDTO employeeQueryDTO = new EmployeeQueryDTO();
        employeeQueryDTO.setLikeName(likeName);
        employeeQueryDTO.setColumns(columns);
        return employeeQueryDTO;
    }

    public boolean isShowCompanyName() {
        return isShowCompanyName;
    }

    public void setShowCompanyName(boolean showCompanyName) {
        isShowCompanyName = showCompanyName;
    }

    public boolean isShowParkOrgName() {
        return isShowParkOrgName;
    }

    public void setShowParkOrgName(boolean showParkOrgName) {
        isShowParkOrgName = showParkOrgName;
    }

    public boolean isShowJobCodes() {
        return isShowJobCodes;
    }

    public void setShowJobCodes(boolean showJobCodes) {
        isShowJobCodes = showJobCodes;
    }

    public boolean isShowTagNames() {
        return isShowTagNames;
    }

    public void setShowTagNames(boolean showTagNames) {
        isShowTagNames = showTagNames;
    }
}
