package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.system.vo.SimpleNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "park-system", path = "/smartPark/park-system/common_feign", contextId = "CommonProvider")
public interface CommonProvider {

    /**
     * @title 通过ids获取名字
     */
    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.POST)
    Map<Integer, Map<String, String>> getNamesByIds(@RequestBody List<SimpleNode> list);

    /**
     * 通过类型和ids获取名字
     *
     * @param ids
     * @param typeCode
     */
    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.GET)
    Map<String, String> getNamesByIdsAndType(@RequestParam("ids") Set<String> ids, @RequestParam("typeCode") Integer typeCode);

    @RequestMapping(value = "/getBuildingHouseNamesByIds", method = RequestMethod.GET)
    Map<String, String> getBuildingHouseNamesByIds(@RequestParam("ids") Set<String> houseIds);

    @RequestMapping(value = "/getIdsByNames", method = RequestMethod.GET)
    Map<String, String> getIdsByNamesAndType(@RequestParam(value = "names", required = false) Set<String> names,
                                             @RequestParam("typeCode") Integer typeCode,
                                             @RequestParam(value = "parkOrgId", required = false) String parkOrgId);

    /**
     * 根据组织id，获取名称
     *
     * @param orgIds
     * @return
     */
    @RequestMapping(value = "/getOrgNamesByOrgIds", method = RequestMethod.POST)
    Map<String, String> getOrgNamesByOrgIds(@RequestBody Set<String> orgIds);

    /**
     * 根据当前用户获取园区的组织id
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/getCurrentUseParkOrgIds", method = RequestMethod.POST)
    Set<String> getCurrentUseParkOrgIds();


    /**
     * 根据组织id获取用户园区id集合
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/getAllParkOrgIdsByOrgId", method = RequestMethod.GET)
    Set<String> getAllParkOrgIdsByOrgId(@RequestParam("orgId") String orgId);


    /**
     * 根据当前用户获取用户信息
     *
     * @param userIds
     * @param columns
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    Map<String, Map<String, Object>> getUsernameByUserId(@RequestParam("userIds") Set<String> userIds, @RequestParam("columns") Set<String> columns);

}
