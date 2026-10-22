package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.service.AttendanceRuleRelationService;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * park-attendance
 * 考勤规则关联员工
 * */
@Slf4j
@RestController
@RequestMapping("/attendance-rule-relation")
@Validated
@Api(tags = "考勤管理")
public class AttendanceRuleRelationController {

    @Autowired
    private AttendanceRuleRelationService attendanceRuleRelationService;

    @ApiOperation(value = "考勤规则关联员工-通过考勤规则ID查询所有员工")
    @GetMapping("/getAllEmployeeByRuleId/{ruleId}")
    public ResponseResult<List<DictVO>> getAllEmployeeByRuleId(@PathVariable String ruleId) {
        return ResponseResult.success(attendanceRuleRelationService.getAllEmployeeByRuleId(ruleId));
    }

}

