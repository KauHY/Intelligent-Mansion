package com.crlink.smartpark.customer.service.impl;

import com.crlink.smartpark.customer.entity.MeetingPermission;
import com.crlink.smartpark.customer.model.query.MeetingPermissionQuery;
import com.crlink.smartpark.customer.model.MeetingPermissionReqDTO;
import com.crlink.smartpark.customer.dto.MeetingPermissionDTO;
import com.crlink.smartpark.customer.service.MeetingPermissionService;
import com.crlink.smartpark.customer.mapper.MeetingPermissionMapper;
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
 * 会议室权限关联 服务实现类
 * */
@Service
public class MeetingPermissionServiceImpl extends ServiceImpl<MeetingPermissionMapper, MeetingPermission> implements MeetingPermissionService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingPermission, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingPermission> lambdaQuery = this.lambdaQuery().in(MeetingPermission::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingPermission::getId, sValue);
        }
        Stream<MeetingPermission> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingPermission::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingPermission::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeetingPermission(MeetingPermissionReqDTO reqDTO) {
        MeetingPermission entity = MyBeanUtils.copy(reqDTO, MeetingPermission::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MeetingPermissionDTO getMeetingPermissionById(String id) {
        MeetingPermission entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<MeetingPermissionDTO> listMeetingPermissionPage(MeetingPermissionQuery query) {
        query.startPage();
        List<MeetingPermission> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingPermissionDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingPermission> getConditionWrapper(MeetingPermissionQuery query) {
        LambdaQueryWrapper<MeetingPermission> queryWrapper = new LambdaQueryWrapper<MeetingPermission>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeetingPermissionDTO> entitiesToDTOs(List<MeetingPermission> entities) {
        return MyBeanUtils.copyList(entities, MeetingPermissionDTO::new, (s, t) -> {

        });
    }
}
