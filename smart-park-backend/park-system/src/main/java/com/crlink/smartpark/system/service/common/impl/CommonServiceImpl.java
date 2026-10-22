package com.crlink.smartpark.system.service.common.impl;

import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.service.common.CommonService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.UserInfoService;
import com.crlink.smartpark.system.service.parkmanagement.*;
import com.crlink.smartpark.system.vo.SimpleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TagService tagService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public Map<Integer, Map<String, String>> getNamesByIds(List<SimpleNode> list) {
        Map<Integer, Map<String, String>> result = new HashMap<>();
        Map<Integer, List<SimpleNode>> map = list.stream().collect(Collectors.groupingBy(SimpleNode::getType));
        for (Integer typeCode : map.keySet()) {
            List<SimpleNode> nodes = map.get(typeCode);
            Set<String> ids = nodes.stream().map(t -> (String) t.getValue()).collect(Collectors.toSet());
            Map<String, String> resultMap = getNamesByIdsAndType(ids, typeCode);
            result.put(typeCode, resultMap);
        }
        return result;
    }

    @Override
    //@Cacheable(value = CACHE_GROUP_COMMON, keyGenerator = CacheKeyGenerator)
    public Map<String, String> getNamesByIdsAndType(Set<String> ids, Integer typeCode) {
        Map<String, String> map = null;
        if (SimpleNode.TypeEnum.PARK.getCode().equals(typeCode)) {
            map = (Map<String, String>) organizationService.id2ValueMap(ids, Organization::getOrgName);
        } else if (SimpleNode.TypeEnum.COMPANY.getCode().equals(typeCode)) {
            map = (Map<String, String>) companyService.id2ValueMap(ids, Company::getName);
        } else if (SimpleNode.TypeEnum.Tag.getCode().equals(typeCode)) {
            map = tagService.id2Name(ids);
        } else if (SimpleNode.TypeEnum.EMPLOYEE.getCode().equals(typeCode)) {
            map = (Map<String, String>) employeeService.id2ValueMap(ids, Employee::getName);
        } else if (SimpleNode.TypeEnum.HOUSE.getCode().equals(typeCode)) {
            map = (Map<String, String>) houseService.id2ValueMap(ids, House::getName);
        } else if (SimpleNode.TypeEnum.BUILDING.getCode().equals(typeCode)) {
            map = (Map<String, String>) buildingService.id2ValueMap(ids, Building::getName);
        } else if (SimpleNode.TypeEnum.USER.getCode().equals(typeCode)) {
            map = (Map<String, String>) userInfoService.userid2ValueMap(ids, RsUserInfo::getRealName);
        } else if (SimpleNode.TypeEnum.BUILDING_HOUSE.getCode().equals(typeCode)) {
            map = houseService.getBuildingHouseNamesByIds(ids);
        }
        return map;
    }

    @Override
    public Map<String, String> getIdsByNamesAndType(Set<String> names, Integer typeCode, String parkOrgId) {
        Map<String, String> map = null;
        if (SimpleNode.TypeEnum.PARK.getCode().equals(typeCode)) {
            map = (Map<String, String>) organizationService.name2IdMap(names, parkOrgId);
        }
        if (SimpleNode.TypeEnum.BUILDING.getCode().equals(typeCode)) {
            map = (Map<String, String>) buildingService.name2IdMap(names, parkOrgId);
        }
        return map;
    }
}
