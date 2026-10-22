package com.crlink.smartpark.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 * */
@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/apartmentPerson", contextId = "ApartmentPersonHouseRelationProvider")
public interface ApartmentPersonHouseRelationProvider {

    /**
     * 根据房屋id获取关联的所有员工id
     *
     * @param houseIds
     * @return
     */
    @RequestMapping(value = "/houseRelation/getEmployeeIdsByHouseId", method = RequestMethod.GET)
    Map<String, Set<String>> getEmployeeIdsByHouseId(@RequestParam("houseIds") Set<String> houseIds);
}
