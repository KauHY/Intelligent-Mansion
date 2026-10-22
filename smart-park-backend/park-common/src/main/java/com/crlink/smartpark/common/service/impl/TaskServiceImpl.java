package com.crlink.smartpark.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.dto.TaskDTO;
import com.crlink.smartpark.common.entity.Task;
import com.crlink.smartpark.common.mapper.TaskMapper;
import com.crlink.smartpark.common.service.TaskService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台任务 服务实现类
 * </p>
 * */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Override
    public Set<TaskDTO> getExecuteConditionByIds(Set<String> taskIds) {
        Set<TaskDTO> list = this.lambdaQuery().in(Task::getId, taskIds).list().parallelStream()
                .map(t -> MyBeanUtils.copy(t, TaskDTO::new)).collect(Collectors.toSet());
        return list;
    }

    @Override
    public Set<TaskDTO> getAllTask() {
        Set<TaskDTO> list = this.lambdaQuery().list().parallelStream()
                .map(t -> MyBeanUtils.copy(t, TaskDTO::new)).collect(Collectors.toSet());
        return list;
    }

    @Override
    public Set<TaskDTO> getScheduleTasks(String applicationName) {
        Set<TaskDTO> list = this.lambdaQuery().eq(Task::getApplicationName, applicationName).list().parallelStream()
                .map(t -> MyBeanUtils.copy(t, TaskDTO::new)).collect(Collectors.toSet());
        return list;
    }
}
