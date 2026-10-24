package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.business.entity.BusinessHouseRelation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 * */
public interface BusinessHouseRelationService extends IService<BusinessHouseRelation> {

    Set<String> getAllHouseInfoIds(Set<String> businessIds);

    Map<String, Set<String>> businessInfoId2HouseInfoIds(Set<String> businessInfoIds);


    Map<String, String> houseInfoId2BusinessInfoIdMap(Collection<String> houseInfoIds);
}
