package com.crlink.smartpark.core.id2obj;

import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.system.feign.CommonProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BuildingIdHouseId2objHandler implements Id2objectSingleHandler {

    @Resource
    private CommonProvider commonProvider;

    @Override
    public String code() {
        return Id2objCodeConstant.BUILDING_HOUSE_NAME_CODE;
    }

    @Override
    public Map<Object, Object> ids2Objects(Set<Object> ids) {
        Set<String> strIdSet = ids.stream().map(e -> String.valueOf(e)).collect(Collectors.toSet());
        Map<String, String> map = commonProvider.getBuildingHouseNamesByIds(strIdSet);
        Map<Object, Object> nameObjMap = new HashMap<>();
        map.forEach((k, v) -> nameObjMap.put(k, v));
        return nameObjMap;
    }

}
