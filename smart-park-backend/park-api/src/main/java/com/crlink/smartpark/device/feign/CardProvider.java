package com.crlink.smartpark.device.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * <p>
 * 设备信息表 服务类
 * </p>
 * */
@FeignClient(name = "park-device", path = "/smartPark/park-device/card", contextId = "CardProvider")
public interface CardProvider {

    @ApiOperation(value = "卡片管理-根据员工ID查询所有可用卡号", tags = "卡片管理")
    @GetMapping("/findAllCardByEmployeeId/{employeeId}")
    Set<String> findAllCardByEmployeeId(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId);

    @ApiOperation(value = "卡片管理-根据员工ID查询是否有关联的卡片", tags = "卡片管理")
    @GetMapping("/containsEmployeeRelationCard/{employeeId}")
    Boolean containsEmployeeRelationCard(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId);

}
