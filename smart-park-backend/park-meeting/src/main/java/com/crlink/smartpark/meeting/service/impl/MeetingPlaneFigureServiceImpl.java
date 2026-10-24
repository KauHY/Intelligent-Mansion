package com.crlink.smartpark.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.meeting.entity.MeetingPlaneFigure;
import com.crlink.smartpark.meeting.controller.request.query.MeetingPlaneFigureQuery;
import com.crlink.smartpark.meeting.model.dto.MeetingPlaneFigureReqDTO;
import com.crlink.smartpark.meeting.vo.MeetingPlaneFigureVO;
import com.crlink.smartpark.meeting.model.query.MeetingPlaneFigureTreeQuery;
import com.crlink.smartpark.meeting.service.MeetingPlaneFigureService;
import com.crlink.smartpark.meeting.mapper.MeetingPlaneFigureMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 会议室平面图 服务实现类
 * */
@Service
public class MeetingPlaneFigureServiceImpl extends ServiceImpl<MeetingPlaneFigureMapper, MeetingPlaneFigure> implements MeetingPlaneFigureService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingPlaneFigure, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingPlaneFigure> lambdaQuery = this.lambdaQuery().in(MeetingPlaneFigure::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingPlaneFigure::getId, sValue);
        }
        Stream<MeetingPlaneFigure> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingPlaneFigure::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingPlaneFigure::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveMeetingPlaneFigure(MeetingPlaneFigureReqDTO reqDTO) {
        //修改时只针对图片做修改
        if (StringUtils.hasText(reqDTO.getId())) {
            reqDTO.setParkOrgId(null);
            reqDTO.setBuildingId(null);
            reqDTO.setFloorId(null);
        } else {
            //新增时校验楼栋楼层唯一性
            int count = this.lambdaQuery().eq(MeetingPlaneFigure::getBuildingId, reqDTO.getBuildingId())
                    .eq(MeetingPlaneFigure::getFloorId, reqDTO.getFloorId()).count();
            AssertUtil.requireTrue(count == 0, "400003");
        }
        MeetingPlaneFigure entity = MyBeanUtils.copy(reqDTO, MeetingPlaneFigure::new);
        entity.setCreateTime(LocalDateTime.now());
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeetingPlaneFigureVO getMeetingPlaneFigureById(String id) {
        MeetingPlaneFigure entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeetingPlaneFigureVO> listMeetingPlaneFigurePage(MeetingPlaneFigureTreeQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryChainWrapper<MeetingPlaneFigure> wrapper = this.lambdaQuery()
                .in(MeetingPlaneFigure::getParkOrgId, parkOrgIds);
        wrapper.orderByDesc(MeetingPlaneFigure::getCreateTime);
        query.startPage();
        List<MeetingPlaneFigure> list = wrapper.list();
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingPlaneFigureVO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public String findPlaneFigure(String buildingId, String floorNum) {
        List<MeetingPlaneFigure> resultList = this.lambdaQuery().eq(MeetingPlaneFigure::getBuildingId, buildingId)
                .eq(MeetingPlaneFigure::getFloorId, floorNum).list();
        if (CollUtil.isNotEmpty(resultList)) {
            return resultList.get(0).getMeetingPlaneFigure();
        }
        return "";
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingPlaneFigure> getConditionWrapper(MeetingPlaneFigureQuery query) {
        LambdaQueryWrapper<MeetingPlaneFigure> queryWrapper = new LambdaQueryWrapper<MeetingPlaneFigure>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeetingPlaneFigureVO> entitiesToDTOs(List<MeetingPlaneFigure> entities) {
        return MyBeanUtils.copyList(entities, MeetingPlaneFigureVO::new, (s, t) -> {

        });
    }
}
