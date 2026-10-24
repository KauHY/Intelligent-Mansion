package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.service.MeetingRentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会议室租赁占用统计
 *
 */
@Slf4j
@Api(value = "会议室租赁占用统计", tags = "会议室租赁占用统计")
@RestController
@RequestMapping("/meeting-statistics")
public class MeetingStatisticsController {

    @Autowired
    private MeetingRentService meetingRentService;

    @ApiOperation(value = "获取统计", tags = "获取统计")
    @GetMapping("/{parkOrgId}/get")
    public ResponseResult<Object> get(@PathVariable String parkOrgId) {
        return ResponseResult.success(meetingRentService.getMeetingOccupationRatio(parkOrgId));
    }
}
