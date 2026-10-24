package com.crlink.smartpark.visitor.feign;

import com.crlink.smartpark.visitor.dto.DeviceIdsAndCardNosDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 策略员工
 * </p>
 * */
@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/policy-employee", contextId = "PolicyEmployeeProvider")
public interface PolicyEmployeeProvider {

    @GetMapping("/{personId}/findByPersonId")
    DeviceIdsAndCardNosDTO findByPersonId(@PathVariable("personId") @NotNull(message = "personId不能为空") Long personId);

}
