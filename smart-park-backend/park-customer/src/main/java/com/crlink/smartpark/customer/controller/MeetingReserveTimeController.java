package com.crlink.smartpark.customer.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.model.query.MeetingReserveTimeQuery;
import com.crlink.smartpark.customer.model.MeetingReserveTimeReqDTO;
import com.crlink.smartpark.customer.dto.MeetingReserveTimeDTO;
import com.crlink.smartpark.customer.service.MeetingReserveTimeService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 租赁会议室预订时间管理
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-reserve-time")
@Validated
public class MeetingReserveTimeController {

    @Autowired
    private MeetingReserveTimeService meetingReserveTimeService;

//
//    @ApiOperation(value = "租赁会议室预订时间管理-新增", tags = "租赁会议室预订时间管理")
//    @PostMapping("/create")
//    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingReserveTimeReqDTO reqDTO) {
//        reqDTO.setId(null);
//        return ResponseResult.success(meetingReserveTimeService.saveOrUpdateMeetingReserveTime(reqDTO));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预订时间管理-删除", tags = "租赁会议室预订时间管理")
//    @DeleteMapping("/remove/{id}")
//    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(meetingReserveTimeService.removeById(id));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预订时间管理-根据ID查询", tags = "租赁会议室预订时间管理")
//    @GetMapping("/findById/{id}")
//    public ResponseResult<MeetingReserveTimeDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(meetingReserveTimeService.getMeetingReserveTimeById(id));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预订时间管理-修改", tags = "租赁会议室预订时间管理")
//    @PostMapping("/modify")
//    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingReserveTimeReqDTO reqDTO) {
//        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
//        return ResponseResult.success(meetingReserveTimeService.saveOrUpdateMeetingReserveTime(reqDTO));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预订时间管理-分页查询", tags = "租赁会议室预订时间管理")
//    @PostMapping("/listMeetingReserveTimePage")
//    public ResponseResult<PageInfo<MeetingReserveTimeDTO>> listMeetingReserveTimePage(@RequestBody @Valid MeetingReserveTimeQuery query){
//        return ResponseResult.success(meetingReserveTimeService.listMeetingReserveTimePage(query));
//    }

}

