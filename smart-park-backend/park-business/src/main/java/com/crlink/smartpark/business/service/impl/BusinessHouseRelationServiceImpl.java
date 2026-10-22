package com.crlink.smartpark.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.business.entity.BusinessHouseRelation;
import com.crlink.smartpark.business.mapper.BusinessHouseRelationMapper;
import com.crlink.smartpark.business.service.BusinessHouseRelationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 * */
@Service
public class BusinessHouseRelationServiceImpl extends ServiceImpl<BusinessHouseRelationMapper, BusinessHouseRelation> implements BusinessHouseRelationService {

    @Override
    public Set<String> getAllHouseInfoIds(Set<String> businessIds) {
        Set<String> houseInfoIds = this.lambdaQuery()
                .select(BusinessHouseRelation::getHouseInfoId)
                .in(CollectionUtil.isNotEmpty(businessIds), BusinessHouseRelation::getBusinessInfoId, businessIds)
                .list().parallelStream()
                .map(BusinessHouseRelation::getHouseInfoId)
                .collect(Collectors.toSet());
        return houseInfoIds;
    }

    @Override
    public Map<String, Set<String>> businessInfoId2HouseInfoIds(Set<String> businessInfoIds) {
        if (CollectionUtil.isEmpty(businessInfoIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> businessInfoId2HouseIds = this.lambdaQuery()
                .select(BusinessHouseRelation::getBusinessInfoId, BusinessHouseRelation::getHouseInfoId)
                .in(BusinessHouseRelation::getBusinessInfoId, businessInfoIds).list()
                .parallelStream()
                .collect(Collectors.groupingBy(BusinessHouseRelation::getBusinessInfoId,
                        Collectors.mapping(BusinessHouseRelation::getHouseInfoId, Collectors.toSet())));
        return businessInfoId2HouseIds;
    }


    @Override
    public Map<String, String> houseInfoId2BusinessInfoIdMap(Collection<String> houseInfoIds) {
        if (CollectionUtil.isEmpty(houseInfoIds)) {
            return Collections.emptyMap();
        }
        Map<String, String> houseInfoId2BusinessInfoIdMap = this.lambdaQuery()
                .select(BusinessHouseRelation::getHouseInfoId, BusinessHouseRelation::getBusinessInfoId)
                .in(BusinessHouseRelation::getHouseInfoId, houseInfoIds).list()
                .parallelStream()
                .collect(Collectors.toMap(BusinessHouseRelation::getHouseInfoId, BusinessHouseRelation::getBusinessInfoId));
        return houseInfoId2BusinessInfoIdMap;
    }

}
