package com.crlink.smartpark.inspection.controller;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.inspection.controller.request.InspectionPointReqDTO;
import com.crlink.smartpark.inspection.controller.request.PointQuery;
import com.crlink.smartpark.inspection.dto.InspectionPointDTO;
import com.crlink.smartpark.inspection.service.InspectionPointService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * park-inspection
 * 巡检点表 前端控制器
 * */
@Api(tags = "巡检管理-巡检点")
@Slf4j
@RestController
@RequestMapping("/inspection/inspection-point")
public class InspectionPointController {


    @Autowired
    private InspectionPointService inspectionPointService;


    @ApiOperation(value = "巡检点表-新增", tags = "巡检点")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Validated InspectionPointReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = inspectionPointService.saveOrUpdatePoint(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检点表-删除", tags = "巡检点")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = inspectionPointService.deletedPointById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检点表-根据ID查询", tags = "巡检")
    @GetMapping("/findById/{id}")
    public ResponseResult<InspectionPointDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success();
    }


    @ApiOperation(value = "巡检点表-修改", tags = "巡检点")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Validated InspectionPointReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        Boolean result = inspectionPointService.saveOrUpdatePoint(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检点-分页查询", tags = "巡检点")
    @PostMapping("/listPointPage")
    public ResponseResult<PageInfo<InspectionPointDTO>> listPointPage(@RequestBody PointQuery pointQuery) {
        PageInfo<InspectionPointDTO> result = inspectionPointService.listPointPage(pointQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检点-导出", tags = "巡检点")
    @PostMapping("/export")
    public void export(@RequestBody PointQuery query) {
        inspectionPointService.export(query);
    }

    @ApiOperation(value = "巡检点-导入", tags = "巡检点")
    @PostMapping("/importPoint")
    public ResponseResult<FileImportErrorInfoDTO> export(@RequestPart("file") MultipartFile file, @RequestParam String parkOrgId) {
        FileImportErrorInfoDTO errorInfoDTO = inspectionPointService.importPoints(file, parkOrgId);
        return ResponseResult.success(errorInfoDTO);
    }

}

