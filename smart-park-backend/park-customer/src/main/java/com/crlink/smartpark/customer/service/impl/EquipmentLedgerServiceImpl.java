package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.MultistageCategoryProvider;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.dto.EquipmentLedgerDTO;
import com.crlink.smartpark.customer.entity.EquipmentLedger;
import com.crlink.smartpark.customer.mapper.EquipmentLedgerMapper;
import com.crlink.smartpark.customer.model.dto.EquipmentLedgerExportDTO;
import com.crlink.smartpark.customer.model.dto.EquipmentLedgerImportDTO;
import com.crlink.smartpark.customer.model.dto.EquipmentLedgerReqDTO;
import com.crlink.smartpark.customer.model.query.EquipmentLedgerQuery;
import com.crlink.smartpark.customer.service.EquipmentLedgerService;
import com.crlink.smartpark.system.feign.BuildingProvider;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.SMALL_DATE_FORMAT;


/**
 * 设备台账 服务实现类
 * */
@Service
public class EquipmentLedgerServiceImpl extends ServiceImpl<EquipmentLedgerMapper, EquipmentLedger> implements EquipmentLedgerService {

    public static final String NO_PRE = "TZ";
    public static final String FILE_NAME = "设备台账";
    @Autowired
    private CommonProvider commonProvider;
    @Autowired
    private MultistageCategoryProvider multistageCategoryProvider;
    @Autowired
    private NumberProvider numberProvider;
    @Resource
    private BuildingProvider buildingProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<EquipmentLedger, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<EquipmentLedger> lambdaQuery = this.lambdaQuery().in(EquipmentLedger::getId, ids);
        if (flag) {
            lambdaQuery.select(EquipmentLedger::getId, sValue);
        }
        Stream<EquipmentLedger> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(EquipmentLedger::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(EquipmentLedger::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateEquipmentLedger(EquipmentLedgerReqDTO reqDTO) {
        if (StringUtils.isBlank(reqDTO.getId())) {
            AssertUtil.requireTrue(this.count(Wrappers.<EquipmentLedger>query().lambda()
                    .eq(EquipmentLedger::getParkOrgId, reqDTO.getParkOrgId())
                    .eq(EquipmentLedger::getName, reqDTO.getName())) <= 0, "60005");
            NumberSeqDTO numberSeq = numberProvider.getNumberSeq(NO_PRE);
            String no = NO_PRE +
                    numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)) +
                    numberSeq.getValue();
            reqDTO.setDeviceCode(no);
        } else {
            EquipmentLedger equipmentLedger = Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            AssertUtil.requireTrue(Objects.equals(equipmentLedger.getName(), reqDTO.getName())
                    || this.count(Wrappers.<EquipmentLedger>query().lambda()
                    .eq(EquipmentLedger::getParkOrgId, reqDTO.getParkOrgId())
                    .eq(EquipmentLedger::getName, reqDTO.getName())) <= 0, "60005");
        }


        EquipmentLedger entity = MyBeanUtils.copy(reqDTO, EquipmentLedger::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Id2objHandle
    @Override
    public EquipmentLedgerDTO getEquipmentLedgerById(String id) {
        EquipmentLedger entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Id2objHandle
    @Override
    public PageInfo<EquipmentLedgerDTO> listEquipmentLedgerPage(EquipmentLedgerQuery query) {
        query.startPage();
        List<EquipmentLedger> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<EquipmentLedgerDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void export(EquipmentLedgerQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)), this::entitiesToExportDTOs, FILE_NAME, EquipmentLedgerExportDTO.class);
    }

    @Override
    public FileImportErrorInfoDTO importEquipment(MultipartFile file, String parkOrgId) {
        AssertUtil.requireTrue(SystemUtil.getInstance().getParkOrgIds().contains(parkOrgId), "10020");
        List<DictVO> buildings = buildingProvider.listBuildingDictByParkOrgId(parkOrgId).getData();
        Map<String, Object> buildingMap = buildings.stream().collect(Collectors.toMap(DictVO::getLabel, DictVO::getValue));
        LambdaQueryWrapper<EquipmentLedger> wrapper = new LambdaQueryWrapper();
        wrapper.select(EquipmentLedger::getName);
        wrapper.eq(EquipmentLedger::getParkOrgId, parkOrgId);
        Set<String> namesSet = list(wrapper).stream().map(EquipmentLedger::getName).collect(Collectors.toSet());
        Map<String, String> categoryNameIdMap = new HashMap<>();
        ExcelImportRollbackListener<EquipmentLedgerImportDTO> importListener = new ExcelImportRollbackListener<>(EquipmentLedgerImportDTO.class,
                //业务参数校验方法
                e -> {
                    if (namesSet.contains(e.getName())) {
                        return Message.code("60005").getDisplayText();
                    } else {
                        namesSet.add(e.getName());
                    }
                    if (!buildingMap.containsKey(e.getBuildingName())) {
                        return Message.code("20024").getDisplayText();
                    }
                    if (categoryNameIdMap.containsKey(e.getCategoryName())) {
                        e.setCategoryId(categoryNameIdMap.get(e.getCategoryName()));
                    } else {
                        String categoryId = multistageCategoryProvider.getTzIdBySecondName(e.getCategoryName(), parkOrgId);
                        if (StrUtil.isBlank(categoryId)) {
                            return "设备分类不存在";
                        } else {
                            categoryNameIdMap.put(e.getCategoryName(), categoryId);
                            e.setCategoryId(categoryId);
                        }
                    }
                    return null;
                },
                //数据读取完成后操作
                resultList -> {
                    List<EquipmentLedger> list = MyBeanUtils.copyList(resultList, EquipmentLedger::new, (s, t) -> {
                        t.setParkOrgId(parkOrgId);
                        t.setBuildingId(buildingMap.get(s.getBuildingName()).toString());
                        EntityUserUtil.setAll(t);
                    });
                    saveBatch(list);
                });
        ExcelImportUtils.importExcel(file, EquipmentLedgerImportDTO.class, importListener);
        return importListener.getImportResult();
    }

    @Override
    public Boolean scrap(String id) {
        EquipmentLedger equipmentLedger = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        equipmentLedger.setState(DictConstant.EquipmentLedgerResult.SCRAPED.getCode());
        equipmentLedger.setScrapDate(LocalDate.now());
        return updateById(equipmentLedger);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<EquipmentLedger> getConditionWrapper(EquipmentLedgerQuery query) {
        LambdaQueryWrapper<EquipmentLedger> queryWrapper = new LambdaQueryWrapper<EquipmentLedger>();
        queryWrapper
                .in(EquipmentLedger::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), EquipmentLedger::getParkOrgId, query.getParkOrgId())
                .like(StringUtils.isNotBlank(query.getName()), EquipmentLedger::getName, query.getName())
                .eq(Objects.nonNull(query.getState()), EquipmentLedger::getState, query.getState())
                .orderByDesc(EquipmentLedger::getCreateTime);
        if (StringUtils.isNotBlank(query.getCategoryId())) {
            Set<String> allSubId = multistageCategoryProvider.getAllSubId(query.getCategoryId());
            allSubId = Objects.isNull(allSubId) ? Sets.newHashSet() : allSubId;
            allSubId.add(query.getCategoryId());
            queryWrapper.in(EquipmentLedger::getCategoryId, allSubId);
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<EquipmentLedgerDTO> entitiesToDTOs(List<EquipmentLedger> entities) {
        Set<String> categoryIds = entities.parallelStream().map(EquipmentLedger::getCategoryId).collect(Collectors.toSet());
        Map<String, String> categoryNameMap = multistageCategoryProvider.getNamesByIds(categoryIds);
        return MyBeanUtils.copyList(entities, EquipmentLedgerDTO::new,
                (s, t) -> t.setCategoryName(categoryNameMap.get(s.getCategoryId())));
    }

    private List<EquipmentLedgerExportDTO> entitiesToExportDTOs(List<EquipmentLedger> entities) {
        Set<String> buildingIds = Sets.newHashSet();
        Set<String> categoryIds = Sets.newHashSet();

        entities.forEach(e -> {
            buildingIds.add(e.getBuildingId());
            categoryIds.add(e.getCategoryId());
        });
        Map<String, String> buildingNameMap = commonProvider.getNamesByIdsAndType(buildingIds, SimpleNode.TypeEnum.BUILDING.getCode());
        Map<String, String> categoryNameMap = multistageCategoryProvider.getNamesByIds(categoryIds);
        return MyBeanUtils.copyList(entities, EquipmentLedgerExportDTO::new, (s, t) -> {
            t.setBuildingName(buildingNameMap.get(s.getBuildingId()));
            t.setCategory(categoryNameMap.get(s.getCategoryId()));
            t.setStateName(DictConstant.EquipmentLedgerResult.getEquipmentLedgerResultByCode(s.getState()));
        });
    }

    @Override
    public Boolean hasAssociate(String categoryId) {
        return this.lambdaQuery().eq(EquipmentLedger::getCategoryId, categoryId).count() > 0;
    }
}
