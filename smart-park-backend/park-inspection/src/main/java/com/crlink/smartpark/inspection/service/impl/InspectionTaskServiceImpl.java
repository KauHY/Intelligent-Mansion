package com.crlink.smartpark.inspection.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.inspection.common.Constant;
import com.crlink.smartpark.inspection.controller.request.TaskQuery;
import com.crlink.smartpark.inspection.controller.request.TaskRotateReqDTO;
import com.crlink.smartpark.inspection.dto.*;
import com.crlink.smartpark.inspection.dto.resultmap.PlanCircuitEntity;
import com.crlink.smartpark.inspection.entity.InspectionEmployeeRelation;
import com.crlink.smartpark.inspection.entity.InspectionPlan;
import com.crlink.smartpark.inspection.entity.InspectionPointDetail;
import com.crlink.smartpark.inspection.entity.InspectionTask;
import com.crlink.smartpark.inspection.mapper.InspectionTaskMapper;
import com.crlink.smartpark.inspection.service.*;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.TIME_WITHOUT_SECONDS_FORMATTER;
import static com.crlink.smartpark.common.constants.Constants.USER_WX_SOURCE_B;
import static com.crlink.smartpark.inspection.common.Constant.EmployeeRelationEnum.INSPECTION_PLAN;
import static com.crlink.smartpark.inspection.common.Constant.EmployeeRelationEnum.INSPECTION_TASK;
import static com.crlink.smartpark.inspection.common.Constant.InspectionTaskStatus.*;
import static com.crlink.smartpark.inspection.common.Constant.TASK_REDIS_LOCK_FORMAT;
import static com.crlink.smartpark.inspection.common.Constant.TaskPointDetailStatus.*;

/**
 * <p>
 * 服务实现类
 * </p>
 * */
@Service
@Slf4j
public class InspectionTaskServiceImpl extends ServiceImpl<InspectionTaskMapper, InspectionTask> implements InspectionTaskService {
    public static final int PENDING_TASK = 1;
    public static final int TODAY_TASK = 2;
    public static final int HISTORY_TASK = 3;

    @Autowired
    private InspectionPlanService inspectionPlanService;

    @Autowired
    private InspectionEmployeeRelationService inspectionEmployeeRelationService;

    @Autowired
    private InspectionCircuitService inspectionCircuitService;

    @Autowired
    public InspectionPointDetailService inspectionPointDetailService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private CommonProvider commonProvider;


    @Override
    @Id2objHandle
    public PageInfo<InspectionTaskDTO> listTaskPage(TaskQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        String employeeName = query.getEmployeeName();
        Integer taskStatus = query.getTaskStatus();
        Set<String> employeeIds = null;
        LambdaQueryChainWrapper<InspectionTask> queryWrapper = this.lambdaQuery();
        if (StringUtils.isNotBlank(employeeName)) {
            Map<String, String> employeeId2NameMap = employeeProvider.listEmployeeByName(employeeName);
            employeeIds = employeeId2NameMap.keySet();
            MyPageInfoUtils.isEmptyPage(employeeIds);
            Set<String> taskIds = inspectionEmployeeRelationService.lambdaQuery().eq(InspectionEmployeeRelation::getType, INSPECTION_TASK.type())
                    .in(InspectionEmployeeRelation::getEmployeeId, employeeIds).list().parallelStream()
                    .map(InspectionEmployeeRelation::getTargetId)
                    .collect(Collectors.toSet());
            MyPageInfoUtils.isEmptyPage(taskIds);

            String sql = new StringBuilder(" ( status = 1 and id in (")
                    .append(CollectionUtil.join(taskIds, ",", "'", "'"))
                    .append(" ) )").toString();
            String employeeSql = new StringBuilder("actual_employee_id in (")
                    .append(CollectionUtil.join(employeeIds, ",", "'", "'"))
                    .append(" )").toString();
            MyPageInfoUtils.isEmptyPage(taskIds);
            if (Objects.isNull(taskStatus)) {
                String sql2 = new StringBuilder(" OR ( status != 1 and ").append(employeeSql)
                        .append(")").toString();
                queryWrapper.apply(sql + sql2);
            } else if (taskStatus.equals(NOT_EXECUTE.status)) {
                queryWrapper.apply(sql);
            } else {
                queryWrapper.apply(employeeSql);
            }
        }

        query.startPage();
        List<InspectionTask> tasks = queryWrapper.like(StringUtils.isNotBlank(query.getTaskName()), InspectionTask::getTaskName, query.getTaskName())
                .in(InspectionTask::getParkOrgId, parkOrgIds)
                .eq(null != taskStatus, InspectionTask::getStatus, taskStatus)
                .ge(null != query.getCompleteStart(), InspectionTask::getCompletionTime, query.getCompleteStart())
                .le(null != query.getCompleteEnd(), InspectionTask::getCompletionTime, query.getCompleteEnd())
                .orderByDesc(InspectionTask::getCreateTime)
                .list();
        List<InspectionTaskDTO> list = convert(tasks);
        return query.createPageInfo(list);
    }


    /**
     * 根据巡检计划生成巡检任务,单线程执行
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateInspectionTask() {
        log.info("检查是否生成巡检任务");
        LocalDate currentDate = LocalDate.now();
        //判断当前的计划是否已经生成任务

        if (!getRedisLock(currentDate)) {
            log.info("无需巡检任务");
            return;
        }
        //查询当天要生效执行的计划
        List<InspectionPlan> plans = inspectionPlanService.lambdaQuery()
                .le(InspectionPlan::getEffectDateStart, currentDate)
                .ge(InspectionPlan::getEffectDateEnd, currentDate)
                .eq(InspectionPlan::getStatus, Constant.InspectionPlanStatus.OPENING.status()).list();
        if (ObjectUtil.isEmpty(plans)) {
            log.info("无需生成巡检任务");
            return;
        }
        log.info("存在要生成的巡检任务，开始处理，{}", JSONUtil.toJsonStr(plans));
        Set<String> planIds = plans.parallelStream().map(InspectionPlan::getId).collect(Collectors.toSet());
        Map<String, PlanCircuitEntity> planId2Circuit = inspectionPlanService.queryCircuitByPlanId(planIds);
        Map<String, String> plan2CircuitIdMap = (Map<String, String>) inspectionPlanService.id2ValueMap(planIds, InspectionPlan::getCircuitId);
        Map<String, List<InspectionPointDTO>> circuitId2PointsMap = inspectionCircuitService.queryPointByCircuitIds(plan2CircuitIdMap.values());

        //查询计划相关的人
        Map<String, Set<String>> employeeIdsMap = inspectionEmployeeRelationService.getEmployeeIds(INSPECTION_PLAN.type(), planIds);

        Map<String, String> taskId2PlanIdMap = Maps.newHashMap();
        List<InspectionTask> tasks = plans.parallelStream().filter(t -> isExecuteCurrentDate(currentDate, t.getCycleType(), t.getExecutionRule(), t.getCanDelay()))
                //巡检计划关联巡检人不能为空
                .filter(i -> ObjectUtil.isNotEmpty(employeeIdsMap.get(i.getId())))
                .map(p ->
                        buildTask(currentDate, p, taskId2PlanIdMap, planId2Circuit, circuitId2PointsMap)
                )
                //巡检内容不能为空
                .filter(task -> ObjectUtil.isNotNull(task.getTaskContent()))
                .collect(Collectors.toList());
        this.saveBatch(tasks);
        log.info("生成巡检任务，{}", JSONUtil.toJsonStr(tasks));
        ArrayList<InspectionEmployeeRelation> taskEmployeeRelations = Lists.newArrayList();
        taskId2PlanIdMap.forEach((k, v) -> {
            Set<String> employeeIds = employeeIdsMap.get(v);
            if (ObjectUtil.isNotEmpty(employeeIds)) {
                for (String employeeId : employeeIds) {
                    InspectionEmployeeRelation employeeRelation = new InspectionEmployeeRelation();
                    employeeRelation.setType(INSPECTION_TASK.type());
                    employeeRelation.setTargetId(k);
                    employeeRelation.setEmployeeId(employeeId);
                    taskEmployeeRelations.add(employeeRelation);
                }
            }
        });
        //保存任务相关的巡检人
        inspectionEmployeeRelationService.saveBatch(taskEmployeeRelations);
        log.info("保存生成任务相关的巡检人，{}", JSONUtil.toJsonStr(taskEmployeeRelations));

        //更新巡检点状态
        CompletableFuture.runAsync(() -> updateLastPointDetailStatusAndTaskStatus());
    }

    //更新昨天未完成的巡检点的状态，同时更新相关联巡检任务状态
    private void updateLastPointDetailStatusAndTaskStatus() {
        //筛选出一周内的巡检点，避免遗漏
        log.info("检查是否存在昨天未完成的巡检点");
        LocalDateTime today = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        LocalDateTime lastWeek = today.minusWeeks(1);
        List<InspectionPointDetail> inspectionPointDetails = inspectionPointDetailService.lambdaQuery()
                .le(InspectionPointDetail::getCreateTime, today)
                .ge(InspectionPointDetail::getCreateTime, lastWeek)
                .eq(InspectionPointDetail::getStatus, POINT_NOT_EXECUTE.status).list();
        if (CollectionUtil.isEmpty(inspectionPointDetails)) {
            log.info("不存在昨天未完成的巡检点");
        }
        log.info("存在昨天未完成的巡检点的状态,开始处理，{}", JSONUtil.toJsonStr(inspectionPointDetails));
        LocalDateTime nowTime = LocalDateTime.now();
        inspectionPointDetails.forEach(t -> {
            t.setStatus(INCOMPLETE.status);
            t.setUpdateTime(nowTime);
            t.setUpdateBy("-1");
        });
        inspectionPointDetailService.updateBatchById(inspectionPointDetails);
        log.info("未完成的巡检点的状态处理完成,同时更新相关联巡检任务状态，开始处理");
        List<InspectionTask> tasks = inspectionPointDetails.parallelStream().map(t -> {
            InspectionTask task = new InspectionTask();
            task.setId(t.getTaskId());
            task.setStatus(ENDED.status);
            task.setCompletionTime(nowTime);
            task.setUpdateBy("-1");
            task.setUpdateTime(nowTime);
            return task;
        }).collect(Collectors.toList());
        this.updateBatchById(tasks);
        log.info("相关联巡检任务状态，处理结束，{}", JSONUtil.toJsonStr(tasks));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean acceptTask(String id, String employeeId) {
        InspectionTask task = this.getById(id);
        TaskContentDTO content = task.getTaskContent();
        List<InspectionPointDetail> pointDetails = content.getTaskPoints().stream().map(t -> {
            InspectionPointDetail pointDetail = new InspectionPointDetail();
            pointDetail.setTaskId(id);
            pointDetail.setPointName(t.getPointName());
            pointDetail.setStatus(Constant.TaskPointDetailStatus.POINT_NOT_EXECUTE.status);
            EntityUserUtil.setAll(pointDetail);
            return pointDetail;
        }).collect(Collectors.toList());
        inspectionPointDetailService.saveBatch(pointDetails);
        task.setActualEmployeeId(employeeId);
        task.setStatus(EXECUTING.status);
        EntityUserUtil.setUpdateBy(task);
        this.updateById(task);
        return true;
    }

    @Override
    @Id2objHandle
    public InspectionTaskDTO findTaskById(String id) {
        InspectionTask task = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        InspectionTaskDTO taskDTO = convert(Lists.newArrayList(task)).get(0);
        List<InspectionPointDetailDTO> pointDetails = inspectionPointDetailService.lambdaQuery().eq(InspectionPointDetail::getTaskId, id).list()
                .parallelStream().map(t -> {
                    InspectionPointDetailDTO pointDetailDTO = MyBeanUtils.copy(t, InspectionPointDetailDTO::new);
                    Integer status = t.getStatus();
                    if (status.equals(POINT_ENDED.status) || status.equals(TIMEOUT_END.status)) {
                        pointDetailDTO.setUpdateTime(t.getUpdateTime().format(TIME_WITHOUT_SECONDS_FORMATTER));
                    } else {
                        pointDetailDTO.setUpdateTime("-");
                    }
                    pointDetailDTO.setPointStatus(status);
                    return pointDetailDTO;
                })
                .collect(Collectors.toList());
        taskDTO.setOrderly(task.getTaskContent().getOrderly());
        taskDTO.setPointDetails(pointDetails);
        return taskDTO;
    }


    @Override
    @Id2objHandle
    public PageInfo<InspectionTaskDTO> wxTaskList(BaseQuery query, Integer listType) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        query.startPage();
        List<InspectionTask> tasks = null;
        if (listType == PENDING_TASK) {
            tasks = baseMapper.queryPendingTasksByEmployeeId(employeeId, LocalDate.now());
        } else if (listType == TODAY_TASK) {
            tasks = this.lambdaQuery().eq(InspectionTask::getActualEmployeeId, employeeId)
                    .eq(InspectionTask::getTaskDate, LocalDate.now()).list();
//                    .eq(InspectionTask::getStatus, NOT_EXECUTE.status).list();
        } else if (listType == HISTORY_TASK) {
            tasks = baseMapper.queryHistoryTasksByEmployeeId(employeeId, LocalDate.now());
        }
        MyPageInfoUtils.isEmptyPage(tasks);
        List<InspectionTaskDTO> list = convert(tasks);
        return query.createPageInfo(list);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rotateTask(TaskRotateReqDTO dto) {
        inspectionEmployeeRelationService.remove(Wrappers.<InspectionEmployeeRelation>lambdaQuery()
                .eq(InspectionEmployeeRelation::getTargetId, dto.getTaskId())
                .eq(InspectionEmployeeRelation::getType, INSPECTION_TASK.type));
        List<InspectionEmployeeRelation> relations = dto.getEmployeeIds().parallelStream().map(t -> {
            InspectionEmployeeRelation relation = new InspectionEmployeeRelation();
            relation.setType(INSPECTION_TASK.type);
            relation.setTargetId(dto.getTaskId());
            relation.setEmployeeId(t);
            return relation;
        }).collect(Collectors.toList());
        inspectionEmployeeRelationService.saveBatch(relations);
        return true;
    }

    @Override
    public Map<Integer, Long> getTodayTaskCount(String parkOrgId) {
        Map<Integer, Long> result = Maps.newHashMap();
        List<InspectionTask> list = this.lambdaQuery().select(InspectionTask::getId, InspectionTask::getStatus)
                .eq(InspectionTask::getParkOrgId, parkOrgId)
                .eq(InspectionTask::getTaskDate, LocalDate.now()).list();
        Set<String> taskIds = list.parallelStream().map(InspectionTask::getId).collect(Collectors.toSet());
        Map<Integer, Long> group = list.parallelStream().collect(Collectors.groupingBy(InspectionTask::getStatus, Collectors.counting()));
        for (Constant.InspectionTaskStatus taskStatus : Constant.InspectionTaskStatus.values()) {
            result.put(taskStatus.status, group.getOrDefault(taskStatus.status, 0L));
        }
        return result;
    }

    @Override
    public List<InspectionTaskDetailVO> get3DayExceptionalTaskDetail(String parkOrgId) {
        LocalDate today = LocalDate.now();
        List<InspectionTask> tasks = this.lambdaQuery().select(InspectionTask::getId, InspectionTask::getTaskName).eq(InspectionTask::getParkOrgId, parkOrgId)
                .in(InspectionTask::getTaskDate, today, today.minusDays(1), today.minusDays(2)).list();
        if (CollUtil.isEmpty(tasks)) {
            return Lists.newArrayList();
        }
        Map<String, String> taskMap = tasks.parallelStream().collect(Collectors.toMap(InspectionTask::getId, InspectionTask::getTaskName));
        List<InspectionPointDetail> details = inspectionPointDetailService.lambdaQuery()
                .eq(InspectionPointDetail::getState, false)
                .in(InspectionPointDetail::getTaskId, taskMap.keySet())
                .orderByDesc(InspectionPointDetail::getUpdateTime).list();
        List<InspectionTaskDetailVO> result = MyBeanUtils.copyList(details, InspectionTaskDetailVO::new, (s, t) -> {
            t.setTaskName(taskMap.get(t.getTaskId()));
        });
        return result;
    }

    private List<InspectionTaskDTO> convert(List<InspectionTask> tasks) {
        String source = SystemUtil.getInstance().getSource();
        Set<String> taskIds = tasks.parallelStream().map(InspectionTask::getId).collect(Collectors.toSet());
        Map<String, Set<String>> taskEmployeeMap = inspectionEmployeeRelationService.getEmployeeIds(INSPECTION_TASK.type, taskIds);
        List<InspectionTaskDTO> list = MyBeanUtils.copyList(tasks, InspectionTaskDTO::new, (s, t) -> {
            completeTaskTime(s, t, source);
            t.setEmployeeIds(taskEmployeeMap.get(s.getId()));
        });
        return list;
    }


    private InspectionTask buildTask(LocalDate currentDate, InspectionPlan p,
                                     Map<String, String> taskId2PlanIdMap,
                                     Map<String, PlanCircuitEntity> planId2CircuitMap,
                                     Map<String, List<InspectionPointDTO>> circuitId2PointsMap) {
        InspectionTask task = MyBeanUtils.copy(p, InspectionTask::new, "id");
        String taskId = IdWorker.getIdStr(task);
        taskId2PlanIdMap.put(taskId, p.getId());
        task.setId(taskId);
        task.setTaskDate(currentDate);
        task.setTaskName(p.getPlanName());
        task.setTaskContent(buildTaskContent(p.getId(), planId2CircuitMap, circuitId2PointsMap));
        task.setStatus(NOT_EXECUTE.status);
        return task;
    }

    private TaskContentDTO buildTaskContent(String planId,
                                            Map<String, PlanCircuitEntity> planId2CircuitMap,
                                            Map<String, List<InspectionPointDTO>> circuitId2PointsMap) {
        PlanCircuitEntity circuitEntity = planId2CircuitMap.get(planId);
        List<InspectionPointDTO> pointDTOs = circuitId2PointsMap.get(circuitEntity.getCircuitId());
        if (ObjectUtil.isEmpty(pointDTOs)) {
            return null;
        } else {
            List<TaskContentDTO.TaskPointDTO> points = pointDTOs.parallelStream().map(p -> {
                TaskContentDTO.TaskPointDTO dto = new TaskContentDTO.TaskPointDTO();
                dto.setPointName(p.getPointName());
                dto.setLocation(p.getLocation());
                return dto;
            }).collect(Collectors.toList());
            TaskContentDTO taskContentDTO = new TaskContentDTO();
            taskContentDTO.setTaskPoints(points);
            taskContentDTO.setCircuitName(circuitEntity.getCircuitName());
            taskContentDTO.setOrderly(circuitEntity.getOrderly());
            return taskContentDTO;
        }
    }

    /**
     * 判断是否生成当天的任务
     *
     * @param localDate 当天日期
     * @param cycleType 执行周期
     * @param canDelay  是否顺延节假日
     * @return
     */
    private boolean isExecuteCurrentDate(LocalDate localDate, Integer cycleType, String executionRule, Boolean canDelay) {
        DayOfWeek weekDay = localDate.getDayOfWeek();
        int dayOfWeek = weekDay.getValue();
        //执行周期是按周执行
        if (Objects.equals(1, cycleType)) {
            return Arrays.stream(StringUtils.split(executionRule, ",")).anyMatch(t -> dayOfWeek == Integer.parseInt(t));
        }
        return true;
    }

    private Boolean getRedisLock(LocalDate localDate) {
        LocalDate nextDay = localDate.plusDays(1);
        redisTemplate.opsForValue().set(String.format(TASK_REDIS_LOCK_FORMAT, nextDay), nextDay.toString(), 25, TimeUnit.HOURS);
        Boolean lockResult = Optional.ofNullable(redisTemplate.opsForValue().get(String.format(TASK_REDIS_LOCK_FORMAT, localDate))).map(t -> true).orElse(false);
        if (lockResult) {
            redisTemplate.delete(String.format(TASK_REDIS_LOCK_FORMAT, localDate));
        }
        return lockResult;

    }

    private void completeTaskTime(InspectionTask s, InspectionTaskDTO t, String source) {
        String startTime = DateTimeFormatter.ofPattern("HH:mm").format(s.getExecutionTimeStart());
        String endTime = DateTimeFormatter.ofPattern("HH:mm").format(s.getExecutionTimeEnd());
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(s.getTaskDate());
        if (USER_WX_SOURCE_B.equals(source)) {
            String taskStartTime = new StringBuilder().append(date).append(StringUtils.SPACE).append(startTime).toString();
            t.setTaskStartTime(taskStartTime);
            String taskEndTime = new StringBuilder().append(date).append(StringUtils.SPACE).append(endTime).toString();
            t.setTaskEndTime(taskEndTime);
        }
        String taskTime = new StringBuilder().append(date).append(StringUtils.SPACE).append(startTime)
                .append("-").append(date).append(StringUtils.SPACE).append(endTime).toString();
        t.setTaskTime(taskTime);
    }


    /**
     * 根据巡检点状态判断巡检任务状态
     *
     * @param taskIds
     * @return
     */
    //private Map<String, Integer> getTaskStatusByPointsStatus(Collection<String> taskIds) {
    //    if (CollectionUtil.isEmpty(taskIds)) {
    //        return Collections.emptyMap();
    //    }
    //    Map<String, Set<Integer>> taskId2PointDetailStatusesMap = inspectionPointDetailService.lambdaQuery()
    //            .select(InspectionPointDetail::getStatus, InspectionPointDetail::getTaskId)
    //            .in(InspectionPointDetail::getTaskId, taskIds).list()
    //            .parallelStream()
    //            .collect(Collectors.groupingBy(InspectionPointDetail::getTaskId,
    //                    Collectors.mapping(InspectionPointDetail::getStatus, Collectors.toSet())));
    //    Map<String, Integer> taskStatusMap = Maps.newHashMap();
    //    taskId2PointDetailStatusesMap.forEach((k, v) -> {
    //        if (v.contains(POINT_NOT_EXECUTE.status) || v.contains(EXECUTING.status)) {
    //            taskStatusMap.put(k, EXECUTING.status);
    //        } else {
    //            taskStatusMap.put(k, ENDED.status);
    //        }
    //    });
    //    return taskStatusMap;
    //}
}
