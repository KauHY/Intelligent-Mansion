package com.crlink.smartpark.visitor.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.visitor.entity.ApartmentPersonHouseRelation;
import com.crlink.smartpark.visitor.mapper.ApartmentPersonHouseRelationMapper;
import com.crlink.smartpark.visitor.service.ApartmentPersonHouseRelationService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 公寓人员管理房屋关系表 服务实现类
 * */
@Service
public class ApartmentPersonHouseRelationServiceImpl extends ServiceImpl<ApartmentPersonHouseRelationMapper, ApartmentPersonHouseRelation> implements ApartmentPersonHouseRelationService {

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<ApartmentPersonHouseRelation, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(ApartmentPersonHouseRelation::getId, sValue)
                .in(ApartmentPersonHouseRelation::getId, ids).list().stream()
                .collect(HashMap::new, (map, relation) -> map.put(relation.getId(), sValue.apply(relation)), HashMap::putAll);
        return result;
    }


    /**
     * 根据公寓人员id查询对应的所有房屋id
     *
     * @param personIds
     * @return
     */
    @Override
    public Map<String, Set<String>> personId2HouseIds(Collection<String> personIds) {
        if (ObjectUtils.isEmpty(personIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> result = this.lambdaQuery()
                .select(ApartmentPersonHouseRelation::getId, ApartmentPersonHouseRelation::getApartmentPersonId,
                        ApartmentPersonHouseRelation::getHouseId)
                .in(ApartmentPersonHouseRelation::getApartmentPersonId, personIds).list()
                .parallelStream().collect(Collectors.groupingBy(ApartmentPersonHouseRelation::getApartmentPersonId,
                        Collectors.mapping(ApartmentPersonHouseRelation::getHouseId, Collectors.toSet())));
        return result;
    }


    /**
     * 根据房屋id查询对应的所有员工id
     *
     * @param houseIds
     * @return
     */
    @Override
    public Map<String, Set<String>> houseId2EmployeeIds(Collection<String> houseIds) {
        if (ObjectUtils.isEmpty(houseIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> result = this.lambdaQuery()
                .select(ApartmentPersonHouseRelation::getHouseId, ApartmentPersonHouseRelation::getEmployeeId)
                .in(ApartmentPersonHouseRelation::getHouseId, houseIds).list()
                .parallelStream().collect(Collectors.groupingBy(ApartmentPersonHouseRelation::getHouseId,
                        Collectors.mapping(ApartmentPersonHouseRelation::getEmployeeId, Collectors.toSet())));
        return result;
    }
}
