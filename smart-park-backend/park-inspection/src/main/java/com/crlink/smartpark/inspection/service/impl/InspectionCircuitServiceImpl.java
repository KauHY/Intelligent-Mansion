package com.crlink.smartpark.inspection.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.inspection.controller.request.CircuitQuery;
import com.crlink.smartpark.inspection.controller.request.CircuitRelatePointReqDTO;
import com.crlink.smartpark.inspection.controller.request.InspectionCircuitReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionCircuitDTO;
import com.crlink.smartpark.inspection.dto.InspectionPointDTO;
import com.crlink.smartpark.inspection.dto.resultmap.CircuitPointEntity;
import com.crlink.smartpark.inspection.entity.CircuitPointRelation;
import com.crlink.smartpark.inspection.entity.InspectionCircuit;
import com.crlink.smartpark.inspection.entity.InspectionPlan;
import com.crlink.smartpark.inspection.mapper.InspectionCircuitMapper;
import com.crlink.smartpark.inspection.service.CircuitPointRelationService;
import com.crlink.smartpark.inspection.service.InspectionCircuitService;
import com.crlink.smartpark.inspection.service.InspectionPlanService;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.inspection.common.Constant.InspectionPlanStatus.OPENING;

/**
 * <p>
 * 巡检路线 服务实现类
 * </p>
 * */
@Service
public class InspectionCircuitServiceImpl extends ServiceImpl<InspectionCircuitMapper, InspectionCircuit> implements InspectionCircuitService {
    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private CircuitPointRelationService circuitPointRelationService;

    @Autowired
    private InspectionPlanService inspectionPlanService;


    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<InspectionCircuit, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> id2ValueMap = this.lambdaQuery()
                .select(InspectionCircuit::getId, sValue)
                .in(InspectionCircuit::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(InspectionCircuit::getId, sValue));
        return id2ValueMap;
    }

    @Override
    public Map<String, List<InspectionPointDTO>> queryPointByCircuitIds(Collection<String> circuitIds) {
        if (ObjectUtil.isEmpty(circuitIds)) {
            return Collections.emptyMap();
        }
        List<CircuitPointEntity> list = baseMapper.queryPointByCircuitIds(circuitIds);
        Map<String, List<InspectionPointDTO>> circuitId2PointsMap = list.parallelStream()
                .collect(Collectors.toMap(CircuitPointEntity::getCircuitId, CircuitPointEntity::getPoints));
        return circuitId2PointsMap;
    }

    @Override
    public Boolean saveOrUpdateCircuit(InspectionCircuitReqDTO reqDTO) {
        InspectionCircuit circuit = MyBeanUtils.copy(reqDTO, InspectionCircuit::new);
        EntityUserUtil.setAllDynamic(circuit);
        String circuitId = reqDTO.getId();
        if (StringUtils.hasText(circuitId) && getCircuitRelatedOpeningStatusPlan(circuitId) > 0) {
            throw new ResolinkRuntimeException("200002");
        }
        return this.saveOrUpdate(circuit);
    }

    private Integer getCircuitRelatedOpeningStatusPlan(String circuitId) {
        Integer planCount = inspectionPlanService.lambdaQuery().eq(InspectionPlan::getCircuitId, circuitId)
                .eq(InspectionPlan::getStatus, OPENING.status()).count();
        return planCount;
    }


    @Override
    public PageInfo<InspectionCircuitDTO> listCircuitPage(CircuitQuery circuitQuery) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(circuitQuery.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        circuitQuery.startPage();
        List<InspectionCircuit> circuits = this.lambdaQuery().in(InspectionCircuit::getParkOrgId, parkOrgIds)
                .like(StringUtils.hasText(circuitQuery.getCircuitName()), InspectionCircuit::getCircuitName, circuitQuery.getCircuitName())
                .eq(null != circuitQuery.getType(), InspectionCircuit::getType, circuitQuery.getType())
                .orderByDesc(InspectionCircuit::getCreateTime)
                .list();
        MyPageInfoUtils.isEmptyPage(circuits);
        List<InspectionCircuitDTO> dtoList = convert(circuits, InspectionCircuitDTO::new);
        return circuitQuery.createPageInfo(dtoList);
    }

    @Override
    @Transactional
    public Boolean deleteCircuit(String id) {
        //当巡检路线被巡检计划关联时，巡检计划状态为“开启”，该巡检路线无法删除，提示“巡检路线所关联的巡检计划已被开启（即计划状态为开时）
        Integer planCount = getCircuitRelatedOpeningStatusPlan(id);
        if (planCount > 0) {
            throw new ResolinkRuntimeException("200002");
        }
        circuitPointRelationService.remove(Wrappers.<CircuitPointRelation>lambdaQuery().eq(CircuitPointRelation::getCircuitId, id));
        return this.removeById(id);
    }

    @Override
    public InspectionCircuitDTO getInspectionCircuitById(String id) {
        InspectionCircuit circuit = this.getById(id);
        InspectionCircuitDTO circuitDTO = convert(Lists.newArrayList(circuit), InspectionCircuitDTO::new).get(0);
        return circuitDTO;
    }

    @Override
    @Transactional
    public Boolean circuitRelatePoints(CircuitRelatePointReqDTO dto) {
        if (CollectionUtil.isNotEmpty(dto.getPointIds())) {
            circuitPointRelationService.remove(Wrappers.<CircuitPointRelation>lambdaQuery().eq(CircuitPointRelation::getCircuitId, dto.getCircuitId()));
        }
        List<CircuitPointRelation> points = Lists.newArrayList();
        List<String> pointIds = dto.getPointIds();
        for (int i = 0; i < pointIds.size(); i++) {
            CircuitPointRelation pointRelation = new CircuitPointRelation();
            pointRelation.setCircuitId(dto.getCircuitId());
            pointRelation.setPointId(pointIds.get(i));
            pointRelation.setSort(i + 1);
            points.add(pointRelation);
        }
        return circuitPointRelationService.saveBatch(points);
    }

    @Override
    @Transactional
    public List<InspectionCircuit> test(InspectionCircuitReqDTO reqDTO) {
        this.saveOrUpdateCircuit(reqDTO);
        return this.list();
    }

    /**
     * v18 创建巡检计划时，仅调用已关联巡检点的巡检路线
     *
     * @param parkOrgId
     * @param type
     * @return
     */
    @Override
    public List<DictVO> selectCircuits(String parkOrgId, Integer type) {
        Set<String> hasPointCircuitIds = circuitPointRelationService.lambdaQuery().select(CircuitPointRelation::getId, CircuitPointRelation::getCircuitId)
                .list().parallelStream().map(CircuitPointRelation::getCircuitId).collect(Collectors.toSet());
        List<DictVO> result = this.lambdaQuery().select(InspectionCircuit::getId, InspectionCircuit::getCircuitName)
                .eq(InspectionCircuit::getParkOrgId, parkOrgId)
                .eq(InspectionCircuit::getType, type).list()
                .parallelStream().filter(t -> hasPointCircuitIds.contains(t.getId()))
                .map(t -> new DictVO(t.getCircuitName(), t.getId())).collect(Collectors.toList());
        return result;
    }

    public List<InspectionCircuitDTO> convert(List<InspectionCircuit> circuits, Supplier<InspectionCircuitDTO> supplier) {
        Set<String> circuitIds = Sets.newHashSet();
        Set<String> parkOrgIds = circuits.parallelStream().map(t -> {
            circuitIds.add(t.getId());
            return t.getParkOrgId();
        }).collect(Collectors.toSet());
        Map<String, List<InspectionPointDTO>> circuitId2PointIdsMap = this.queryPointByCircuitIds(circuitIds);
        Map<String, String> parkOrgNameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        List<InspectionCircuitDTO> result = MyBeanUtils.copyList(circuits, supplier, (s, t) -> {
            t.setParkOrgName(parkOrgNameMap.get(t.getParkOrgId()));
            t.setPoints(circuitId2PointIdsMap.get(s.getId()));
            t.setUpdateTime(s.getUpdateTime().toLocalDate());
        });
        return result;
    }
}
