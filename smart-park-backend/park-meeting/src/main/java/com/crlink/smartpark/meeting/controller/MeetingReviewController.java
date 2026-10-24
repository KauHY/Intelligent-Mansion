package com.crlink.smartpark.meeting.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.controller.request.query.MeetingReviewQuery;
import com.crlink.smartpark.meeting.model.dto.MeetingReviewReqDTO;
import com.crlink.smartpark.meeting.dto.MeetingReviewDTO;
import com.crlink.smartpark.meeting.service.MeetingReviewService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-meeting
 * 会议室审核人关联
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-review")
@Validated
public class MeetingReviewController {

    @Autowired
    private MeetingReviewService meetingReviewService;


//    @ApiOperation(value = "会议室审核人关联-新增", tags = "会议室审核人关联")
//    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingReviewReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(meetingReviewService.saveOrUpdateMeetingReview(reqDTO));
    }


//    @ApiOperation(value = "会议室审核人关联-删除", tags = "会议室审核人关联")
//    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingReviewService.removeById(id));
    }


//    @ApiOperation(value = "会议室审核人关联-根据ID查询", tags = "会议室审核人关联")
//    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingReviewDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingReviewService.getMeetingReviewById(id));
    }


//    @ApiOperation(value = "会议室审核人关联-修改", tags = "会议室审核人关联")
//    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingReviewReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meetingReviewService.saveOrUpdateMeetingReview(reqDTO));
    }


//    @ApiOperation(value = "会议室审核人关联-分页查询", tags = "会议室审核人关联")
//    @PostMapping("/listMeetingReviewPage")
    public ResponseResult<PageInfo<MeetingReviewDTO>> listMeetingReviewPage(@RequestBody @Valid MeetingReviewQuery query){
        return ResponseResult.success(meetingReviewService.listMeetingReviewPage(query));
    }

}

