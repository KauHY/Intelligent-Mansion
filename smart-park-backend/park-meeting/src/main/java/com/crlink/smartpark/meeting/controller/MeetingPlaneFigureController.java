package com.crlink.smartpark.meeting.controller;

import com.crlink.smartpark.meeting.model.query.MeetingPlaneFigureTreeQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.model.dto.MeetingPlaneFigureReqDTO;
import com.crlink.smartpark.meeting.vo.MeetingPlaneFigureVO;
import com.crlink.smartpark.meeting.service.MeetingPlaneFigureService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-meeting
 * 会议室平面图
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-plane-figure")
@Api(tags = "会议室管理-会议室平面图")
@Validated
public class MeetingPlaneFigureController {

    @Autowired
    private MeetingPlaneFigureService meetingPlaneFigureService;


    @ApiOperation(value = "会议室平面图-新增", tags = "会议室平面图")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingPlaneFigureReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(meetingPlaneFigureService.saveMeetingPlaneFigure(reqDTO));
    }


    @ApiOperation(value = "会议室平面图-删除", tags = "会议室平面图")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingPlaneFigureService.removeById(id));
    }


    @ApiOperation(value = "会议室平面图-根据ID查询", tags = "会议室平面图")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingPlaneFigureVO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingPlaneFigureService.getMeetingPlaneFigureById(id));
    }


    @ApiOperation(value = "会议室平面图-修改", tags = "会议室平面图")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingPlaneFigureReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meetingPlaneFigureService.saveMeetingPlaneFigure(reqDTO));
    }


    @ApiOperation(value = "会议室平面图-分页查询", tags = "会议室平面图")
    @PostMapping("/listMeetingPlaneFigurePage")
    public ResponseResult<PageInfo<MeetingPlaneFigureVO>> listMeetingPlaneFigurePage(@RequestBody @Valid MeetingPlaneFigureTreeQuery query){
        return ResponseResult.success(meetingPlaneFigureService.listMeetingPlaneFigurePage(query));
    }

}

