package com.crlink.smartpark.inspection.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.inspection.entity.CircuitPointRelation;
import com.crlink.smartpark.inspection.mapper.CircuitPointRelationMapper;
import com.crlink.smartpark.inspection.service.CircuitPointRelationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CircuitPointRelationServiceImpl extends ServiceImpl<CircuitPointRelationMapper, CircuitPointRelation> implements CircuitPointRelationService {

    @Override
    public Map<String, Set<String>> circuitId2PointIds(Collection<String> circuitIds) {
        if (ObjectUtil.isEmpty(circuitIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> circuitId2PointIdsMap = this.lambdaQuery().select(CircuitPointRelation::getCircuitId, CircuitPointRelation::getPointId)
                .in(CircuitPointRelation::getCircuitId, circuitIds).list()
                .parallelStream().collect(Collectors.groupingBy(CircuitPointRelation::getCircuitId,
                        Collectors.mapping(CircuitPointRelation::getPointId, Collectors.toSet())));
        return circuitId2PointIdsMap;
    }

    @Override
    public Map<String, Set<String>> pointId2CircuitIds(Collection<String> pointIds) {
        if (ObjectUtil.isEmpty(pointIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> pointId2CircuitIds = this.lambdaQuery().select(CircuitPointRelation::getCircuitId, CircuitPointRelation::getPointId)
                .in(CircuitPointRelation::getPointId, pointIds).list()
                .parallelStream().collect(Collectors.groupingBy(CircuitPointRelation::getPointId,
                        Collectors.mapping(CircuitPointRelation::getCircuitId, Collectors.toSet())));
        return pointId2CircuitIds;
    }
}
