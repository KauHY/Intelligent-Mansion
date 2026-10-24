package com.crlink.smartpark.meeting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.controller.request.query.MeetingPermissionQuery;
import com.crlink.smartpark.meeting.model.dto.MeetingPermissionReqDTO;
import com.crlink.smartpark.meeting.dto.MeetingPermissionDTO;
import com.crlink.smartpark.meeting.service.MeetingPermissionService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-meeting
 * 会议室权限关联
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-permission")
//@Api(tags = "会议室管理-会议室权限关联")
@Validated
public class MeetingPermissionController {

    @Autowired
    private MeetingPermissionService meetingPermissionService;


//    @ApiOperation(value = "会议室权限关联-新增", tags = "会议室权限关联")
//    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingPermissionReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(meetingPermissionService.saveOrUpdateMeetingPermission(reqDTO));
    }


//    @ApiOperation(value = "会议室权限关联-删除", tags = "会议室权限关联")
//    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingPermissionService.removeById(id));
    }


//    @ApiOperation(value = "会议室权限关联-根据ID查询", tags = "会议室权限关联")
//    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingPermissionDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingPermissionService.getMeetingPermissionById(id));
    }


//    @ApiOperation(value = "会议室权限关联-修改", tags = "会议室权限关联")
//    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingPermissionReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meetingPermissionService.saveOrUpdateMeetingPermission(reqDTO));
    }


//    @ApiOperation(value = "会议室权限关联-分页查询", tags = "会议室权限关联")
//    @PostMapping("/listMeetingPermissionPage")
    public ResponseResult<PageInfo<MeetingPermissionDTO>> listMeetingPermissionPage(@RequestBody @Valid MeetingPermissionQuery query){
        return ResponseResult.success(meetingPermissionService.listMeetingPermissionPage(query));
    }

}

