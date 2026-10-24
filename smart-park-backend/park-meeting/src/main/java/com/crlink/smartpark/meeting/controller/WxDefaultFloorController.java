package com.crlink.smartpark.meeting.controller;

import com.crlink.smartpark.meeting.model.dto.DefaultFloorReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.controller.request.query.DefaultFloorQuery;
import com.crlink.smartpark.meeting.dto.DefaultFloorDTO;
import com.crlink.smartpark.meeting.service.DefaultFloorService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-meeting 会议室默认楼层
 * */
@Slf4j
@RestController
@RequestMapping("/default-floor")
@Api(tags = "会议室管理-C端小程序")
@Validated
public class WxDefaultFloorController {

    @Autowired
    private DefaultFloorService defaultFloorService;

    // @ApiOperation(value = "会议室默认楼层-新增", tags = "会议室默认楼层")
//    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid DefaultFloorReqDTO reqDTO) {
        return ResponseResult.success(defaultFloorService.saveOrUpdateDefaultFloor(reqDTO));
    }

    @ApiOperation(value = "会议室默认楼层-设置当前园区下默认楼层", tags = "会议室默认楼层")
    @PostMapping("/settingDefault")
    public ResponseResult<String> settingDefault(@RequestBody @Valid DefaultFloorReqDTO reqDTO) {
        return ResponseResult.success(defaultFloorService.settingDefaultFloor(reqDTO));
    }

    @ApiOperation(value = "会议室默认楼层-取消设置默认楼层", tags = "会议室默认楼层")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(defaultFloorService.removeById(id));
    }

    @ApiOperation(value = "会议室默认楼层-根据员工ID查询默认楼层", tags = "会议室默认楼层")
    @GetMapping("/getDefaultFloor")
    public ResponseResult<DefaultFloorDTO> getDefaultFloor() {
        return ResponseResult.success(defaultFloorService.getDefaultFloor());
    }

    // @ApiOperation(value = "会议室默认楼层-根据ID查询", tags = "会议室默认楼层")
//    @GetMapping("/findById/{id}")
    public ResponseResult<DefaultFloorDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(defaultFloorService.getDefaultFloorById(id));
    }

    // @ApiOperation(value = "会议室默认楼层-修改", tags = "会议室默认楼层")
//    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid DefaultFloorReqDTO reqDTO) {
        // AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(defaultFloorService.saveOrUpdateDefaultFloor(reqDTO));
    }

    // @ApiOperation(value = "会议室默认楼层-分页查询", tags = "会议室默认楼层")
//    @PostMapping("/listDefaultFloorPage")
    public ResponseResult<PageInfo<DefaultFloorDTO>> listDefaultFloorPage(@RequestBody @Valid DefaultFloorQuery query) {
        return ResponseResult.success(defaultFloorService.listDefaultFloorPage(query));
    }

}
