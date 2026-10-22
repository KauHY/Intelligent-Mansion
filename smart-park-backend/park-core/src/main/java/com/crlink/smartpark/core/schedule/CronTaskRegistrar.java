package com.crlink.smartpark.core.schedule;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CronTaskRegistrar implements DisposableBean {


    private final Map<String, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>();


    public void addCronTask(String taskId, Runnable runnable, String expression, TaskScheduler taskScheduler) {
        MyCronTask cronTask = new MyCronTask(taskId, runnable, expression);
        addCronTask(cronTask, taskScheduler);
    }


    private void addCronTask(MyCronTask cronTask, TaskScheduler taskScheduler) {
        if (cronTask != null) {
            String taskId = cronTask.getTaskId();
            if (this.scheduledTaskMap.containsKey(taskId)) {
                removeCronTask(taskId);
            }
            //添加
            this.scheduledTaskMap.put(taskId, scheduleCronTask(cronTask, taskScheduler));
        }
    }

    /**
     * 移除定时任务
     */
    public void removeCronTask(String taskId) {
        ScheduledTask scheduledTask = this.scheduledTaskMap.remove(taskId);
        if (scheduledTask != null)
            scheduledTask.cancel();
    }

    public ScheduledTask scheduleCronTask(MyCronTask cronTask, TaskScheduler taskScheduler) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }

    @Override
    public void destroy() throws Exception {
        for (ScheduledTask task : this.scheduledTaskMap.values()) {
            task.cancel();
        }
        this.scheduledTaskMap.clear();
    }
}
