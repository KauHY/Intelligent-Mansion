package com.crlink.smartpark.core.schedule;

import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.ScheduledTaskDTO;
import com.crlink.smartpark.common.dto.TaskDTO;
import com.crlink.smartpark.common.feign.TaskProvider;
import com.crlink.smartpark.core.utils.LockUtil;
import com.crlink.smartpark.core.utils.SpringContextUtils;
import com.crlink.smartpark.core.utils.SpringTargetBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskExecutorUtils {

    @Resource
    private TaskProvider taskProvider;
    @Resource
    private CronTaskRegistrar cronTaskRegistrar;

    public static void execute(List<ScheduledTaskDTO> tasks) {
        CronTaskRegistrar cronTaskRegistrar = (CronTaskRegistrar) SpringContextUtils.getBean("cronTaskRegistrar");
        TaskProvider taskProvider = SpringContextUtils.getBean(TaskProvider.class);
        Map<String, Runnable> taskMap = tasks.parallelStream().collect(Collectors.toMap(ScheduledTaskDTO::getTaskId, ScheduledTaskDTO::getRunnable));
        Set<TaskDTO> taskDTOs = taskProvider.getExecuteCondition(taskMap.keySet());
        Map<String, TaskDTO> dtoMap = taskDTOs.parallelStream().collect(Collectors.toMap(TaskDTO::getId, Function.identity()));
        dtoMap.forEach((k, v) -> {
            if (!v.getClosed()) {
                ThreadPoolTaskScheduler taskScheduler = (ThreadPoolTaskScheduler) SpringContextUtils.getBean("backendTaskScheduler");
                if (v.getPoolSize() != null) {
                    taskScheduler.setPoolSize(v.getPoolSize());
                }
                if (v.getRemoveOnCancelPolicy() != null) {
                    taskScheduler.setRemoveOnCancelPolicy(v.getRemoveOnCancelPolicy());
                }
                if (StringUtils.isNotBlank(v.getThreadNamePrefix())) {
                    taskScheduler.setThreadNamePrefix(v.getThreadNamePrefix());
                }
                System.out.println(JSONUtil.toJsonStr(taskScheduler));
                cronTaskRegistrar.addCronTask(k, taskMap.get(k), v.getCron(), taskScheduler);
            }
        });

    }

    public void execute() {
        Set<TaskDTO> tasks = null;
        try {
            String applicationName = SpringContextUtils.getApplicationId().substring(0, SpringContextUtils.getApplicationId().length() - 2);
            tasks = taskProvider.getScheduleTasks(applicationName);
        } catch (Exception e) {
            log.error("请先启动park-common服务", e);
        }
        int taskSize = (tasks == null ? 0 : tasks.size());
        log.info("启动任务数：{}", taskSize);
        if (taskSize == 0) {
            return;
        }
        for (TaskDTO taskDTO : tasks) {
            if (taskDTO.getClosed()) {
                continue;
            }
            ThreadPoolTaskScheduler taskScheduler = (ThreadPoolTaskScheduler) SpringContextUtils.getBean("backendTaskScheduler");
            Object bean = null;
            try {
                bean = SpringContextUtils.getBean(taskDTO.getBeanName());
            } catch (Exception e) {
            }
            if (bean != null) {
                try {
                    Object target = SpringTargetBeanUtils.getTarget(bean);
                    Method method = ClassUtils.getMethod(target.getClass(), taskDTO.getMethodName());
                    Runnable runnable = () -> {
                        String lockKey = Constants.Redis.TASK_LOCK_PREFIX.getBuildCode(taskDTO.getId());
                        if (LockUtil.lock(lockKey)) {
                            try {
                                invoke(target, method);
                            } catch (Exception e) {
                                log.error("定时器执行异常", e);
                            } finally {
                                LockUtil.unLock(lockKey);
                            }
                        }
                    };
                    log.info("添加任务: {}-{}", taskDTO.getId(), taskDTO.getTaskName());
                    cronTaskRegistrar.addCronTask(taskDTO.getId(), runnable, taskDTO.getCron(), taskScheduler);
                } catch (Exception e) {
                    log.error("执行定时任务失败：{}", tasks, e);
                }
            }
        }
    }

    public static Object invoke(Object bean, Method method) {
        Object result = null;
        if (method.isAnnotationPresent(Transactional.class)) {
            PlatformTransactionManager transactionManager = SpringContextUtils.getBean(PlatformTransactionManager.class);
            if (transactionManager != null) {
                DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
                TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
                try {
                    log.info(">>>>>>>>>>>>定时任务开启事务！<<<<<<<<<<<<<<<");
                    result = method.invoke(bean);
                    transactionManager.commit(transactionStatus);
                    log.info(">>>>>>>>>>>>定时任务提交事务！<<<<<<<<<<<<<<<");
                } catch (Exception e) {
                    transactionManager.rollback(transactionStatus);
                    log.error("定时任务执行出错：", e);
                    log.info(">>>>>>>>>>>>定时任务事务回滚！<<<<<<<<<<<<<<<");
                }
            }
        } else {
            try {
                result = method.invoke(bean);
            } catch (Exception e) {
                log.error("定时任务执行出错：", e);
            }
        }
        return result;
    }

    public static Object invoke(Object bean, Method method, Object... obj) {
        Object result = null;
        if (method.isAnnotationPresent(Transactional.class)) {
            PlatformTransactionManager transactionManager = SpringContextUtils.getBean(PlatformTransactionManager.class);
            if (transactionManager != null) {
                DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
                TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
                try {
                    log.info(">>>>>>>>>>>>定时任务开启事务！<<<<<<<<<<<<<<<");
                    result = method.invoke(bean, obj);
                    transactionManager.commit(transactionStatus);
                    log.info(">>>>>>>>>>>>定时任务提交事务！<<<<<<<<<<<<<<<");
                } catch (Exception e) {
                    transactionManager.rollback(transactionStatus);
                    log.error("定时任务执行出错：", e);
                    log.info(">>>>>>>>>>>>定时任务事务回滚！<<<<<<<<<<<<<<<");
                }
            }
        } else {
            try {
                result = method.invoke(bean, obj);
            } catch (Exception e) {
                log.error("定时任务执行出错：", e);
            }
        }
        return result;
    }


}
