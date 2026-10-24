package com.crlink.smartpark.common.controller;

import com.crlink.smartpark.common.dto.TaskDTO;
import com.crlink.smartpark.common.service.TaskService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * park-common
 * 后台任务 前端控制器
 * */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    /**
     * 后台任务
     */
    @Autowired
    private TaskService taskService;


    @ApiOperation(value = "获取后台任务执行条件", tags = "后台任务-内部接口")
    @GetMapping("/getExecuteCondition")
    public Set<TaskDTO> getExecuteCondition(@RequestParam("taskIds") Set<String> taskIds) {
        Set<TaskDTO> result = taskService.getExecuteConditionByIds(taskIds);
        return result;
    }

    @ApiOperation(value = "获取后台任务", tags = "后台任务-内部接口")
    @GetMapping(value = "/getAllTask")
    public Set<TaskDTO> getAllTask() {
        Set<TaskDTO> result = taskService.getAllTask();
        return result;
    }

    @ApiOperation(value = "获取后台任务", tags = "后台任务-内部接口")
    @GetMapping(value = "/{applicationName}/getScheduleTasks")
    public Set<TaskDTO> getScheduleTasks(@PathVariable String applicationName) {
        return taskService.getScheduleTasks(applicationName);
    }
}

