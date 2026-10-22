package com.crlink.smartpark.system.service.impl;

import com.crlink.smartpark.system.entity.BusinessMenu;
import com.crlink.smartpark.system.model.query.BusinessMenuQuery;
import com.crlink.smartpark.system.model.BusinessMenuReqDTO;
import com.crlink.smartpark.system.dto.BusinessMenuDTO;
import com.crlink.smartpark.system.service.BusinessMenuService;
import com.crlink.smartpark.system.mapper.BusinessMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.EntityUserUtil;
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
 * 企业端菜单/按钮表 服务实现类
 * */
@Service
public class BusinessMenuServiceImpl extends ServiceImpl<BusinessMenuMapper, BusinessMenu> implements BusinessMenuService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BusinessMenu, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<BusinessMenu> lambdaQuery = this.lambdaQuery().in(BusinessMenu::getId, ids);
        if (flag) {
            lambdaQuery.select(BusinessMenu::getId, sValue);
        }
        Stream<BusinessMenu> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(BusinessMenu::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(BusinessMenu::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateBusinessMenu(BusinessMenuReqDTO reqDTO) {
        BusinessMenu entity = MyBeanUtils.copy(reqDTO, BusinessMenu::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public BusinessMenuDTO getBusinessMenuById(String id) {
        BusinessMenu entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    @Override
    public List<BusinessMenu> listBusinessMenu() {
        return this.lambdaQuery().orderByAsc(BusinessMenu::getCreateTime).list();
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<BusinessMenuDTO> listBusinessMenuPage(BusinessMenuQuery query) {
        query.startPage();
        List<BusinessMenu> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<BusinessMenuDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<BusinessMenu> getConditionWrapper(BusinessMenuQuery query) {
        LambdaQueryWrapper<BusinessMenu> queryWrapper = new LambdaQueryWrapper<BusinessMenu>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<BusinessMenuDTO> entitiesToDTOs(List<BusinessMenu> entities) {
        return MyBeanUtils.copyList(entities, BusinessMenuDTO::new, (s, t) -> {

        });
    }
}
