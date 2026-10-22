package com.crlink.smartpark.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.inspection.entity.CircuitPointRelation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface CircuitPointRelationService extends IService<CircuitPointRelation> {


    Map<String, Set<String>> circuitId2PointIds(Collection<String> circuitIds);

    Map<String, Set<String>> pointId2CircuitIds(Collection<String> pointIds);
}
