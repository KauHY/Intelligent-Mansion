package com.crlink.smartpark.inspection.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.inspection.controller.request.TaskPointDetailReqDTO;
import com.crlink.smartpark.inspection.service.InspectionPointDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * park-inspection
 * 前端控制器
 * */
@Api(tags = "巡检管理-巡检任务的巡检点")
@Slf4j
@RestController
@RequestMapping("/inspection/inspection-point-detail")
public class InspectionPointDetailController {

//    @Autowired
//    private InspectionPointDetailService inspectionPointDetailService;
//
//    @ApiOperation(value = "运营小程序:关闭巡检点的巡检-接受任务", tags = "")
//    @PostMapping("/wx/closePointDetail/{id}")
//    public ResponseResult<Boolean> closePointDetail(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
//        Boolean result = inspectionPointDetailService.closePointDetail(id);
//        return ResponseResult.success(result);
//    }
//
//    @ApiOperation(value = "运营小程序:巡检点巡检", tags = "")
//    @PostMapping("/wx/reportPointDetail")
//    public ResponseResult<Boolean> reportPointDetail(@RequestBody TaskPointDetailReqDTO detailDTO) {
//        Boolean result = inspectionPointDetailService.reportPointDetail(detailDTO);
//        return ResponseResult.success(result);
//    }

}

