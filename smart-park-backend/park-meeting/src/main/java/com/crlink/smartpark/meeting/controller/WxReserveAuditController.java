package com.crlink.smartpark.meeting.controller;

import com.crlink.smartpark.meeting.model.dto.*;
import com.crlink.smartpark.meeting.vo.MeetingReserveDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.controller.request.query.ReserveAuditQuery;
import com.crlink.smartpark.meeting.dto.ReserveAuditDTO;
import com.crlink.smartpark.meeting.service.ReserveAuditService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

/**
 * park-meeting
 * 会议室预订审核
 * */
@Slf4j
@RestController
@RequestMapping("/wx-reserve-audit")
@Validated
@Api(tags = "会议室管理-C端小程序")
public class WxReserveAuditController {

    @Autowired
    private ReserveAuditService reserveAuditService;


    @ApiOperation(value = "我的预定-分页查询", tags = "我的预定")
    @PostMapping("/listReserveAuditPage")
    public ResponseResult<PageInfo<ReserveAuditDTO>> listReserveAuditPage(@RequestBody @Valid ReserveAuditQuery query){
        return ResponseResult.success(reserveAuditService.listReserveAuditPage(query));
    }

    @ApiOperation(value = "我的预定-预定详情", tags = "我的预定")
    @PostMapping("/findMyReserve")
    public ResponseResult<MeetingReserveDetailVO> findMyReserve(@RequestBody @Valid ReserveAuditDetailDTO reqDTO) {
        return ResponseResult.success(reserveAuditService.getReviewDetail(reqDTO));
    }

    @ApiOperation(value = "我的预定-变更会议时间", tags = "我的预定")
    @PostMapping("/modifyTime")
    public ResponseResult<Boolean> modifyTime(@RequestBody @Valid ReserveAuditUpdateDTO reqDTO) {
        return ResponseResult.success(reserveAuditService.modifyTime(reqDTO));
    }

    @ApiOperation(value = "我的预定-取消预定", tags = "我的预定")
    @PostMapping("/cancelReserve")
    public ResponseResult<Boolean> cancelReserve(@RequestBody @Valid ReserveAuditCancelDTO reqDTO) {
        return ResponseResult.success(reserveAuditService.cancelReserve(reqDTO));
    }

    @ApiOperation(value = "我的预定-新增会议纪要", tags = "我的预定")
    @PostMapping("/addSummary")
    public ResponseResult<Boolean> addSummary(@RequestBody @Valid ReserveSummaryDTO reqDTO) {
        return ResponseResult.success(reserveAuditService.addSummary(reqDTO));
    }

    @ApiOperation(value = "B端会议室预订审核-分页查询", tags = "会议室预订审核")
    @PostMapping("/myAuditPage")
    public ResponseResult<PageInfo<ReserveAuditDTO>> myAuditPage(@RequestBody @Valid ReserveAuditQuery query){
        return ResponseResult.success(reserveAuditService.myAuditPage(query));
    }

    @ApiOperation(value = "B端会议室预订审核-预定详情", tags = "会议室预订审核")
    @PostMapping("/getReviewDetail")
    public ResponseResult<MeetingReserveDetailVO> getReviewDetail(@RequestBody @Valid ReserveAuditDetailDTO reqDTO) {
        return ResponseResult.success(reserveAuditService.getReviewDetail(reqDTO));
    }

    @ApiOperation(value = "B端会议室预订审核-审核", tags = "会议室预订审核")
    @PostMapping("/review")
    public ResponseResult<Boolean> review(@RequestBody @Valid ReserveReviewDTO reqDTO) {
        return ResponseResult.success(reserveAuditService.review(reqDTO));
    }


}

