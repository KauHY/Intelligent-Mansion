package com.crlink.smartpark.common.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.MultistageCategoryEnum;
import com.crlink.smartpark.common.dto.MultistageCategoryDTO;
import com.crlink.smartpark.common.entity.MultistageCategory;
import com.crlink.smartpark.common.entity.MultistageCategoryRelation;
import com.crlink.smartpark.common.mapper.MultistageCategoryMapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.model.MultistageCategoryReqDTO;
import com.crlink.smartpark.common.model.MultistageCategoryUpdateReqDTO;
import com.crlink.smartpark.common.model.query.MultistageCategoryQuery;
import com.crlink.smartpark.common.service.MultistageCategoryRelationService;
import com.crlink.smartpark.common.service.MultistageCategoryService;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.customer.feign.EquipmentLedgerProvider;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.system.vo.TreeDictVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 多级分类表 服务实现类
 * */
@Service
public class MultistageCategoryServiceImpl extends ServiceImpl<MultistageCategoryMapper, MultistageCategory> implements MultistageCategoryService {

    @Autowired
    private CommonProvider commonProvider;
    @Autowired
    private EquipmentLedgerProvider equipmentLedgerProvider;
    @Autowired
    private MultistageCategoryRelationService multistageCategoryRelationService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MultistageCategory, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MultistageCategory> lambdaQuery = this.lambdaQuery().in(MultistageCategory::getId, ids);
        if (flag) {
            lambdaQuery.select(MultistageCategory::getId, sValue);
        }
        Stream<MultistageCategory> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MultistageCategory::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MultistageCategory::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveMultistageCategory(MultistageCategoryReqDTO reqDTO) {


        Integer hierarchy = null;
        if (StringUtils.isNotBlank(reqDTO.getParentId())) {
            MultistageCategoryDTO parent = this.getMultistageCategoryById(reqDTO.getParentId());
            MultistageCategoryEnum multistageCategoryByType = MultistageCategoryEnum.getMultistageCategoryByType(reqDTO.getType());
            hierarchy = parent.getHierarchy() + 1;
            AssertUtil.requireTrue(hierarchy <= multistageCategoryByType.getMaxHierarchy(), "10019");
            reqDTO.setExpand(hierarchy < multistageCategoryByType.getMaxHierarchy());
        }

        AssertUtil.requireTrue(this.count(Wrappers.<MultistageCategory>query().lambda()
                .eq(MultistageCategory::getParkOrgId, reqDTO.getParkOrgId())
                .eq(MultistageCategory::getType, reqDTO.getType())
                .eq(MultistageCategory::getHierarchy, Objects.nonNull(hierarchy) ? hierarchy : 0)
                .eq(MultistageCategory::getLabel, reqDTO.getLabel())) <= 0, "10021");

        MultistageCategory entity = MyBeanUtils.copy(reqDTO, MultistageCategory::new);
        if (Objects.nonNull(hierarchy)) {
            entity.setHierarchy(hierarchy);
        }
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean updateMultistageCategory(MultistageCategoryUpdateReqDTO reqDTO) {
        MultistageCategory multistageCategory = Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(Objects.equals(multistageCategory.getLabel(), reqDTO.getLabel())
                || this.count(Wrappers.<MultistageCategory>query().lambda()
                .eq(MultistageCategory::getParkOrgId, multistageCategory.getParkOrgId())
                .eq(MultistageCategory::getType, multistageCategory.getType())
                .eq(MultistageCategory::getHierarchy, multistageCategory.getHierarchy())
                .eq(MultistageCategory::getLabel, reqDTO.getLabel())) <= 0, "10021");
        MultistageCategory entity = MyBeanUtils.copy(reqDTO, MultistageCategory::new);
        EntityUserUtil.setAllDynamic(entity);
        return updateById(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MultistageCategoryDTO getMultistageCategoryById(String id) {
        MultistageCategory entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    @Override
    public TreeDictVO listMultistageCategoryTree(String parkOrgId, String categoryType) {

        Map<String, String> namesByIdsAndType = commonProvider.getNamesByIdsAndType(Sets.newHashSet(parkOrgId), SimpleNode.TypeEnum.PARK.getCode());
        TreeDictVO park = new TreeDictVO(parkOrgId, namesByIdsAndType.get(parkOrgId), SimpleNode.TypeEnum.PARK.getCode(), 1);
        LambdaQueryWrapper<MultistageCategory> queryWrapper = new LambdaQueryWrapper<MultistageCategory>();
        queryWrapper
                .select(MultistageCategory::getId, MultistageCategory::getParentId, MultistageCategory::getLabel, MultistageCategory::getHierarchy)
                .eq(MultistageCategory::getParkOrgId, parkOrgId)
                .eq(MultistageCategory::getType, categoryType)
                .orderByDesc(MultistageCategory::getLabel);
        Map<Integer, List<MultistageCategory>> hierarchyData = list(queryWrapper).stream().collect(Collectors.groupingBy(MultistageCategory::getHierarchy));
        if (CollectionUtil.isEmpty(hierarchyData)) {
            return park;
        }

        Map<String, TreeDictVO> id2TreeVoMap = Maps.newHashMap();
        for (int i = 0; i < hierarchyData.size(); i++) {
            List<MultistageCategory> multistageCategories = hierarchyData.get(i);

            for (MultistageCategory multistageCategory : multistageCategories) {
                Set<TreeDictVO> children = StringUtils.isNotBlank(multistageCategory.getParentId()) ?
                        id2TreeVoMap.get(multistageCategory.getParentId()).getChildren() : park.getChildren();
                TreeDictVO treeDictVO = new TreeDictVO(multistageCategory.getId(), multistageCategory.getLabel(), SimpleNode.TypeEnum.CATEGORY.code, multistageCategory.getHierarchy() + 2);
                children.add(treeDictVO);
                id2TreeVoMap.put(multistageCategory.getId(), treeDictVO);
            }
        }

        return park;
    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MultistageCategory> getConditionWrapper(MultistageCategoryQuery query) {
        LambdaQueryWrapper<MultistageCategory> queryWrapper = new LambdaQueryWrapper<MultistageCategory>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MultistageCategoryDTO> entitiesToDTOs(List<MultistageCategory> entities) {
        return MyBeanUtils.copyList(entities, MultistageCategoryDTO::new, (s, t) -> {

        });
    }


    @Override
    public Boolean hasSub(String id) {
        return this.lambdaQuery().eq(MultistageCategory::getParentId, id).count() > 0;
    }

    @Override
    public Boolean removeCategoryAndSub(String id) {
        MultistageCategory entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (Objects.nonNull(entity.getType()) && MultistageCategoryEnum.TZ.getType().equals(entity.getType())) {
            AssertUtil.requireTrue(Constants.ZERO < entity.getHierarchy() || !hasSub(entity.getId()), "60008");
            AssertUtil.requireTrue(Constants.ONE > entity.getHierarchy() || !hasAssociate(entity.getType(), entity.getId()), "60009");
        } else if (Objects.nonNull(entity.getType()) && MultistageCategoryEnum.ENTERPRISE_GROUP.getType().equals(entity.getType())) {
            AssertUtil.requireTrue(Constants.ZERO < entity.getHierarchy() || !hasSub(entity.getId()), "10024");
            AssertUtil.requireTrue(!hasAssociate(entity.getType(), entity.getId()), "10025");
        } else {
            AssertUtil.requireTrue(Constants.ZERO < entity.getHierarchy() || !hasSub(entity.getId()), "10024");
            AssertUtil.requireTrue(Constants.ONE > entity.getHierarchy() || !hasAssociate(entity.getType(), entity.getId()), "10025");
        }

        Set<String> allSubId = this.getAllSubId(id);
        allSubId.add(id);
        multistageCategoryRelationService.remove(Wrappers.<MultistageCategoryRelation>lambdaQuery().in(MultistageCategoryRelation::getCategoryId, allSubId));
        return this.removeByIds(allSubId);
    }

    @Override
    public Set<String> getAllSubId(String id) {
        Set<String> result = Sets.newHashSet();
        getSubId(id, result);
        return result;
    }

    public void getSubId(String parentId, Set<String> result) {
        Set<String> subIds = this.lambdaQuery()
                .select(MultistageCategory::getId)
                .eq(MultistageCategory::getParentId, parentId)
                .list().stream().map(MultistageCategory::getId).collect(Collectors.toSet());

        if (CollectionUtil.isEmpty(subIds)) {
            return;
        }

        result.addAll(subIds);
        for (String subId : subIds) {
            getSubId(subId, result);
        }

    }

    @Override
    public String getTzIdBySecondName(String name, String parkOrgId) {
        LambdaQueryWrapper<MultistageCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(MultistageCategory::getId);
        wrapper.eq(MultistageCategory::getParkOrgId, parkOrgId);
        wrapper.eq(MultistageCategory::getType, MultistageCategoryEnum.TZ.getType());
        wrapper.eq(MultistageCategory::getLabel, name);
        wrapper.isNotNull(MultistageCategory::getParentId);
        MultistageCategory preCategory = getOne(wrapper, false);
        return preCategory != null ? preCategory.getId() : "";
    }

    @Override
    public Boolean hasAssociate(String type, String id) {
        if (MultistageCategoryEnum.TZ.getType().equals(type)) {
            return equipmentLedgerProvider.hasAssociate(id).getData();
        } else if (MultistageCategoryEnum.ENTERPRISE_GROUP.getType().equals(type)) {
            return multistageCategoryRelationService.hasAssociate(id);
        } else {
            return multistageCategoryRelationService.hasAssociate(id);
        }
    }
}
