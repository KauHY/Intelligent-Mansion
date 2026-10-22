package com.crlink.smartpark.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.TaskDTO;
import com.crlink.smartpark.common.entity.Task;

import java.util.Set;

/**
 * <p>
 * 后台任务 服务类
 * </p>
 * */
public interface TaskService extends IService<Task> {

    Set<TaskDTO> getExecuteConditionByIds(Set<String> taskIds);

    Set<TaskDTO> getAllTask();

    Set<TaskDTO> getScheduleTasks(String applicationName);
    
}
