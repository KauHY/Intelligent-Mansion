package com.crlink.smartpark.system.service.impl;

import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.entity.CompanyInformation;
import com.crlink.smartpark.system.model.query.CompanyInformationQuery;
import com.crlink.smartpark.system.model.CompanyInformationReqDTO;
import com.crlink.smartpark.system.dto.CompanyInformationDTO;
import com.crlink.smartpark.system.service.CompanyInformationService;
import com.crlink.smartpark.system.mapper.CompanyInformationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.collection.CollectionUtil;

import java.util.Collections;

import com.google.common.collect.Lists;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 企业信息 服务实现类
 * */
@Service
public class CompanyInformationServiceImpl extends ServiceImpl<CompanyInformationMapper, CompanyInformation> implements CompanyInformationService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<CompanyInformation, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<CompanyInformation> lambdaQuery = this.lambdaQuery().in(CompanyInformation::getId, ids);
        if (flag) {
            lambdaQuery.select(CompanyInformation::getId, sValue);
        }
        Stream<CompanyInformation> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(CompanyInformation::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(CompanyInformation::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateCompanyInformation(CompanyInformationReqDTO reqDTO) {
        CompanyInformation entity = MyBeanUtils.copy(reqDTO, CompanyInformation::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public CompanyInformationDTO getCompanyInformationById(String id) {
        CompanyInformation entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<CompanyInformationDTO> listCompanyInformationPage(CompanyInformationQuery query) {
        query.startPage();
        List<CompanyInformation> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<CompanyInformationDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<CompanyInformation> getConditionWrapper(CompanyInformationQuery query) {
        LambdaQueryWrapper<CompanyInformation> queryWrapper = new LambdaQueryWrapper<CompanyInformation>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<CompanyInformationDTO> entitiesToDTOs(List<CompanyInformation> entities) {
        return MyBeanUtils.copyList(entities, CompanyInformationDTO::new, (s, t) -> {

        });
    }

    @Override
    public Set<String> listCodeByUserId() {
        return this.lambdaQuery().select(CompanyInformation::getCode)
                .eq(CompanyInformation::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId())
                .list().stream().map(CompanyInformation::getCode).collect(Collectors.toSet());
    }
}
