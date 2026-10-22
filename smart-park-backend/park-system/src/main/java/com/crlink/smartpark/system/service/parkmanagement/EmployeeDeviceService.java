package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.system.entity.Employee;

import java.util.List;

/** * @desc
 */
public interface EmployeeDeviceService {

    String addEmployee(Employee employee);

    void batchAddEmployees(List<Employee> employees);

    String updateEmployee(Employee employee);

    void pushEmployee(Employee employee);

    void addAndPushEmployee(Employee employee);

    void updateAndPushEmployee(Employee employee);

    String delEmployee(Long personId, String cardNo);

    Boolean personExistById(Long personId);

    Boolean personExist(String telephone);

    String updateStatus(String employeeId, Integer status);

    String enablePerson(List<Long> personIds);

    String unablePerson(List<Long> personIds);

}
