package com.crlink.smartpark.core.schedule;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.config.CronTask;

import javax.validation.constraints.NotNull;

public class MyCronTask extends CronTask {
    @Setter
    @Getter
    @NotNull
    private String taskId;

    public MyCronTask(String taskId, Runnable runnable, String expression) {
        super(runnable, expression);
        this.taskId = taskId;
    }

    public MyCronTask(Runnable runnable, String expression) {
        super(runnable, expression);
    }
}
