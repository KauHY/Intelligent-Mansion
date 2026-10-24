package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.vo.SimpleNode;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 入住企业员工信息表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/parkManagement/employee", contextId = "EmployeeProvider")
public interface EmployeeProvider {


    @PostMapping("/queryEmployeeColumns")
    Map<String, Map<String, Object>> queryEmployeeColumns(@RequestBody EmployeeQueryDTO dto);

    @ApiOperation(value = "查询员工信息2")
    @PostMapping("/queryEmployeeByCondition")
    Map<String, EmployeeDTO> queryEmployeeByCondition(@RequestBody EmployeeQueryDTO queryDTO);

    @PostMapping("/orderRecordQueryEmployeeColumns")
    Map<String, EmployeeDTO> orderRecordQueryEmployeeColumns(@RequestBody Collection<String> employeeIds);

    @ApiOperation(value = "根据员工名like查询物业企业员工")
    @GetMapping("/listSpecialEmployeeByLikeName")
    Map<String, String> getEmployeeIdsByNameLike(@RequestParam(value = "orgId", required = false) String orgId, @RequestParam("name") String name);

    @ApiOperation(value = "获取员工岗位", tags = "内部接口")
    @PostMapping("/getJobCodeByEmployeeId")
    Map<String, Set<Integer>> getJobCodeByEmployeeIds(@RequestBody Collection<String> employeeIds);

    //@ApiOperation(value = "获取员工公司Id", tags = "内部接口")
    //@GetMapping("/getEmployeeCompanyId")
    //String getEmployeeCompanyId(@RequestParam("employeeId") String employeeId);

    //@ApiOperation(value = "获取员工园区Id", tags = "内部接口")
    //@GetMapping("/getEmployeeParkOrgId")
    //String getEmployeeParkOrgId(@RequestParam("employeeId") String employeeId);


    @ApiOperation(value = "根据员工名like查询企业员工")
    @GetMapping("/listEmployeeByName")
    Map<String, String> listEmployeeByName(@RequestParam("name") String name);

    @ApiOperation(value = "通过企业ID和手机号获取员工", tags = "内部接口")
    @GetMapping("/findEmployeeByCompanyAndTel/{companyId}/{mobile}")
    EmployeeVO findEmployeeByCompanyAndTel(@PathVariable("companyId") String companyId, @PathVariable("mobile") String mobile);

    @ApiOperation(value = "通过员工ID集合获取员工名集合", tags = "内部接口")
    @GetMapping("/findEmployeeNamesByIds")
    Map<String, String> findEmployeeNamesByIds(@RequestParam("employeeIds") String[] employeeIds);

    @ApiOperation(value = "通过企业ID集合和标签ID集合获取员工", tags = "内部接口")
    @GetMapping("/findEmployeeByCompanyIdsAndTagIds")
    List<EmployeeVO> findEmployeeByCompanyIdsAndTagIds(@RequestParam(value = "companyIds", required = false) Collection<String> companyIds, @RequestParam(value = "tagIds", required = false) Collection<String> tagIds, @RequestParam("translateParkCompany") Boolean translateParkCompany);

    @ApiOperation(value = "查询员工3", tags = "内部接口")
    @GetMapping("/queryEmployeesList")
    List<SimpleNode> queryEmployeesList(@RequestParam(value = "orgId", required = false) String orgId,
                                        @RequestParam(value = "parkOrgId", required = false) String parkOrgId,
                                        @RequestParam(value = "companyId", required = false) String companyId,
                                        @RequestParam(value = "companyType", required = false) Integer companyType,
                                        @RequestParam(value = "employeeName", required = false) String employeeName,
                                        @RequestParam(value = "jobCodes", required = false) Integer[] jobCodes);

    @PostMapping("/queryEmployeesCount")
    Map<String, Integer> queryEmployeesCount(@RequestBody Collection<String> companyIds);

    @ApiOperation(value = "通过员工人员设备ID获取员工", tags = "内部接口")
    @GetMapping("/findEmployeeByDevicePersonId/{devicePersonId}")
    EmployeeVO findEmployeeByDevicePersonId(@PathVariable("devicePersonId") Long devicePersonId);

    @ApiOperation(value = "根据id获取员工信息", tags = "内部接口")
    @GetMapping("/getEmployeeById/{id}")
    EmployeeVO getEmployeeById(@PathVariable("id") String id);

    @ApiOperation(value = "更新卡号", tags = "内部接口")
    @PostMapping("/updateEmployeeCardNo")
    Boolean updateEmployeeCardNo(@RequestParam(value = "id") String id, @RequestParam(value = "cardNo") String cardNo);

}
