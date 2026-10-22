package com.crlink.smartpark.inspection.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.inspection.controller.request.TaskQuery;
import com.crlink.smartpark.inspection.controller.request.TaskRotateReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionTaskDTO;
import com.crlink.smartpark.inspection.dto.InspectionTaskDetailVO;
import com.crlink.smartpark.inspection.service.InspectionTaskService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * park-inspection
 * 前端控制器
 * */
@Api(tags = "巡检管理-巡检任务")
@Slf4j
@RestController
@RequestMapping("/inspection/inspection-task")
public class InspectionTaskController {

    @Autowired
    private InspectionTaskService inspectionTaskService;


    @ApiOperation(value = "巡检任务详情", tags = "")
    @GetMapping("/findTaskById/{id}")
    public ResponseResult<InspectionTaskDTO> findTaskById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        InspectionTaskDTO result = inspectionTaskService.findTaskById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "任务流转", tags = "")
    @PostMapping("/rotateTask")
    public ResponseResult<Boolean> rotateTask(@RequestBody @Validated TaskRotateReqDTO dto) {
        Boolean result = inspectionTaskService.rotateTask(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "分页查询任务", tags = "")
    @PostMapping("/listTaskPage")
    public ResponseResult<PageInfo<InspectionTaskDTO>> listTaskPage(@RequestBody TaskQuery query) {
        PageInfo<InspectionTaskDTO> result = inspectionTaskService.listTaskPage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "当天巡检情况", tags = "运营大屏")
    @GetMapping("/todayTaskCount")
    public ResponseResult<Map<Integer, Long>> getTodayTaskCount(String parkOrgId) {
        Map<Integer, Long> result = inspectionTaskService.getTodayTaskCount(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "3天内的异常巡检情况", tags = "运营大屏")
    @GetMapping("/3DayExceptionalTaskDetail")
    public ResponseResult<List<InspectionTaskDetailVO>> get3DayExceptionalTaskDetail(String parkOrgId) {
        List<InspectionTaskDetailVO> result = inspectionTaskService.get3DayExceptionalTaskDetail(parkOrgId);
        return ResponseResult.success(result);
    }

}

