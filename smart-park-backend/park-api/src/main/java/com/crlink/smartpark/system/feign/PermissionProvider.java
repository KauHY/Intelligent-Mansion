package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.system.dto.organization.OrganizationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Component
@FeignClient(name = "park-system", path = "/smartPark/park-system/permission", contextId = "PermissionProvider")
public interface PermissionProvider {

    @GetMapping("/orgIds")
    Set<OrganizationDTO> getOrganizationsByUser(@RequestParam("userId") String userId);

    @GetMapping("/disable/menuUrl")
    Set<String> getMenuUrlsByUser(@RequestParam("userId") String userId);
}
