package com.crlink.smartpark.meeting.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinMemberDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinMeetingDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinPersonDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinVerifyDTO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinAttendanceVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinPersonVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinRequestVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinResultVO;
import com.crlink.smartpark.meeting.service.FaceCheckinService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Face check-in controller for smart meeting attendance.
 */
@Slf4j
@RestController
@RequestMapping("/face-checkin")
@Api(tags = "人脸识别签到")
@Validated
public class FaceCheckinController {

    @Autowired
    private FaceCheckinService faceCheckinService;

    @ApiOperation(value = "人脸识别签到-核验", tags = "人脸识别签到")
    @PostMapping("/verify")
    public ResponseResult<FaceCheckinResultVO> verify(@RequestBody @Valid FaceCheckinVerifyDTO dto) {
        return ResponseResult.success(faceCheckinService.verify(dto));
    }

    @ApiOperation(value = "人脸识别签到-人员登记", tags = "人脸识别签到")
    @PostMapping("/person/save")
    public ResponseResult<Boolean> savePerson(@RequestBody @Valid FaceCheckinPersonDTO dto) {
        try {
            return ResponseResult.success(faceCheckinService.savePerson(dto));
        } catch (IllegalArgumentException e) {
            return ResponseResult.errorMsg(e.getMessage());
        }
    }

    @ApiOperation(value = "人脸识别签到-人员列表", tags = "人脸识别签到")
    @GetMapping("/person/list")
    public ResponseResult<PageInfo<FaceCheckinPersonVO>> listPersons(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        return ResponseResult.success(faceCheckinService.listPersons(pageNum, pageSize, keyword));
    }

    @ApiOperation(value = "人脸识别签到-人员删除", tags = "人脸识别签到")
    @DeleteMapping("/person/remove/{id}")
    public ResponseResult<Boolean> removePerson(@PathVariable("id") String id) {
        return ResponseResult.success(faceCheckinService.removeById(id));
    }

    @ApiOperation(value = "人脸识别签到-会议成员维护", tags = "人脸识别签到")
    @PostMapping("/member/save")
    public ResponseResult<Boolean> saveMembers(@RequestBody @Valid FaceCheckinMemberDTO dto) {
        return ResponseResult.success(faceCheckinService.saveMembers(dto));
    }

    @ApiOperation(value = "人脸识别签到-会议成员查询", tags = "人脸识别签到")
    @GetMapping("/member/list")
    public ResponseResult<List<FaceCheckinPersonVO>> listMembers(@RequestParam("meetingReserveId") String meetingReserveId) {
        return ResponseResult.success(faceCheckinService.listMembers(meetingReserveId));
    }

    @ApiOperation(value = "人脸识别签到-签到记录查询", tags = "人脸识别签到")
    @GetMapping("/attendance/list")
    public ResponseResult<List<FaceCheckinAttendanceVO>> listAttendance(@RequestParam("meetingReserveId") String meetingReserveId) {
        return ResponseResult.success(faceCheckinService.listAttendance(meetingReserveId));
    }

    @ApiOperation(value = "人脸识别签到-上传记录", tags = "人脸识别签到")
    @GetMapping("/request/list")
    public ResponseResult<List<FaceCheckinRequestVO>> listRequests(
            @RequestParam(required = false) String meetingReserveId) {
        return ResponseResult.success(faceCheckinService.listRequests(meetingReserveId));
    }

    @ApiOperation(value = "人脸识别签到-会议列表", tags = "人脸识别签到")
    @GetMapping("/meeting/list")
    public ResponseResult<List<com.crlink.smartpark.meeting.entity.MeetingReserve>> listMeetings() {
        return ResponseResult.success(faceCheckinService.listMeetings());
    }

    @ApiOperation(value = "人脸识别签到-新增会议", tags = "人脸识别签到")
    @PostMapping("/meeting/save")
    public ResponseResult<Boolean> saveMeeting(@RequestBody @Valid FaceCheckinMeetingDTO dto) {
        try {
            return ResponseResult.success(faceCheckinService.saveMeeting(dto));
        } catch (IllegalArgumentException e) {
            return ResponseResult.errorMsg(e.getMessage());
        }
    }

    @ApiOperation(value = "人脸识别签到-修改会议", tags = "人脸识别签到")
    @PostMapping("/meeting/update")
    public ResponseResult<Boolean> updateMeeting(@RequestBody @Valid FaceCheckinMeetingDTO dto) {
        try {
            return ResponseResult.success(faceCheckinService.updateMeeting(dto));
        } catch (IllegalArgumentException e) {
            return ResponseResult.errorMsg(e.getMessage());
        }
    }

    @ApiOperation(value = "人脸识别签到-删除会议", tags = "人脸识别签到")
    @DeleteMapping("/meeting/remove/{id}")
    public ResponseResult<Boolean> removeMeeting(@PathVariable("id") String id) {
        return ResponseResult.success(faceCheckinService.removeMeeting(id));
    }
    @ApiOperation(value = "人脸识别签到-导出CSV", tags = "人脸识别签到")
    @GetMapping("/attendance/export")
    public ResponseResult<String> exportAttendance(@RequestParam("meetingReserveId") String meetingReserveId) {
        return ResponseResult.success(faceCheckinService.exportAttendanceCsv(meetingReserveId));
    }

    @ApiOperation(value = "人脸识别签到-审核人员", tags = "人脸识别签到")
    @PostMapping("/person/audit/{id}")
    public ResponseResult<Boolean> auditPerson(
            @PathVariable("id") String id,
            @RequestParam("auditStatus") Integer auditStatus) {
        return ResponseResult.success(faceCheckinService.auditPerson(id, auditStatus));
    }
    @ApiOperation(value = "人脸识别签到-未签到名单", tags = "人脸识别签到")
    @GetMapping("/attendance/absent")
    public ResponseResult<java.util.List<com.crlink.smartpark.meeting.model.vo.FaceCheckinAbsentVO>> listAbsent(@RequestParam("meetingReserveId") String meetingReserveId) {
        return ResponseResult.success(faceCheckinService.listAbsent(meetingReserveId));
    }
}


