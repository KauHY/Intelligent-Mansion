package com.crlink.smartpark.inspection.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.inspection.controller.request.InspectionPlanReqDTO;
import com.crlink.smartpark.inspection.controller.request.PlanQuery;
import com.crlink.smartpark.inspection.controller.request.PlanRelateEmployeeReqDTO;
import com.crlink.smartpark.inspection.controller.request.StatusReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionPlanDTO;
import com.crlink.smartpark.inspection.service.InspectionPlanService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.crlink.smartpark.inspection.common.Constant.InspectionPlanStatus.DELETED;

/**
 * park-inspection
 * 巡检计划 前端控制器
 * */
@Api(tags = "巡检管理-巡检计划")
@Slf4j
@RestController
@RequestMapping("/inspection/inspection-plan")
public class InspectionPlanController {

    @Autowired
    private InspectionPlanService inspectionPlanService;


    @ApiOperation(value = "巡检计划-新增", tags = "巡检计划")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid InspectionPlanReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = inspectionPlanService.saveOrUpdatePlan(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检计划-删除", tags = "巡检计划")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = inspectionPlanService.updatePlanStatus(id, DELETED.status());
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检计划-根据ID查询", tags = "巡检计划")
    @GetMapping("/findById/{id}")
    public ResponseResult<InspectionPlanDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        InspectionPlanDTO result = inspectionPlanService.findById(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检计划-修改", tags = "巡检计划")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid InspectionPlanReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        Boolean result = inspectionPlanService.saveOrUpdatePlan(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检计划-更改巡检状态", tags = "巡检计划")
    @PostMapping("/updatePlanStatus")
    public ResponseResult<Boolean> updatePlanStatus(@RequestBody @Validated StatusReqDTO reqDTO) {
        Boolean result = inspectionPlanService.updatePlanStatus(reqDTO.getId(), reqDTO.getStatus());
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检计划-分页查询", tags = "巡检计划")
    @PostMapping("/listPlanPage")
    public ResponseResult<PageInfo<InspectionPlanDTO>> listPlanPage(@RequestBody PlanQuery query) {
        PageInfo<InspectionPlanDTO> result = inspectionPlanService.listPlanPage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检计划-关联巡检人", tags = "巡检计划")
    @PostMapping("/planRelateEmployees")
    public ResponseResult<Boolean> planRelateEmployees(@RequestBody PlanRelateEmployeeReqDTO dto) {
        Boolean result = inspectionPlanService.planRelateEmployees(dto);
        return ResponseResult.success(result);
    }


}

