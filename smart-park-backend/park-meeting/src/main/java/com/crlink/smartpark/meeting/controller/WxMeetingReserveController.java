package com.crlink.smartpark.meeting.controller;

import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.entity.MeetingReserve;
import com.crlink.smartpark.meeting.model.dto.MeetingReserveGetDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingTimeGetDTO;
import com.crlink.smartpark.meeting.model.query.MeetingReserveQuery;
import com.crlink.smartpark.meeting.model.vo.MeetingByBuildingVO;
import com.crlink.smartpark.meeting.model.vo.MeetingReserveVO;
import com.crlink.smartpark.meeting.model.vo.ReserveTimeVO;
import com.crlink.smartpark.meeting.service.MeetingPermissionService;
import com.crlink.smartpark.meeting.service.MeetingPlaneFigureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.model.dto.MeetingReserveAddDTO;
import com.crlink.smartpark.meeting.service.MeetingReserveService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * park-meeting 会议室预订
 * */
@Slf4j
@RestController
@RequestMapping("/wx-meeting-reserve")
@Api(tags = "会议室管理-C端小程序")
@Validated
public class WxMeetingReserveController {

    @Autowired
    private MeetingReserveService meetingReserveService;

    @Autowired
    private MeetingPlaneFigureService meetingPlaneFigureService;

    @Autowired
    private MeetingPermissionService meetingPermissionService;

    @ApiOperation(value = "会议室预订-根据园区ID小程序入口权限校验", tags = "会议室管理-C端小程序")
    @GetMapping("/checkPermission/{parkOrgId}")
    public ResponseResult<Boolean> checkPermission(@PathVariable("parkOrgId") @NotEmpty(message = "园区id不能为空") String parkOrgId) {
        return ResponseResult.success(meetingPermissionService.checkPermission(parkOrgId));
    }

    @ApiOperation(value = "会议室预订-新增", tags = "会议室管理-C端小程序")
    @PostMapping("/create")
    public ResponseResult<Object> create(@RequestBody @Valid MeetingReserveAddDTO reqDTO) {
        return ResponseResult.success(meetingReserveService.saveOrUpdateMeetingReserve(reqDTO));
    }

    @ApiOperation(value = "会议室预订-根据楼栋和时间查询空闲会议室", tags = "会议室管理-C端小程序")
    @PostMapping("/getMeeting")
    public ResponseResult<List<MeetingByBuildingVO>> getMeeting(@RequestBody @Valid MeetingReserveGetDTO reqDTO) {
        return ResponseResult.success(meetingReserveService.getMeeting(reqDTO));
    }

    @ApiOperation(value = "会议室预订-根据会议室查询对应不可用时段", tags = "会议室管理-C端小程序")
    @PostMapping("/getMeetingTime")
    public ResponseResult<List<ReserveTimeVO>> getMeetingTime(@RequestBody @Valid MeetingTimeGetDTO reqDTO) {
        return ResponseResult.success(meetingReserveService.getMeetingTime(reqDTO));
    }

    @ApiOperation(value = "会议室预订-列表查询", tags = "会议室管理-C端小程序")
    @PostMapping("/listMeetingReservePage")
    public ResponseResult<List<MeetingReserveVO>> listMeetingReservePage(@RequestBody @Valid MeetingReserveQuery query) {
        return ResponseResult.success(meetingReserveService.listMeetingReserve(query));
    }

    @ApiOperation(value = "会议室预订-根据ID查询详情", tags = "会议室管理-C端小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingReserve> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingReserveService.getMeetingReserveById(id));
    }

    @ApiOperation(value = "会议室预订-根据楼栋楼层查会议室分布图", tags = "会议室管理-C端小程序")
    @GetMapping("/getPlaneFigure/{buildingId}/{floorNum}")
    public ResponseResult<String> findPlaneFigure(
            @PathVariable("buildingId") @NotEmpty(message = "buildingId不能为空") String buildingId,
            @PathVariable("floorNum") @NotEmpty(message = "floorNum不能为空") String floorNum) {
        return ResponseResult.success(meetingPlaneFigureService.findPlaneFigure(buildingId, floorNum));
    }

}
