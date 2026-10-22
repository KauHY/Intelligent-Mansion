package com.crlink.smartpark.system.service.common;

import com.crlink.smartpark.system.vo.SimpleNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommonService {

    /**
     * 通过Ids获取名字
     *
     * @return key:对象类型，value：map{key：id，value：名字}
     */
    Map<Integer, Map<String, String>> getNamesByIds(List<SimpleNode> list);

    /**
     * 通过类型和ids获取名字
     *
     * @param ids
     * @param typeCode
     */
    Map<String, String> getNamesByIdsAndType(Set<String> ids, Integer typeCode);

    Map<String, String> getIdsByNamesAndType(Set<String> names, Integer typeCode, String parkOrgId);
}
