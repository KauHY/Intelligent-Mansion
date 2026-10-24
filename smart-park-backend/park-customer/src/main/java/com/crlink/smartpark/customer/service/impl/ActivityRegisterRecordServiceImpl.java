package com.crlink.smartpark.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.controller.request.ActivityRegisterRecordReqDTO;
import com.crlink.smartpark.customer.controller.request.query.ActivityRegisterRecordQuery;
import com.crlink.smartpark.customer.dto.ActivityRegisterRecordDTO;
import com.crlink.smartpark.customer.entity.Activity;
import com.crlink.smartpark.customer.entity.ActivityRegisterRecord;
import com.crlink.smartpark.customer.mapper.ActivityRegisterRecordMapper;
import com.crlink.smartpark.customer.service.ActivityRegisterRecordService;
import com.crlink.smartpark.customer.service.ActivityService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 报名记录 服务实现类
 * */
@Service
public class ActivityRegisterRecordServiceImpl extends ServiceImpl<ActivityRegisterRecordMapper, ActivityRegisterRecord> implements ActivityRegisterRecordService {

    public static final String FILE_NAME = "报名记录";
    @Autowired
    private ActivityService activityService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ActivityRegisterRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ActivityRegisterRecord> lambdaQuery = this.lambdaQuery().in(ActivityRegisterRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(ActivityRegisterRecord::getId, sValue);
        }
        Stream<ActivityRegisterRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ActivityRegisterRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ActivityRegisterRecord::getId, Function.identity()));
        }
        return result;
    }

    @Override
    public Integer queryRegisteredCount(String activityId) {
        Integer count = this.lambdaQuery().eq(ActivityRegisterRecord::getActivityId, activityId).count();
        return count;
    }


    /**
     * 获取用户报名的所有活动Id
     *
     * @param userIds
     * @return
     */
    @Override
    public Map<String, Set<String>> queryUserRegisterActivityIds(Collection<String> userIds) {
        List<ActivityRegisterRecord> list = this.lambdaQuery().select(ActivityRegisterRecord::getActivityId, ActivityRegisterRecord::getCreateBy)
                .in(ActivityRegisterRecord::getCreateBy, userIds).list();
        Map<String, Set<String>> result = list.parallelStream().collect(Collectors.groupingBy(ActivityRegisterRecord::getCreateBy,
                Collectors.mapping(ActivityRegisterRecord::getActivityId, Collectors.toSet())));
        return result;
    }

    @Override
    public Boolean isRegistered(String activityId, String userId) {
        Integer count = this.lambdaQuery().eq(ActivityRegisterRecord::getActivityId, activityId)
                .eq(ActivityRegisterRecord::getCreateBy, userId).count();
        return Optional.ofNullable(count).map(t -> count >= 1).orElse(false);
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateActivityRegisterRecord(ActivityRegisterRecordReqDTO reqDTO) {
        ActivityRegisterRecord entity = MyBeanUtils.copy(reqDTO, ActivityRegisterRecord::new);
        Activity activity = activityService.getById(reqDTO.getActivityId());
        if (Optional.ofNullable(activity.getConfRegister()).orElse(false)
                && Optional.of(activity.getConfRegisterLimit()).orElse(false)) {
            Integer registeredCount = this.queryRegisteredCount(activity.getId());
            if (registeredCount == activity.getConfRegisterLimitCount().intValue()) {
                throw new ResolinkRuntimeException("50004");
            }
        }
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Integer selfCount = this.lambdaQuery().eq(ActivityRegisterRecord::getActivityId, activity.getId())
                .eq(ActivityRegisterRecord::getCreateBy, userId).count();
        if (selfCount > 0) {
            throw new ResolinkRuntimeException("50005");
        }
        EntityUserUtil.setCreateBy(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public ActivityRegisterRecordDTO getActivityRegisterRecordById(String id) {
        ActivityRegisterRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<ActivityRegisterRecordDTO> listActivityRegisterRecordPage(ActivityRegisterRecordQuery query) {
        query.startPage();
        List<ActivityRegisterRecord> list = this.lambdaQuery()
                .eq(ActivityRegisterRecord::getActivityId, query.getId())
                .like(StringUtils.isNotBlank(query.getName()), ActivityRegisterRecord::getName, query.getName())
                .like(StringUtils.isNotBlank(query.getMobile()), ActivityRegisterRecord::getMobile, query.getMobile())
                .list();
        MyPageInfoUtils.isEmptyPage(list);
        List<ActivityRegisterRecordDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void export(ActivityRegisterRecordQuery query) {
        List<ActivityRegisterRecord> list = listRecordPage(query);
        ExcelExportUtils.export(() -> list,
                this::entitiesToDTOs, FILE_NAME, ActivityRegisterRecordDTO.class);
    }

    private List<ActivityRegisterRecord> listRecordPage(ActivityRegisterRecordQuery query) {
        List<ActivityRegisterRecord> list = this.lambdaQuery()
                .eq(ActivityRegisterRecord::getActivityId, query.getId())
                .like(StringUtils.isNotBlank(query.getName()), ActivityRegisterRecord::getName, query.getName())
                .like(StringUtils.isNotBlank(query.getMobile()), ActivityRegisterRecord::getMobile, query.getMobile())
                .list();
        return list;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ActivityRegisterRecord> getConditionWrapper(ActivityRegisterRecordQuery query) {
        LambdaQueryWrapper<ActivityRegisterRecord> queryWrapper = new LambdaQueryWrapper<ActivityRegisterRecord>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ActivityRegisterRecordDTO> entitiesToDTOs(List<ActivityRegisterRecord> entities) {
        return MyBeanUtils.copyList(entities, ActivityRegisterRecordDTO::new, (s, t) -> {

        });
    }
}
