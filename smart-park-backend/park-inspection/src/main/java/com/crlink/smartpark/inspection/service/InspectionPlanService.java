package com.crlink.smartpark.inspection.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.inspection.controller.request.InspectionPlanReqDTO;
import com.crlink.smartpark.inspection.controller.request.PlanQuery;
import com.crlink.smartpark.inspection.controller.request.PlanRelateEmployeeReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionPlanDTO;
import com.crlink.smartpark.inspection.dto.resultmap.PlanCircuitEntity;
import com.crlink.smartpark.inspection.entity.InspectionPlan;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 巡检计划 服务类
 * </p>
 * */
public interface InspectionPlanService extends IService<InspectionPlan> {

    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<InspectionPlan, ?> sValue);

    Map<String, PlanCircuitEntity> queryCircuitByPlanId(Collection<String> planIds);

    Boolean saveOrUpdatePlan(InspectionPlanReqDTO reqDTO);

    PageInfo<InspectionPlanDTO> listPlanPage(PlanQuery query);

    Boolean updatePlanStatus(String id, Integer status);

    Boolean planRelateEmployees(PlanRelateEmployeeReqDTO dto);

    InspectionPlanDTO findById(String id);

    //Boolean deletePlan(String id);
}
