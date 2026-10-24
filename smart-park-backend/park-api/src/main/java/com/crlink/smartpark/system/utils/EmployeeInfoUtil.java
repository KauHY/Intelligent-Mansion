package com.crlink.smartpark.system.utils;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
public class EmployeeInfoUtil {

    private static EmployeeProvider employeeProvider;

    @Autowired
    private void setEmployeeProvider(EmployeeProvider employeeProvider) {
        EmployeeInfoUtil.employeeProvider = employeeProvider;
    }

    /**
     * 通过ID获取员工名
     *
     * @param employeeId
     * @return
     */
    public static String getEmployeeNameById(String employeeId) {
        EmployeeQueryDTO queryDTO = EmployeeQueryDTO.employeeIdsBuildDTO(Sets.newHashSet(employeeId), "name");
        Map<String, Map<String, Object>> employeeColumns = employeeProvider.queryEmployeeColumns(queryDTO);
        if (CollectionUtils.isEmpty(employeeColumns)) {
            return null;
        }
        return (String) employeeColumns.get(employeeId).get("name");
    }

    /**
     * 通过ID获取员工名为空异常
     *
     * @param employeeId
     * @return
     */
    public static String getEmployeeNameByIdOrEx(String employeeId) {
        String name = getEmployeeNameById(employeeId);
        AssertUtil.requireNotNullOrParamExp(name);
        return name;
    }

}
