package com.crlink.smartpark.inspection.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.inspection.controller.request.InspectionPlanReqDTO;
import com.crlink.smartpark.inspection.controller.request.PlanQuery;
import com.crlink.smartpark.inspection.controller.request.PlanRelateEmployeeReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionPlanDTO;
import com.crlink.smartpark.inspection.dto.resultmap.PlanCircuitEntity;
import com.crlink.smartpark.inspection.entity.InspectionCircuit;
import com.crlink.smartpark.inspection.entity.InspectionEmployeeRelation;
import com.crlink.smartpark.inspection.entity.InspectionPlan;
import com.crlink.smartpark.inspection.mapper.InspectionPlanMapper;
import com.crlink.smartpark.inspection.service.InspectionCircuitService;
import com.crlink.smartpark.inspection.service.InspectionEmployeeRelationService;
import com.crlink.smartpark.inspection.service.InspectionPlanService;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.inspection.common.Constant.EmployeeRelationEnum.INSPECTION_PLAN;
import static com.crlink.smartpark.inspection.common.Constant.InspectionPlanStatus.*;

/**
 * <p>
 * 巡检计划 服务实现类
 * </p>
 * */
@Service
public class InspectionPlanServiceImpl extends ServiceImpl<InspectionPlanMapper, InspectionPlan> implements InspectionPlanService {

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private InspectionCircuitService inspectionCircuitService;

    @Autowired
    private InspectionEmployeeRelationService inspectionEmployeeRelationService;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<InspectionPlan, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> id2ValueMap = this.lambdaQuery()
                .select(InspectionPlan::getId, sValue)
                .in(InspectionPlan::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(InspectionPlan::getId, sValue));
        return id2ValueMap;
    }

    @Override
    public Map<String, PlanCircuitEntity> queryCircuitByPlanId(Collection<String> planIds) {
        if (ObjectUtil.isEmpty(planIds)) {
            return Collections.emptyMap();
        }
        List<PlanCircuitEntity> list = baseMapper.queryCircuitByPlanId(planIds);
        return list.parallelStream().collect(Collectors.toMap(PlanCircuitEntity::getPlanId, Function.identity()));
    }

    @Override
    public Boolean saveOrUpdatePlan(InspectionPlanReqDTO reqDTO) {
        if (reqDTO.getEffectDateEnd().isBefore(LocalDate.now())) {
            throw new ResolinkRuntimeException("200004");
        }
        InspectionPlan plan = MyBeanUtils.copy(reqDTO, InspectionPlan::new);
        EntityUserUtil.setAllDynamic(plan);
        return this.saveOrUpdate(plan);
    }


    @Override
    public PageInfo<InspectionPlanDTO> listPlanPage(PlanQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        Integer status = query.getStatus();
        LambdaQueryChainWrapper<InspectionPlan> wrapperQuery = this.lambdaQuery();
        if (Objects.nonNull(status)) {
            LocalDate now = LocalDate.now();
            if (status.equals(CLOSED.status())) {
                wrapperQuery.and(wrapper -> wrapper.eq(InspectionPlan::getStatus, status)
                        .or().lt(InspectionPlan::getEffectDateEnd, now));
            } else if (status.equals(OPENING.status())) {
                wrapperQuery.eq(InspectionPlan::getStatus, status)
                        .ge(InspectionPlan::getEffectDateEnd, now);
            }
        }
        wrapperQuery.in(InspectionPlan::getParkOrgId, parkOrgIds)
                .like(StringUtils.hasText(query.getPlanName()), InspectionPlan::getPlanName, query.getPlanName())
//                .eq(null != status, InspectionPlan::getStatus, status)
                .ne(InspectionPlan::getStatus, DELETED.status())
                .orderByDesc(InspectionPlan::getCreateTime);
        query.startPage();
        List<InspectionPlan> plans = wrapperQuery.list();
        MyPageInfoUtils.isEmptyPage(plans);
        List<InspectionPlanDTO> dtoList = convert(plans, InspectionPlanDTO::new);
        return query.createPageInfo(dtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePlanStatus(String id, Integer status) {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        boolean update = this.lambdaUpdate().eq(InspectionPlan::getId, id)
                .set(InspectionPlan::getStatus, status)
                .set(InspectionPlan::getUpdateBy, userId).update();
        return update;
    }


    /**
     * 巡检计划关联巡检人
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean planRelateEmployees(PlanRelateEmployeeReqDTO dto) {
        inspectionEmployeeRelationService.remove(Wrappers.<InspectionEmployeeRelation>lambdaQuery()
                .eq(InspectionEmployeeRelation::getTargetId, dto.getPlanId())
                .eq(InspectionEmployeeRelation::getType, INSPECTION_PLAN.type()));
        List<InspectionEmployeeRelation> employeeRelations = dto.getEmployeeIds().parallelStream().map(t -> {
            InspectionEmployeeRelation relation = new InspectionEmployeeRelation();
            relation.setType(INSPECTION_PLAN.type());
            relation.setTargetId(dto.getPlanId());
            relation.setEmployeeId(t);
            return relation;
        }).collect(Collectors.toList());
        return inspectionEmployeeRelationService.saveBatch(employeeRelations);
    }

    @Override
    public InspectionPlanDTO findById(String id) {
        InspectionPlan plan = this.getById(id);
        InspectionPlanDTO planDTO = convert(Lists.newArrayList(plan), InspectionPlanDTO::new).get(0);
        return planDTO;
    }


    private List<InspectionPlanDTO> convert(List<InspectionPlan> plans, Supplier<InspectionPlanDTO> supplier) {
        Set<String> circuitIds = Sets.newHashSet();
        Set<String> planIds = Sets.newHashSet();
        Set<String> parkOrgIds = plans.parallelStream().map(t -> {
            planIds.add(t.getId());
            circuitIds.add(t.getCircuitId());
            return t.getParkOrgId();
        }).collect(Collectors.toSet());
        Map<String, Set<String>> planId2employeeIdsMap = inspectionEmployeeRelationService.getEmployeeIds(INSPECTION_PLAN.type, planIds);
        Set<String> employeeIds = planId2employeeIdsMap.values().parallelStream().flatMap(e -> e.stream()).collect(Collectors.toSet());
        Map<String, String> employeeMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        Map<String, String> circuitId2NameMap = (Map<String, String>) inspectionCircuitService.id2ValueMap(circuitIds, InspectionCircuit::getCircuitName);
        Map<String, String> parkOrgNameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        List<InspectionPlanDTO> planDTOs = MyBeanUtils.copyList(plans, supplier, (s, t) -> {
            t.setExecutionTimeStart(InspectionPlanDTO.format(s.getExecutionTimeStart()));
            t.setExecutionTimeEnd(InspectionPlanDTO.format(s.getExecutionTimeEnd()));
            t.setParkOrgName(parkOrgNameMap.get(s.getParkOrgId()));
            t.setCircuitName(circuitId2NameMap.get(s.getCircuitId()));
            List<DictVO> employees = planId2employeeIdsMap.getOrDefault(s.getId(), Collections.emptySet()).parallelStream()
                    .map(e -> new DictVO(employeeMap.get(e), e)).collect(Collectors.toList());
            t.setEmployees(employees);
            t.setUpdateTime(s.getUpdateTime().toLocalDate());
            //当实际日期在生效日期的结束日期之后，计划状态自动更新为“关闭”。
            t.setStatus(LocalDate.now().isAfter(t.getEffectDateEnd()) ? CLOSED.status() : s.getStatus());

        });
        return planDTOs;

    }

}
