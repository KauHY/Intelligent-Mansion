package com.crlink.smartpark.meeting.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.dto.MeetingManageDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingManageReqDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingManageStatusDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingOverviewDTO;
import com.crlink.smartpark.meeting.model.query.MeetingManageTreeQuery;
import com.crlink.smartpark.meeting.service.MeetingManageService;
import com.crlink.smartpark.meeting.vo.MeetingManageVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * park-meeting
 * 会议室管理
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-manage")
@Api(tags = "会议室管理-会议室管理")
@Validated
public class MeetingManageController {

    @Autowired
    private MeetingManageService meetingManageService;


    @ApiOperation(value = "会议室管理-新增", tags = "会议室管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingManageReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(meetingManageService.saveOrUpdateMeetingManage(reqDTO));
    }


    @ApiOperation(value = "会议室管理-删除", tags = "会议室管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingManageService.removeById(id));
    }


    @ApiOperation(value = "会议室管理-根据ID查询", tags = "会议室管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingManageDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingManageService.getMeetingManageById(id));
    }


    @ApiOperation(value = "会议室管理-修改", tags = "会议室管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingManageReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meetingManageService.saveOrUpdateMeetingManage(reqDTO));
    }

    @ApiOperation(value = "会议室管理-启用禁用", tags = "会议室管理")
    @PostMapping("/changeStatus")
    public ResponseResult<Boolean> changeStatus(@RequestBody @Valid MeetingManageStatusDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");

        return ResponseResult.success(meetingManageService.changeStatusMeetingManage(reqDTO));
    }


    @ApiOperation(value = "会议室管理-分页查询", tags = "会议室管理")
    @PostMapping("/listMeetingManagePage")
    public ResponseResult<PageInfo<MeetingManageVO>> listMeetingManagePage(@RequestBody @Valid MeetingManageTreeQuery query) {
        return ResponseResult.success(meetingManageService.listMeetingManagePage(query));
    }

    @ApiOperation(value = "会议室预订-组织树-根据园区ID拉取存在会议室的楼栋", tags = "会议室管理-C端小程序")
    @GetMapping("/listBuildingDict/{parkOrgId}")
    public ResponseResult<List<DictVO>> listBuildingDict(@PathVariable("parkOrgId") @NotEmpty String parkOrgId) {
        List<DictVO> result = meetingManageService.listBuildingDict(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "会议室预订-组织树-根据楼栋ID拉取存在会议室的楼层", tags = "会议室管理-C端小程序")
    @GetMapping("/listFloorDict/{buildingId}")
    public ResponseResult<List<DictVO>> listBusinessHouseInfoDict(@PathVariable("buildingId") String buildingId) {
        List<DictVO> result = null;//houseInfoService.listBusinessHouseInfoDict(buildingId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "会议室预订-预定概览,idType=1 会议室id，idType 其他,设备id", tags = "会议室管理-会议室信息屏")
    @GetMapping("/overviewMeeting")
    public ResponseResult<MeetingOverviewDTO> overviewMeeting(@RequestParam("id") String id,
                                                              @RequestParam(value = "idType", required = false) Integer idType) {
        MeetingOverviewDTO result = meetingManageService.getMeetingOverview(id, idType);
        return ResponseResult.success(result);
    }


}

