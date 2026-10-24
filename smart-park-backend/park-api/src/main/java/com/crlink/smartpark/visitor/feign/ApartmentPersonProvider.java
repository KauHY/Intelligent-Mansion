package com.crlink.smartpark.visitor.feign;

import com.crlink.smartpark.visitor.dto.ApartmentPersonDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/apartmentPerson", contextId = "ApartmentPersonProvider")
public interface ApartmentPersonProvider {

    @ApiOperation(value = "公寓人员管理-根据员工ID查询公寓设备ID集合", tags = "公寓人员管理")
    @GetMapping("/findDeviceIdsByEmployeeId/{employeeId}")
    ApartmentPersonDTO findDeviceIdsByEmployeeId(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId);

    @ApiOperation(value = "公寓人员管理-根据员工ID查询关联房间ID集合", tags = "公寓人员管理")
    @GetMapping("/findHouseIdsByEmployeeId/{employeeId}")
    Set<String> findHouseIdsByEmployeeId(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId);

    @ApiOperation(value = "设备是否存在", tags = "公寓人员管理")
    @GetMapping("/existsDevice")
    boolean existsDevice(@RequestParam(value = "deviceId") String deviceId);

}
