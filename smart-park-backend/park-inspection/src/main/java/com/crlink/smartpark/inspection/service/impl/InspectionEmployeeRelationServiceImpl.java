package com.crlink.smartpark.inspection.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.inspection.entity.InspectionEmployeeRelation;
import com.crlink.smartpark.inspection.mapper.InspectionEmployeeRelationMapper;
import com.crlink.smartpark.inspection.service.InspectionEmployeeRelationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 巡检计划巡检人关系表 服务实现类
 * </p>
 * */
@Service
public class InspectionEmployeeRelationServiceImpl extends ServiceImpl<InspectionEmployeeRelationMapper, InspectionEmployeeRelation> implements InspectionEmployeeRelationService {

    @Override
    public Map<String, Set<String>> getEmployeeIds(Integer type, Collection<String> targetIds) {
        if (ObjectUtil.isEmpty(targetIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> targetId2EmployeeIdsMap = this.lambdaQuery()
                .select(InspectionEmployeeRelation::getEmployeeId, InspectionEmployeeRelation::getTargetId)
                .eq(InspectionEmployeeRelation::getType, type)
                .in(InspectionEmployeeRelation::getTargetId, targetIds).list()
                .parallelStream().collect(Collectors.groupingBy(InspectionEmployeeRelation::getTargetId,
                        Collectors.mapping(InspectionEmployeeRelation::getEmployeeId, Collectors.toSet())));
        return targetId2EmployeeIdsMap;
    }
}
