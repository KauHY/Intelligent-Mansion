package com.crlink.smartpark.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.inspection.entity.InspectionEmployeeRelation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 巡检计划巡检人关系表 服务类
 * </p>
 * */
public interface InspectionEmployeeRelationService extends IService<InspectionEmployeeRelation> {


    Map<String, Set<String>> getEmployeeIds(Integer type, Collection<String> targetIds);
}
