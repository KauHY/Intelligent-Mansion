package com.crlink.smartpark.customer.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.model.MeetingReserveReviewDTO;
import com.crlink.smartpark.customer.model.query.MeetingRentWxQuery;
import com.crlink.smartpark.customer.model.vo.MeetingRentDetailWxVO;
import com.crlink.smartpark.customer.model.vo.MeetingRentReserveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.query.MeetingRentStatusDTO;
import com.crlink.smartpark.customer.dto.MeetingRentDTO;
import com.crlink.smartpark.customer.model.MeetingRentReqDTO;
import com.crlink.smartpark.customer.model.query.MeetingRentQuery;
import com.crlink.smartpark.customer.model.vo.MeetingRentDetailVO;
import com.crlink.smartpark.customer.service.MeetingRentService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * park-customer
 * 会议室租赁管理
 * */
@Slf4j
@RestController
@RequestMapping("/meetingRent/wx")
@Validated
@Api(tags = "客户模块-会议室租赁管理-小程序")
public class WxMeetingRentController {

    @Autowired
    private MeetingRentService meetingRentService;

    @ApiOperation(value = "会议室租赁管理-根据ID查询", tags = "会议室租赁管理-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingRentDetailWxVO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingRentService.getWxMeetingRentById(id));
    }


    @ApiOperation(value = "会议室租赁管理-分页查询可预定会议室列表（token存parkOrgId、employeeId）", tags = "会议室租赁管理-小程序")
    @PostMapping("/listMeetingRentPage")
    public ResponseResult<PageInfo<MeetingRentDTO>> listMeetingRentPage(@RequestBody @Valid BaseQuery query){
        return ResponseResult.success(meetingRentService.listWxMeetingRentPage(query));
    }

    @ApiOperation(value = "会议室租赁管理-获取会议室预定数据(日期列表)（token存parkOrgId、employeeId）", tags = "会议室租赁管理-小程序")
    @PostMapping("/getMeetingRentReserve")
    public ResponseResult<MeetingRentReserveVO> getMeetingRentReserve(@RequestBody @Valid MeetingRentWxQuery query){
        return ResponseResult.success(meetingRentService.getWxMeetingRentReserve(query));
    }


}

