package com.crlink.smartpark.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.meeting.dto.MeetingPermissionDTO;
import com.crlink.smartpark.meeting.entity.MeetingPermission;
import com.crlink.smartpark.meeting.entity.MeetingReview;
import com.crlink.smartpark.meeting.controller.request.query.MeetingReviewQuery;
import com.crlink.smartpark.meeting.model.dto.MeetingReviewReqDTO;
import com.crlink.smartpark.meeting.dto.MeetingReviewDTO;
import com.crlink.smartpark.meeting.service.MeetingReviewService;
import com.crlink.smartpark.meeting.mapper.MeetingReviewMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collections;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 会议室审核人关联 服务实现类
 * */
@Service
public class MeetingReviewServiceImpl extends ServiceImpl<MeetingReviewMapper, MeetingReview>
    implements MeetingReviewService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReview, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingReview> lambdaQuery = this.lambdaQuery().in(MeetingReview::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingReview::getId, sValue);
        }
        Stream<MeetingReview> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingReview::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingReview::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeetingReview(MeetingReviewReqDTO reqDTO) {
        MeetingReview entity = MyBeanUtils.copy(reqDTO, MeetingReview::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     *
     */
    @Override
    public MeetingReviewDTO getMeetingReviewById(String id) {
        MeetingReview entity =
            Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<MeetingReviewDTO> listMeetingReviewPage(MeetingReviewQuery query) {
        query.startPage();
        List<MeetingReview> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingReviewDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public List<MeetingReviewDTO> findMeetingReviewByMeetingId(String meetingId) {
        List<MeetingReview> meetingReviewList = this.lambdaQuery().eq(MeetingReview::getMeetingId, meetingId).list();
        List<MeetingReviewDTO> meetingReviewDTOS = MyBeanUtils.copyList(meetingReviewList, MeetingReviewDTO::new);
        return meetingReviewDTOS;
    }

    @Override
    public Boolean removeMeetingReviewByMeetingId(String meetingId) {
        LambdaQueryWrapper<MeetingReview> queryWrapper = new LambdaQueryWrapper<MeetingReview>();
        queryWrapper.eq(MeetingReview::getMeetingId, meetingId);

        return this.remove(queryWrapper);
    }

    @Override
    public Boolean updateMeetingReviewByMeetingId(Set<String> reviewList, String meetingId) {
        if (CollUtil.isEmpty(reviewList) || StrUtil.isBlank(meetingId)) {
            return false;
        }
        Set<MeetingReview> relationSet = new HashSet<>(reviewList.size());
        Set<String> needAddEmployeeIds = reviewList;
        List<MeetingReview> relationList =
            list(Wrappers.<MeetingReview>lambdaQuery().eq(MeetingReview::getMeetingId, meetingId));
        if (CollUtil.isNotEmpty(relationList)) {
            Set<String> alreadyExistEmployeeIds =
                relationList.stream().map(MeetingReview::getEmployeeId).collect(Collectors.toSet());
            Set<String> needDelEmployeeIds = Sets.newHashSet(alreadyExistEmployeeIds);
            // 需要删除的
            needDelEmployeeIds.removeAll(needAddEmployeeIds);
            // 需要增加的
            needAddEmployeeIds.removeAll(alreadyExistEmployeeIds);
            if (CollUtil.isNotEmpty(needDelEmployeeIds)) {
                remove(Wrappers.<MeetingReview>lambdaQuery().eq(MeetingReview::getMeetingId, meetingId)
                    .in(MeetingReview::getEmployeeId, needDelEmployeeIds));
            }
        }
        if (CollUtil.isNotEmpty(needAddEmployeeIds)) {
            needAddEmployeeIds.forEach(e -> {
                MeetingReview relation = new MeetingReview();
                relation.setEmployeeId(e);
                relation.setMeetingId(meetingId);
                EntityUserUtil.setAll(relation);
                relationSet.add(relation);
            });
            saveBatch(relationSet);
        }
        return true;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingReview> getConditionWrapper(MeetingReviewQuery query) {
        LambdaQueryWrapper<MeetingReview> queryWrapper = new LambdaQueryWrapper<MeetingReview>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * 
     * @return DTO集合
     */
    private List<MeetingReviewDTO> entitiesToDTOs(List<MeetingReview> entities) {
        return MyBeanUtils.copyList(entities, MeetingReviewDTO::new, (s, t) -> {

        });
    }
}
