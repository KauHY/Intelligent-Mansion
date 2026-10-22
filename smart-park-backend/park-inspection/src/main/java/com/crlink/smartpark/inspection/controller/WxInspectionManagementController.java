package com.crlink.smartpark.inspection.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.inspection.controller.request.TaskPointDetailReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionTaskDTO;
import com.crlink.smartpark.inspection.service.InspectionPointDetailService;
import com.crlink.smartpark.inspection.service.InspectionTaskService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 * */
@Api(tags = "巡检管理-运营小程序")
@Slf4j
@RestController
@RequestMapping("/inspection/wx")
public class WxInspectionManagementController {

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private InspectionPointDetailService inspectionPointDetailService;


    @ApiOperation(value = "任务分页查询：1待接任务，2今日任务，3历史任务", tags = "巡检管理-运营小程序")
    @PostMapping("/task/wxTaskList")
    public ResponseResult<PageInfo<InspectionTaskDTO>> wxTaskList(@RequestParam Integer listType, @RequestBody BaseQuery query) {
        PageInfo<InspectionTaskDTO> result = inspectionTaskService.wxTaskList(query, listType);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检任务详情", tags = "巡检管理-运营小程序")
    @GetMapping("/task/findTaskById/{id}")
    public ResponseResult<InspectionTaskDTO> findTaskById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        InspectionTaskDTO result = inspectionTaskService.findTaskById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "接单-接受任务", tags = "巡检管理-运营小程序")
    @PostMapping("/task/acceptTask/{id}")
    public ResponseResult<Boolean> acceptTask(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        Boolean result = inspectionTaskService.acceptTask(id, employeeId);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "关闭巡检点", tags = "巡检管理-运营小程序")
    @PostMapping("/point-detail/closePointDetail/{id}")
    public ResponseResult<Boolean> closePointDetail(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = inspectionPointDetailService.closePointDetail(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检点巡检", tags = "巡检管理-运营小程序")
    @PostMapping("/point-detail/reportPointDetail")
    public ResponseResult<Boolean> reportPointDetail(@RequestBody TaskPointDetailReqDTO detailDTO) {
        Boolean result = inspectionPointDetailService.reportPointDetail(detailDTO);
        return ResponseResult.success(result);
    }
}
