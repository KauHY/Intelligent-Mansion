package com.crlink.smartpark.common.feign;

import com.crlink.smartpark.common.dto.TaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "park-common", path = "/smartPark/park-common/task", contextId = "TaskProvider")
public interface TaskProvider {

    @RequestMapping(value = "/getExecuteCondition", method = RequestMethod.GET)
    Set<TaskDTO> getExecuteCondition(@RequestParam("taskIds") Set<String> taskIds);

    @RequestMapping(value = "/getAllTask", method = RequestMethod.GET)
    Set<TaskDTO> getAllTask();

    @RequestMapping(value = "/{applicationName}/getScheduleTasks", method = RequestMethod.GET)
    Set<TaskDTO> getScheduleTasks(@PathVariable("applicationName") String applicationName);

}
