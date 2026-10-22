package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.common.enums.ActivityStatusEnum;
import com.crlink.smartpark.customer.controller.request.ActivityReqDTO;
import com.crlink.smartpark.customer.controller.request.query.ActivityQuery;
import com.crlink.smartpark.customer.controller.response.ActivityRespDTO;
import com.crlink.smartpark.customer.controller.response.ActivityToppingReqDTO;
import com.crlink.smartpark.customer.controller.response.MyRegisterRecord;
import com.crlink.smartpark.customer.dto.ActivityDTO;
import com.crlink.smartpark.customer.dto.ActivityRegisterRecordDTO;
import com.crlink.smartpark.customer.entity.Activity;
import com.crlink.smartpark.customer.entity.ActivityRegisterRecord;
import com.crlink.smartpark.customer.entity.BusinessParkRelation;
import com.crlink.smartpark.customer.mapper.ActivityMapper;
import com.crlink.smartpark.customer.service.ActivityRegisterRecordService;
import com.crlink.smartpark.customer.service.ActivityService;
import com.crlink.smartpark.customer.service.BusinessParkRelationService;
import com.crlink.smartpark.customer.vo.ActivityList;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.customer.common.enums.BusinessType.ACTIVITY;
import static com.crlink.smartpark.customer.constants.Constant.DEFAULT_TOPPING_TIME;


/**
 * 活动 服务实现类
 * */
@Slf4j
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    private BusinessParkRelationService businessParkRelationService;

    @Autowired
    private ActivityRegisterRecordService recordService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Activity, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<Activity> lambdaQuery = this.lambdaQuery().in(Activity::getId, ids);
        if (flag) {
            lambdaQuery.select(Activity::getId, sValue);
        }
        Stream<Activity> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(Activity::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(Activity::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateActivity(ActivityReqDTO reqDTO) {
        String id = reqDTO.getId();
        checkActivityNameAndEffectTime(id, reqDTO.getName(), reqDTO.getEffectTime(), reqDTO.getActivityTimeEnd());
        Activity entity = MyBeanUtils.copy(reqDTO, Activity::new);
        if (StringUtils.isBlank(id)) {
            EntityUserUtil.setAll(entity);
        } else {
            LambdaQueryWrapper<BusinessParkRelation> removeQuery = Wrappers.<BusinessParkRelation>query().lambda()
                    .eq(BusinessParkRelation::getBusinessType, ACTIVITY.code)
                    .eq(BusinessParkRelation::getBusinessId, id);
            businessParkRelationService.remove(removeQuery);
            EntityUserUtil.setUpdateBy(entity);
        }
        this.saveOrUpdate(entity);
        List<BusinessParkRelation> businessParkRelations = reqDTO.getParkOrgIds().stream().map(parkOrgId -> {
            BusinessParkRelation businessParkRelation = new BusinessParkRelation();
            businessParkRelation.setParkOrgId(parkOrgId);
            businessParkRelation.setBusinessId(entity.getId());
            businessParkRelation.setBusinessType(ACTIVITY.code);
            return businessParkRelation;
        }).collect(Collectors.toList());
        return businessParkRelationService.saveBatch(businessParkRelations);
    }


    /**
     * 通过id查询实体
     */
    @Override
    public ActivityDTO getActivityById(String id) {
        Activity entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        //Boolean registered = recordService.isRegistered(id, SystemUtil.getInstance().getCurrentUserId());
        ActivityDTO activityDTO = (ActivityDTO) entitiesToDTOs(Lists.newArrayList(entity), ActivityDTO::new).get(0);
        //activityDTO.setSelfRegistered(registered);
        return activityDTO;
    }

    /**
     * 分页查询
     * 列表显示的排序按如下优先级依次排序：
     * 1、已置顶的排前，按置顶时间由近至远排列；2、按创建时间倒序显示。
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<ActivityRespDTO> listActivityPage(ActivityQuery query) {
        List<Activity> list = query(query);
        MyPageInfoUtils.isEmptyPage(list);
        //list.forEach(activity -> setActivityStatus(activity));
        List<ActivityRespDTO> result = (List<ActivityRespDTO>) entitiesToDTOs(list, ActivityRespDTO::new);
        return query.createPageInfo(result);
    }


    /**
     * C端小程序活动列表
     * 活动列表显示的排序按如下优先级依次排序排序规则：
     * （1）优先显示已置顶的活动，按置顶时间由近至远排列；（2）其次按“活动开始时间”倒序取值显示；
     *
     * @param query
     * @return
     */
    @Override
    public PageInfo<ActivityDTO> wxListActivityPage(BaseQuery<String> query) {
        String parkOrgId = query.getParam();
        Map<String, Set<String>> map = businessParkRelationService.parkOrgIds2BusinessIds(Sets.newHashSet(parkOrgId), ACTIVITY.code);
        Set<String> activityIds = map.get(parkOrgId);
        query.startPage();
        List<ActivityDTO> activities = queryWxActivityList(activityIds, Activity::getTopping,
                Activity::getToppingTime, Activity::getActivityTimeStart);
        return query.createPageInfo(activities);
    }

    /**
     * C端小程序我的活动报名列表
     * 按“活动开始时间”倒序取值显示
     *
     * @param query
     * @return
     */
    @Override
    public PageInfo<ActivityDTO> wxMyListActivityPage(BaseQuery<String> query) {
        String parkOrgId = query.getParam();
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Map<String, Set<String>> userRegisterActivityIds = recordService.queryUserRegisterActivityIds(Sets.newHashSet(userId));
        Set<String> userActivityIds = userRegisterActivityIds.get(userId);
        Collection<String> activityIds = null;
        if (StringUtils.isNotBlank(parkOrgId)) {
            Map<String, Set<String>> map = businessParkRelationService.parkOrgIds2BusinessIds(Sets.newHashSet(parkOrgId), ACTIVITY.code);
            Set<String> parkActivityIds = map.get(parkOrgId);
            activityIds = CollectionUtil.intersection(userActivityIds, parkActivityIds);
        } else {
            activityIds = userActivityIds;
        }
        query.startPage();
        //活动开始时间倒序排列
        List<ActivityDTO> activityDTOS = queryWxActivityList(activityIds, Activity::getActivityTimeStart);
        return query.createPageInfo(activityDTOS);
    }

    @Override
    public MyRegisterRecord getWxMyRegisterRecordDetail(String activityId) {
        log.info(">>>>>>>>>>我的报名详情<<<<<<<<<<<<");
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Activity activity = this.getById(activityId);
        ActivityDTO o = (ActivityDTO) entitiesToDTOs(Lists.newArrayList(activity), ActivityDTO::new).get(0);
        ActivityRegisterRecord record = recordService.lambdaQuery().eq(ActivityRegisterRecord::getActivityId, activityId).eq(ActivityRegisterRecord::getCreateBy, userId).one();
        return new MyRegisterRecord(o, MyBeanUtils.copy(record, ActivityRegisterRecordDTO::new));
    }

    @Override
    public Boolean toppingActivity(ActivityToppingReqDTO reqDTO) {
        Activity activity = this.lambdaQuery().select(Activity::getId, Activity::getTopping).eq(Activity::getId, reqDTO.getActivityId()).one();
        if ((activity.getTopping() && reqDTO.getTopping()) || (!activity.getTopping() && !reqDTO.getTopping())) {
            throw new ResolinkRuntimeException("50006");
        }
        activity.setTopping(reqDTO.getTopping());
        if (reqDTO.getTopping()) {
            activity.setToppingTime(LocalDateTime.now());
        } else {
            activity.setToppingTime(DEFAULT_TOPPING_TIME);
        }
        activity.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        return this.updateById(activity);
    }

    /**
     * 近5条的园区活动，优先顺序为“进行中”、“即将开始”、“已结束”。展示内容“活动标题”和“活动时间”
     * “即将开始”的活动时间：取值活动开始时间（年月日）；
     * “进行中”的活动不显示活动时间，显示“进行中”即可；
     * “已结束”的活动时间：取值活动结束时间（年月日）；
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public List<ActivityList> getActivityList(String parkOrgId) {
        List<Activity> list = baseMapper.getActivityList(parkOrgId, LocalDateTime.now());
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return MyBeanUtils.copyList(list, ActivityList::new,
                (s, t) -> t.setStatus(getActivityStatus(s)));
    }

    private List<ActivityDTO> queryWxActivityList(Collection<String> activityIds, SFunction<Activity, ?>... function) {
        MyPageInfoUtils.isEmptyPage(activityIds);
        List<Activity> list = this.lambdaQuery().in(Activity::getId, activityIds)
                .le(Activity::getEffectTime, LocalDateTime.now())
                .orderByDesc(function).list();
        List<ActivityDTO> activities = (List<ActivityDTO>) entitiesToDTOs(list, ActivityDTO::new);
        return activities;
    }

    public List<Activity> query(ActivityQuery query) {
        Set<String> currentUseParkOrgIds = null;
        if (StringUtils.isNotBlank(query.getParkOrgId())) {
            currentUseParkOrgIds = Sets.newHashSet(query.getParkOrgId());
        } else {
            currentUseParkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        }
        Map<String, Set<String>> map = businessParkRelationService.parkOrgIds2BusinessIds(currentUseParkOrgIds, ACTIVITY.code);
        Set<String> activityIds = map.values().parallelStream().flatMap(Collection::stream).collect(Collectors.toSet());
        MyPageInfoUtils.isEmptyPage(activityIds);
        LocalDateTime start = query.getActivityTimeStart();
        LocalDateTime end = query.getActivityTimeEnd();
        LambdaQueryChainWrapper<Activity> lambdaQuery = this.lambdaQuery();
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            lambdaQuery.not(wrapper -> wrapper.gt(Activity::getActivityTimeStart, end)
                    .or().lt(Activity::getActivityTimeEnd, start));
        }
        queryByStatus(lambdaQuery, query.getStatus());
        query.startPage();
        List<Activity> list = lambdaQuery.like(StringUtils.isNotBlank(query.getName()), Activity::getName, query.getName())
                .ge(Objects.nonNull(query.getEffectTimeStart()), Activity::getEffectTime, query.getEffectTimeStart())
                .le(Objects.nonNull(query.getEffectTimeEnd()), Activity::getEffectTime, query.getEffectTimeEnd())
                .eq(Objects.nonNull(query.getConfRegister()), Activity::getConfRegister, query.getConfRegister())
                //.eq(Objects.nonNull(query.getStatus(),))
                .in(Activity::getId, activityIds)
                .orderByDesc(Activity::getTopping, Activity::getToppingTime, Activity::getCreateTime)
                .list();
        return list;
    }

    private void queryByStatus(LambdaQueryChainWrapper<Activity> lambdaQuery, Integer status) {
        if (status == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        if (status == ActivityStatusEnum.NOT_EFFECT.code) {
            lambdaQuery.ge(Activity::getEffectTime, now);
        } else if (status == ActivityStatusEnum.NOTICING.code) {
            lambdaQuery.le(Activity::getEffectTime, now).ge(Activity::getActivityTimeStart, now);
        } else if (status == ActivityStatusEnum.ONGOING.code) {
            lambdaQuery.le(Activity::getActivityTimeStart, now).ge(Activity::getActivityTimeEnd, now);
        } else if (status == ActivityStatusEnum.END.code) {
            lambdaQuery.le(Activity::getActivityTimeEnd, now);
        }
    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<Activity> getConditionWrapper(ActivityQuery query) {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<Activity>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<?> entitiesToDTOs(List<Activity> entities, Supplier<? extends ActivityDTO> supplier) {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Set<String> userIds = Sets.newHashSet(userId);
        Map<String, Set<String>> userId2activityIdsMap = recordService.queryUserRegisterActivityIds(userIds);
        Set<String> activityIds = entities.parallelStream().map(Activity::getId).collect(Collectors.toSet());
        Map<String, Set<String>> activityId2ParkOrgIdsMap = businessParkRelationService.businessIds2ParkOrgIds(activityIds, ACTIVITY.code);
        List<?> list = MyBeanUtils.copyList(entities, supplier, (s, t) -> {
            List<String> parkOrgIds = activityId2ParkOrgIdsMap.get(s.getId()).parallelStream().filter(parkOrgId -> parkOrgId != null).collect(Collectors.toList());
            t.setParkOrgIds(parkOrgIds);
            boolean sefRegistered = CollectionUtil.contains(userId2activityIdsMap.getOrDefault(userId, new HashSet<>(1)), s.getId());
            t.setSelfRegistered(sefRegistered);
            Integer registeredCount = recordService.queryRegisteredCount(s.getId());
            t.setRegisteredCount(Optional.ofNullable(registeredCount).orElse(0));
            t.setConfRegisterLimitCount(s.getConfRegisterLimitCount());
            t.setStatus(getActivityStatus(s));
        });
        return list;
    }


    private int getActivityStatus(Activity activity) {
        LocalDateTime now = LocalDateTime.now();
        if (activity.getEffectTime().isAfter(now)) {
            return ActivityStatusEnum.NOT_EFFECT.code;
        } else if (activity.getEffectTime().isBefore(now) && activity.getActivityTimeStart().isAfter(now)) {
            return ActivityStatusEnum.NOTICING.code;
        } else if (activity.getActivityTimeStart().isBefore(now) && activity.getActivityTimeEnd().isAfter(now)) {
            return ActivityStatusEnum.ONGOING.code;
        } else if (activity.getActivityTimeEnd().isBefore(now)) {
            return ActivityStatusEnum.END.code;
        }
        return -1;
    }

    /**
     * 检验活动名称不能重复,
     * 生效时间不可晚于活动截止时间
     *
     * @param id              活动id
     * @param name            活动名称
     * @param effectTime      活动生效时间
     * @param activityTimeEnd 活动截止时间
     */
    private void checkActivityNameAndEffectTime(String id, String name,
                                                LocalDateTime effectTime,
                                                LocalDateTime activityTimeEnd) {
        Integer count = this.lambdaQuery().eq(Activity::getName, name)
                .ne(StringUtils.isNotBlank(id), Activity::getId, id).count();
        if (count > 0) {
            throw new ResolinkRuntimeException("50009");
        }
        if (!effectTime.isBefore(activityTimeEnd)) {
            throw new ResolinkRuntimeException("50010");
        }
    }

    public static void main(String[] args) {
        List<String> s1 = Lists.newArrayList("a", "b", "c", "c");
        List<String> s2 = Lists.newArrayList("a", "b", "c", "c", "d");
        System.out.println(CollectionUtil.intersection(s1, s2));
    }
}
