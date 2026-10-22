package com.crlink.smartpark.customer.service.impl;

import com.crlink.smartpark.customer.entity.MeetingScheduleTime;
import com.crlink.smartpark.customer.model.query.MeetingScheduleTimeQuery;
import com.crlink.smartpark.customer.model.MeetingScheduleTimeReqDTO;
import com.crlink.smartpark.customer.dto.MeetingScheduleTimeDTO;
import com.crlink.smartpark.customer.service.MeetingScheduleTimeService;
import com.crlink.smartpark.customer.mapper.MeetingScheduleTimeMapper;
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
 * 可预定时段管理 服务实现类
 * */
@Service
public class MeetingScheduleTimeServiceImpl extends ServiceImpl<MeetingScheduleTimeMapper, MeetingScheduleTime> implements MeetingScheduleTimeService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingScheduleTime, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingScheduleTime> lambdaQuery = this.lambdaQuery().in(MeetingScheduleTime::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingScheduleTime::getId, sValue);
        }
        Stream<MeetingScheduleTime> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingScheduleTime::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingScheduleTime::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeetingScheduleTime(MeetingScheduleTimeReqDTO reqDTO) {
        MeetingScheduleTime entity = MyBeanUtils.copy(reqDTO, MeetingScheduleTime::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MeetingScheduleTimeDTO getMeetingScheduleTimeById(String id) {
        MeetingScheduleTime entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<MeetingScheduleTimeDTO> listMeetingScheduleTimePage(MeetingScheduleTimeQuery query) {
        query.startPage();
        List<MeetingScheduleTime> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingScheduleTimeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingScheduleTime> getConditionWrapper(MeetingScheduleTimeQuery query) {
        LambdaQueryWrapper<MeetingScheduleTime> queryWrapper = new LambdaQueryWrapper<MeetingScheduleTime>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeetingScheduleTimeDTO> entitiesToDTOs(List<MeetingScheduleTime> entities) {
        return MyBeanUtils.copyList(entities, MeetingScheduleTimeDTO::new, (s, t) -> {

        });
    }
}
