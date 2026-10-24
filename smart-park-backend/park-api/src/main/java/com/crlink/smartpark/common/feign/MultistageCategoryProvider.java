package com.crlink.smartpark.common.feign;

import com.crlink.smartpark.common.dto.MultistageCategoryDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationQueryDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 * */
@FeignClient(name = "park-common", path = "/smartPark/park-common/multistage-category", contextId = "MultistageCategoryProvider")
public interface MultistageCategoryProvider {

    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.GET)
    Map<String, String> getNamesByIds(@RequestParam("ids") Set<String> ids);

    @RequestMapping(value = "/listBusinessToNCategoryRelation", method = RequestMethod.POST)
    PageInfo<MultistageCategoryRelationDTO> listBusinessToNCategoryRelation(MultistageCategoryRelationQueryDTO multistageCategoryRelationQueryDTO);

    @RequestMapping(value = "/listBusinessToCategoryRelation", method = RequestMethod.POST)
    PageInfo<MultistageCategoryRelationDTO> listBusinessToCategoryRelation(MultistageCategoryRelationQueryDTO multistageCategoryRelationQueryDTO);

    @RequestMapping(value = "/getAllSubId", method = RequestMethod.GET)
    Set<String> getAllSubId(@RequestParam("id") String id);

    @GetMapping("/getTzIdBySecondName")
    String getTzIdBySecondName(@RequestParam("name") String name, @RequestParam("parkOrgId") String parkOrgId);

}
