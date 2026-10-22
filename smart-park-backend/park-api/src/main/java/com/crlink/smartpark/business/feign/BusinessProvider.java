package com.crlink.smartpark.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *
 * </p>
 * */
@FeignClient(name = "park-business", path = "/smartPark/park-business", contextId = "BusinessProvider")
public interface BusinessProvider {

    @RequestMapping(value = "/business/business-chance/getChanceCount", method = RequestMethod.GET)
    Integer getChanceCount(@RequestParam("parkOrgId") String parkOrgId);
}
