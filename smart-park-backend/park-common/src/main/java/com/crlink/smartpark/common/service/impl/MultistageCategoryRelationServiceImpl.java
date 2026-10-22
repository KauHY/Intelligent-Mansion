package com.crlink.smartpark.common.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationQueryDTO;
import com.crlink.smartpark.common.entity.MultistageCategoryRelation;
import com.crlink.smartpark.common.model.MultistageCategoryRelationDelDTO;
import com.crlink.smartpark.common.model.query.MultistageCategoryRelationQuery;
import com.crlink.smartpark.common.model.MultistageCategoryRelationReqDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationDTO;
import com.crlink.smartpark.common.service.MultistageCategoryRelationService;
import com.crlink.smartpark.common.mapper.MultistageCategoryRelationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.collection.CollectionUtil;

import java.time.LocalDateTime;
import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 多级分类关联表 服务实现类
 * */
@Service
public class MultistageCategoryRelationServiceImpl extends ServiceImpl<MultistageCategoryRelationMapper, MultistageCategoryRelation> implements MultistageCategoryRelationService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MultistageCategoryRelation, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MultistageCategoryRelation> lambdaQuery = this.lambdaQuery().in(MultistageCategoryRelation::getId, ids);
        if (flag) {
            lambdaQuery.select(MultistageCategoryRelation::getId, sValue);
        }
        Stream<MultistageCategoryRelation> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MultistageCategoryRelation::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MultistageCategoryRelation::getId, Function.identity()));
        }
        return result;
    }


    /**
     * 通过id查询实体
     */
    @Override
    public MultistageCategoryRelationDTO getMultistageCategoryRelationById(String id) {
        MultistageCategoryRelation entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<MultistageCategoryRelationDTO> listMultistageCategoryRelationPage(MultistageCategoryRelationQuery query) {
        query.startPage();
        List<MultistageCategoryRelation> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<MultistageCategoryRelationDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MultistageCategoryRelation> getConditionWrapper(MultistageCategoryRelationQuery query) {
        LambdaQueryWrapper<MultistageCategoryRelation> queryWrapper = new LambdaQueryWrapper<MultistageCategoryRelation>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MultistageCategoryRelationDTO> entitiesToDTOs(List<MultistageCategoryRelation> entities) {
        return MyBeanUtils.copyList(entities, MultistageCategoryRelationDTO::new, (s, t) -> {

        });
    }

    @Override
    public Boolean associate(MultistageCategoryRelationReqDTO reqDTO) {
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();

        Set<String> newBusinessId = CollectionUtil.isEmpty(reqDTO.getBusinessIds()) ? Sets.newHashSet() :reqDTO.getBusinessIds();
        if (StringUtils.isNotBlank(reqDTO.getBusinessId())) {
            newBusinessId.add(reqDTO.getBusinessId());
        }
        List<MultistageCategoryRelation> list = this.lambdaQuery().eq(MultistageCategoryRelation::getCategoryId, reqDTO.getCategoryId()).list();
        if (CollectionUtil.isNotEmpty(list)) {
            LocalDateTime now = LocalDateTime.now();
            Set<String> existBusinessIds = list.stream().map(MultistageCategoryRelation::getBusinessId).collect(Collectors.toSet());
            if (!Objects.equals(reqDTO.getRemark(), list.get(0).getRemark())) {
                list.forEach(e -> {
                    e.setRemark(reqDTO.getRemark());
                    e.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
                    e.setUpdateTime(now);
                });
                this.updateBatchById(list);
            }
            newBusinessId.removeAll(existBusinessIds);
        }


        List<MultistageCategoryRelation> collect = newBusinessId.stream().map(categoryId ->
                new MultistageCategoryRelation(reqDTO.getCategoryId(), categoryId, reqDTO.getParkOrgId(),reqDTO.getRemark(), currentUserId, currentUserId)
        ).collect(Collectors.toList());
        return this.saveBatch(collect);
    }

    @Override
    public Boolean disassociate(MultistageCategoryRelationDelDTO reqDTO) {
        LambdaQueryWrapper<MultistageCategoryRelation> queryWrapper = new LambdaQueryWrapper<MultistageCategoryRelation>();
        queryWrapper.eq(MultistageCategoryRelation::getBusinessId, reqDTO.getBusinessId())
                .eq(StringUtils.isNotBlank(reqDTO.getCategoryId()), MultistageCategoryRelation::getCategoryId, reqDTO.getCategoryId());
        return remove(queryWrapper);
    }

    @Override
    public Boolean hasAssociate(String categoryId) {
        return this.lambdaQuery().eq(MultistageCategoryRelation::getCategoryId, categoryId).count() > 0;
    }

    @Override
    @Id2objHandle
    public PageInfo<MultistageCategoryRelationDTO> listPageBusinessToNCategoryRelation(MultistageCategoryRelationQueryDTO query) {
        query.startPage();
        List<MultistageCategoryRelationDTO> list = baseMapper.listBusinessToNCategoryRelation(query);
        list.forEach(e -> {
            e.setCategoryIds(e.getCategoryId().contains(",") ? Lists.newArrayList(e.getCategoryId().split(",")) : Lists.newArrayList(e.getCategoryId()));
        });
        return query.createPageInfo(list);
    }

    @Override
    @Id2objHandle
    public PageInfo<MultistageCategoryRelationDTO> listPageBusinessToCategoryRelation(MultistageCategoryRelationQueryDTO query) {
        query.startPage();
        List<MultistageCategoryRelationDTO> list = entitiesToDTOs(this.lambdaQuery().eq(StringUtils.isNotBlank(query.getCategoryId()), MultistageCategoryRelation::getCategoryId, query.getCategoryId())
                .eq(StringUtils.isNotBlank(query.getBusinessId()), MultistageCategoryRelation::getBusinessId, query.getBusinessId())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), MultistageCategoryRelation::getParkOrgId, query.getParkOrgId())
                .orderByDesc(MultistageCategoryRelation::getCreateTime)
                .list());
        return query.createPageInfo(list);
    }


    @Override
    public MultistageCategoryRelationDTO getMultistageCategoryRelation(String businessId, String categoryId) {
        MultistageCategoryRelation one = lambdaQuery().eq(MultistageCategoryRelation::getCategoryId, categoryId)
                .eq(MultistageCategoryRelation::getBusinessId, businessId)
                .last("limit 1")
                .one();
        return MyBeanUtils.copy(one, MultistageCategoryRelationDTO::new);
    }

    @Override
    public Boolean saveOrUpdateMultistageCategoryRelation(MultistageCategoryRelationReqDTO reqDTO) {
        MultistageCategoryRelation entity = MyBeanUtils.copy(reqDTO, MultistageCategoryRelation::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    @Override
    @Id2objHandle
    public MultistageCategoryRelationDTO getAllRelationByCategory(String categoryId) {
        List<MultistageCategoryRelation> list = lambdaQuery().eq(MultistageCategoryRelation::getCategoryId, categoryId).list();
        MultistageCategoryRelationDTO multistageCategoryRelationDTO = new MultistageCategoryRelationDTO();
        if (CollectionUtil.isNotEmpty(list)){
            List<String> businessIds = list.stream().map(MultistageCategoryRelation::getBusinessId).collect(Collectors.toList());
            multistageCategoryRelationDTO.setBusinessIds(businessIds);
            multistageCategoryRelationDTO.setCategoryId(categoryId);
            multistageCategoryRelationDTO.setParkOrgId(list.get(0).getParkOrgId());
            multistageCategoryRelationDTO.setRemark(list.get(0).getRemark());
        }
        return multistageCategoryRelationDTO;
    }
}
