package com.crlink.smartpark.visitor.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.vo.AccessPolicyVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/access-policy", contextId = "AccessPolicyProvider")
public interface AccessPolicyProvider {

    @ApiOperation(value = "通过员工信息获取关联设备ID", tags = "人行管理")
    @GetMapping("/getAccessDeviceIdsByEmployeeInfo")
    Set<String> getAccessDeviceIdsByEmployeeInfo(@RequestParam("employeeCompanyId") String employeeCompanyId, @RequestParam(value = "employeeTagIds", required = false) Collection<String> employeeTagIds);

    @ApiOperation(value = "通行策略-根据ID查询", tags = "人行管理")
    @GetMapping("/findById/{id}")
    ResponseResult<AccessPolicyVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id);

    @ApiOperation(value = "员工标签是否存在于通行策略", tags = "人行管理")
    @GetMapping("/getTagIdsExist")
    Boolean getTagIdsExist(@RequestParam(value = "employeeTagId") String employeeTagId);

    @ApiOperation(value = "设备是否存在", tags = "人行管理")
    @GetMapping("/existsDevice")
    boolean existsDevice(@RequestParam(value = "deviceId") String deviceId);

}
