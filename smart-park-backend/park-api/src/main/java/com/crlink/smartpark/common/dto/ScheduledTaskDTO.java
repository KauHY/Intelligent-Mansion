package com.crlink.smartpark.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScheduledTaskDTO implements Serializable {

    private String taskId;
    private Runnable runnable;
}
