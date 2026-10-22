package com.crlink.smartpark.customer.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.crlink.smartpark.customer.model.MeetingReserveInsertDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.dto.MeetingReserveDTO;
import com.crlink.smartpark.customer.model.MeetingReserveReqDTO;
import com.crlink.smartpark.customer.model.query.MeetingReserveQuery;
import com.crlink.smartpark.customer.service.MeetingReserveService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * park-customer 租赁会议室预订
 * */
@Slf4j
@RestController
@RequestMapping("/meetingReserve/wx")
@Api(tags = "客户模块-会议室租赁预定-小程序")
@Validated
public class WxMeetingReserveController {

    @Autowired
    private MeetingReserveService meetingReserveService;

    @ApiOperation(value = "租赁会议室预订-新增预定", tags = "租赁会议室预订-小程序")
    @PostMapping("/create")
    public ResponseResult<String> create(@RequestBody @Valid MeetingReserveInsertDTO reqDTO) {
        return ResponseResult.success(meetingReserveService.saveMeetingReserve(reqDTO));
    }

    @ApiOperation(value = "租赁会议室预订-取消申请", tags = "租赁会议室预订")
    @GetMapping("/cancel/{id}")
    public ResponseResult<Boolean> cancel(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingReserveService.cancel(id));
    }

    @ApiOperation(value = "租赁会议室预订-订单详情根据ID查询", tags = "租赁会议室预订-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingReserveDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingReserveService.getMeetingReserveById(id));
    }

    @ApiOperation(value = "租赁会议室预订-订单查询", tags = "租赁会议室预订-小程序")
    @PostMapping("/listMeetingReservePage")
    public ResponseResult<PageInfo<MeetingReserveDTO>>
        listMeetingReservePage(@RequestBody @Valid MeetingReserveQuery query) {
        return ResponseResult.success(meetingReserveService.listWxMeetingReservePage(query));
    }

}
