package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.entity.AttendanceRule;
import com.crlink.smartpark.attendance.entity.PunchRecord;
import com.crlink.smartpark.attendance.model.PunchRecordReqDTO;
import com.crlink.smartpark.attendance.model.query.PunchRecordQuery;
import com.crlink.smartpark.attendance.model.vo.AttendanceRecordDateVO;
import com.crlink.smartpark.attendance.service.PunchRecordService;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.SimpleNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/wx/punch-record")
@Validated
@Api(tags = "考勤管理")
public class WxPunchRecordController {

    @Autowired
    private PunchRecordService punchRecordService;

    @ApiOperation(value = "移动打卡-获取当天打卡记录", tags = "移动打卡")
    @GetMapping("/getTodayRecord")
    public ResponseResult<List<PunchRecord>> getTodayRecord() {
        return ResponseResult.success(punchRecordService.getTodayRecord());
    }

    @ApiOperation(value = "移动打卡-新增", tags = "移动打卡")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid PunchRecordReqDTO reqDTO) {
        return ResponseResult.success(punchRecordService.saveOrUpdatePunchRecord(punchRecordService.wrapReq(reqDTO)));
    }

    @ApiOperation(value = "移动打卡-打卡明细", tags = "移动打卡")
    @PostMapping("/detail")
    public ResponseResult<Map<LocalDate, List<PunchRecord>>> detail(@RequestBody PunchRecordQuery query) {
        return ResponseResult.success(punchRecordService.listMonthDetail(query));
    }

    @ApiOperation(value = "移动打卡-判断是否有用户适应的考勤规则", tags = "移动打卡", notes = "true-有；false-无")
    @GetMapping("/hasRule")
    public ResponseResult<Boolean> hasRule() {
        return ResponseResult.success(punchRecordService.hasRule());
    }

    @ApiOperation(value = "移动打卡-获取用户适应的考勤规则", tags = "移动打卡")
    @GetMapping("/getRule")
    public ResponseResult<AttendanceRule> getRule() {
        return ResponseResult.success(punchRecordService.getRule());
    }

    @ApiOperation(value = "移动打卡-获取用户在当前园区下已配置考勤规则的公司", tags = "移动打卡", notes = "extraInfo:考勤规则对象-ruleData；对应员工Id-employeeId")
    @GetMapping("/getCompanySelectWithRule")
    public ResponseResult<List<SimpleNode>> getCompanySelectWithRule() {
        return ResponseResult.success(punchRecordService.getCompanySelectWithRule());
    }

    @ApiOperation(value = "移动打卡-获取某天打卡记录及结果", tags = "移动打卡")
    @GetMapping("/getRecordsByDate/{date}")
    public ResponseResult<AttendanceRecordDateVO> getRecordsByDate(@ApiParam("日期格式yyyy-MM-dd") @PathVariable String date) {
        return ResponseResult.success(punchRecordService.getRecordsByDate(date));
    }
}
