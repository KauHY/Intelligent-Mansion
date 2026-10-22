package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.dto.AttendanceRuleDTO;
import com.crlink.smartpark.attendance.model.AttendanceRuleReqDTO;
import com.crlink.smartpark.attendance.model.AttendanceRuleStatusReqDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceRuleQuery;
import com.crlink.smartpark.attendance.service.AttendanceRuleService;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
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
import java.util.Set;

/**
 * park-attendance
 * 考勤规则
 * */
@Slf4j
@RestController
@RequestMapping("/attendance-rule")
@Validated
@Api(tags = "考勤管理")
public class AttendanceRuleController {

    @Autowired
    private AttendanceRuleService attendanceRuleService;


    @ApiOperation(value = "考勤规则-新增")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid AttendanceRuleReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(attendanceRuleService.saveOrUpdateAttendanceRule(reqDTO));
    }


    @ApiOperation(value = "考勤规则-删除")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(attendanceRuleService.removeById(id));
    }


    @ApiOperation(value = "考勤规则-根据ID查询")
    @GetMapping("/findById/{id}")
    public ResponseResult<AttendanceRuleDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(attendanceRuleService.getAttendanceRuleById(id));
    }


    @ApiOperation(value = "考勤规则-修改")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid AttendanceRuleReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(attendanceRuleService.saveOrUpdateAttendanceRule(reqDTO));
    }


    @ApiOperation(value = "考勤规则-分页查询")
    @PostMapping("/listAttendanceRulePage")
    public ResponseResult<PageInfo<AttendanceRuleDTO>> listAttendanceRulePage(@RequestBody @Valid AttendanceRuleQuery query) {
        return ResponseResult.success(attendanceRuleService.listAttendanceRulePage(query));
    }

    @ApiOperation(value = "考勤规则-启用/禁用")
    @PutMapping("/modifyStatus")
    public ResponseResult modifyStatus(@RequestBody @Valid AttendanceRuleStatusReqDTO dto) {
        attendanceRuleService.modifyStatus(dto);
        return ResponseResult.success();
    }

    @ApiOperation(value = "考勤规则-内部接口打卡提醒")
    @PostMapping("/punchReminder")
    public void punchReminder(@RequestParam(value = "ruleId") String ruleId) {
        attendanceRuleService.punchReminder(ruleId);
    }

    @ApiOperation(value = "考勤规则-发生提醒消息")
    @PostMapping("/sendMsg")
    public void sendMsg(@RequestBody Set<String> mobile) {
        attendanceRuleService.sendMsg(mobile);
    }

    @ApiOperation(value = "考勤规则-获取所有的规则")
    @PostMapping("/getAllRules")
    public List<AttendanceRuleDTO> getAllRules() {
        return attendanceRuleService.getAllRules();
    }
}

