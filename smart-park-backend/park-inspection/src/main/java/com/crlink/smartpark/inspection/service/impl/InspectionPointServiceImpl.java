package com.crlink.smartpark.inspection.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.inspection.controller.request.InspectionPointReqDTO;
import com.crlink.smartpark.inspection.controller.request.PointQuery;
import com.crlink.smartpark.inspection.dto.InspectionPointDTO;
import com.crlink.smartpark.inspection.entity.CircuitPointRelation;
import com.crlink.smartpark.inspection.entity.InspectionPlan;
import com.crlink.smartpark.inspection.entity.InspectionPoint;
import com.crlink.smartpark.inspection.mapper.InspectionPointMapper;
import com.crlink.smartpark.inspection.model.InspectionPointExcelExportDTO;
import com.crlink.smartpark.inspection.model.InspectionPointExcelImportDTO;
import com.crlink.smartpark.inspection.service.CircuitPointRelationService;
import com.crlink.smartpark.inspection.service.InspectionPlanService;
import com.crlink.smartpark.inspection.service.InspectionPointService;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.inspection.common.Constant.INSPECTION_POINT_EXPORT_FILENAME;
import static com.crlink.smartpark.inspection.common.Constant.InspectionPlanStatus.OPENING;

/**
 * <p>
 * 巡检点表 服务实现类
 * </p>
 * */
@Service
public class InspectionPointServiceImpl extends ServiceImpl<InspectionPointMapper, InspectionPoint> implements InspectionPointService {

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private InspectionPlanService inspectionPlanService;
    @Autowired
    private CircuitPointRelationService circuitPointRelationService;

    @Override
    public Boolean saveOrUpdatePoint(InspectionPointReqDTO reqDTO) {

        String id = reqDTO.getId();
        if (StringUtils.hasText(id)) {
            //当巡检点被巡检计划关联时，巡检计划状态为“开启”，该巡检点无法编辑
            //首先查询出巡检点对应的巡检路线
            Set<String> circuitIds = circuitPointRelationService.pointId2CircuitIds(Sets.newHashSet(id)).get(id);
            //然后根据巡检路线查询其关联的所有巡检计划是否存在状态为开启的
            if (CollectionUtil.isNotEmpty(circuitIds)) {
                Integer count = inspectionPlanService.lambdaQuery().select(InspectionPlan::getStatus)
                        .eq(InspectionPlan::getStatus, OPENING.status())
                        .in(InspectionPlan::getCircuitId, circuitIds).count();
                if (count > 0) {
                    throw new ResolinkRuntimeException("200003");
                }
            }
        }
        checkPoint(reqDTO);
        InspectionPoint point = MyBeanUtils.copy(reqDTO, InspectionPoint::new);
        EntityUserUtil.setAllDynamic(point);
        return this.saveOrUpdate(point);
    }

    /**
     * 删除巡检点
     * 当巡检点被巡检计划关联时，且巡检计划状态为“开启”，
     * 该巡检点无法删除，提示“巡检点所关联的巡检计划已被开启（即计划状态为开时），如需删除此巡检点，请先关闭巡检计划”。
     * 22.7.5 v18 当巡检点被巡检路线关联时，该巡检点无法删除，提示“巡检点已被巡检路线关联，无法删除“；
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletedPointById(String id) {
        CircuitPointRelation one = circuitPointRelationService.lambdaQuery().select(CircuitPointRelation::getId, CircuitPointRelation::getPointId)
                .eq(CircuitPointRelation::getPointId, id).last("limit 1").one();
        if (Objects.nonNull(one)) {
            throw new ResolinkRuntimeException("200005");
        }
        LambdaQueryWrapper<CircuitPointRelation> removeQuery = Wrappers.<CircuitPointRelation>query().lambda()
                .eq(CircuitPointRelation::getPointId, id);
        circuitPointRelationService.remove(removeQuery);
        return this.removeById(id);
    }

    @Override
    public PageInfo<InspectionPointDTO> listPointPage(PointQuery pointQuery) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(pointQuery.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryChainWrapper<InspectionPoint> wrapper = getWrapper(pointQuery, parkOrgIds);
        pointQuery.startPage();
        List<InspectionPoint> points = wrapper.list();
        MyPageInfoUtils.isEmptyPage(points);
        List<InspectionPointDTO> dtoList = convert(points, InspectionPointDTO::new);
        return pointQuery.createPageInfo(dtoList);
    }

    /**
     * 导出
     *
     * @param pointQuery
     */
    @Override
    public void export(PointQuery pointQuery) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(pointQuery.getOrgId());
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            parkOrgIds.add("-1");
        }
        LambdaQueryChainWrapper<InspectionPoint> wrapper = getWrapper(pointQuery, parkOrgIds);
        ExcelExportUtils.export(wrapper::list,
                sourceList -> MyBeanUtils.copyList(sourceList, InspectionPointExcelExportDTO::new, (s, t) -> {
                    t.setUpdateTime(s.getUpdateTime().toLocalDate());
                }),
                INSPECTION_POINT_EXPORT_FILENAME, InspectionPointExcelExportDTO.class);
    }

    @Override
    public FileImportErrorInfoDTO importPoints(MultipartFile file, String parkOrgId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<InspectionPointExcelImportDTO> importListener = new ExcelImportRollbackListener<>(InspectionPointExcelImportDTO.class,
                    //业务参数校验方法,并单行保存数据
                    t -> validAndOperateExcelDTO(t, parkOrgId),
                    null
            );
            ExcelImportUtils.importExcel(file, InspectionPointExcelImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }

    /**
     * v12:同一园区下，若巡检点名称、巡检点类型均相同，则以待导入的数据为准更新系统已有数据
     * v13:导入规则调整：同一园区下，若巡检点名称、巡检点类型均相同，则以系统已有数据为准，对应行将不做导入处理。
     *
     * @param dto
     * @param parkOrgId
     * @return
     */
    private String validAndOperateExcelDTO(InspectionPointExcelImportDTO dto, String parkOrgId) {
        dto.setParkOrgId(parkOrgId);
        //同一园区、同一楼栋下，若楼层房号名称相同，则以待导入的数据为准更新系统已有数据。
        if (checkInspectionPoint(dto)) {
            return "该巡检点已存在！";
        }
        InspectionPoint afterPoint = MyBeanUtils.copy(dto, InspectionPoint::new);
        EntityUserUtil.setAllDynamic(afterPoint);
        return this.saveOrUpdate(afterPoint) ? null : "插入数据失败！";
    }

    private Boolean checkInspectionPoint(InspectionPointExcelImportDTO dto) {
        InspectionPoint one = this.lambdaQuery().select(InspectionPoint::getId).eq(InspectionPoint::getType, dto.getType()).eq(InspectionPoint::getPointName, dto.getPointName())
                .eq(InspectionPoint::getParkOrgId, dto.getParkOrgId()).one();
//        if (Objects.nonNull(one)) {
//            dto.setId(one.getId());
//        }
        return Objects.nonNull(one);
    }

    /**
     * 封装查询条件
     *
     * @param pointQuery
     * @param parkOrgIds
     * @return
     */
    private LambdaQueryChainWrapper<InspectionPoint> getWrapper(PointQuery pointQuery, Set<String> parkOrgIds) {
        LambdaQueryChainWrapper<InspectionPoint> wrapper = this.lambdaQuery()
                .in(InspectionPoint::getParkOrgId, parkOrgIds)
                .like(StringUtils.hasText(pointQuery.getPointName()), InspectionPoint::getPointName, pointQuery.getPointName())
                .eq(null != pointQuery.getType(), InspectionPoint::getType, pointQuery.getType())
                .orderByDesc(InspectionPoint::getCreateTime);
        return wrapper;
    }

    public List<InspectionPointDTO> convert(List<InspectionPoint> points, Supplier<InspectionPointDTO> supplier) {
        Set<String> parkOrgIds = points.parallelStream().map(InspectionPoint::getParkOrgId).collect(Collectors.toSet());
        Map<String, String> parkOrgNameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        List<InspectionPointDTO> pointDTOs = MyBeanUtils.copyList(points, supplier, (s, t) -> {
            t.setUpdateTime(s.getUpdateTime().toLocalDate());
            t.setParkOrgName(parkOrgNameMap.get(s.getParkOrgId()));
        });
        return pointDTOs;
    }

    /**
     * 巡检点名称、巡检点类型、巡检位置三者的名称需做唯一性校验，若同时已存在，提示“该巡检点已存在”。
     *
     * @param reqDTO
     */
    private void checkPoint(InspectionPointReqDTO reqDTO) {
        Integer count = this.lambdaQuery().select(InspectionPoint::getId).eq(InspectionPoint::getPointName, reqDTO.getPointName())
                .eq(InspectionPoint::getPointName, reqDTO.getPointName())
                .eq(InspectionPoint::getType, reqDTO.getType())
                .eq(InspectionPoint::getLocation, reqDTO.getLocation())
                .ne(StringUtils.hasText(reqDTO.getId()), InspectionPoint::getId, reqDTO.getId())
                .count();
        if (count >= 1) {
            throw new ResolinkRuntimeException("200001");
        }
    }
}
