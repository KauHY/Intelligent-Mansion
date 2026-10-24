package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.entity.Employee;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 入住企业员工信息表 服务类
 * </p>
 * */
public interface EmployeeService extends IService<Employee> {


    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Employee, ?> sValue);


    Map<String, Map<String, Object>> queryEmployeeColumns(EmployeeQueryDTO dto);

    Map<String, EmployeeDTO> queryEmployeeByCondition(EmployeeQueryDTO dto, String currentHeadOrgId);

    Map<String, EmployeeDTO> orderRecordQueryEmployeeColumns(Collection<String> employeeIds);

    Map<String, String> listSpecialEmployeeByLikeName(String orgId, String likeName);

    Map<String, String> listEmployeeByName(String likeName);

    //Map<String, String> queryEmployeeParkOrgId2NameMapByMobile(String mobile);

    //Set<String> queryCompanyIdsByMobile(String mobile);


    Map<String, Set<Integer>> getJobCodeByEmployeeIds(Collection<String> employeeIds);


    List<Employee> listEmployeeByLikeName(Collection<String> parkOrgIds,
                                          String likeName,
                                          Integer companyType,
                                          String companyId);

    //String getEmployeeCompanyId(String employeeId);

    //String getEmployeeParkOrgId(String employeeId);

    EmployeeVO findEmployeeByCompanyAndTel(String companyId, String mobile);

    Map<String, String> findEmployeeNamesByIds(String[] employeeIds);

    List<EmployeeVO> findEmployeeByCompanyIdsAndTagIds(Collection<String> companyIds, Collection<String> tagIds, Boolean translateParkCompany);

    Map<String, Integer> queryEmployeesCount(Collection<String> companyIds);

    EmployeeVO findEmployeeByDevicePersonId(Long devicePersonId);

    /**
     * 根据id获取员工信息
     *
     * @param id
     * @return
     */
    EmployeeVO getEmployeeVOById(String id);

}
