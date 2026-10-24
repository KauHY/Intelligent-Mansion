package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.controller.request.query.FieldworkApplyRecordQuery;
import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordDTO;
import com.crlink.smartpark.attendance.model.AuditReqDTO;
import com.crlink.smartpark.attendance.model.FieldworkApplyRecordReqDTO;
import com.crlink.smartpark.attendance.service.FieldworkApplyRecordService;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * park-attendance
 * 外勤申请记录 */
@Slf4j
@RestController
@RequestMapping("/wx/fieldwork-apply-record")
@Validated
@Api(tags = "考勤管理")
public class WxFieldworkApplyRecordController {

    @Autowired
    private FieldworkApplyRecordService fieldworkApplyRecordService;


    @ApiOperation(value = "外勤申请记录-新增", tags = "外勤申请记录")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid FieldworkApplyRecordReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(fieldworkApplyRecordService.saveOrUpdate(fieldworkApplyRecordService.wrapApplyReq(reqDTO)));
    }


    @ApiOperation(value = "外勤申请记录-根据ID查询", tags = "外勤申请记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<FieldworkApplyRecordDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(fieldworkApplyRecordService.getFieldworkApplyRecordById(id));
    }


    @ApiOperation(value = "外勤申请记录-取消", tags = "外勤申请记录")
    @PostMapping("/cancel")
    public ResponseResult<Boolean> cancel(@RequestParam @NotBlank(message = "id不能为空") String id) {
        return ResponseResult.success(fieldworkApplyRecordService.cancel(id));
    }

    @ApiOperation(value = "外勤申请记录-审批", tags = "外勤申请记录")
    @PostMapping("/audit")
    public ResponseResult<Boolean> audit(@RequestBody @Valid AuditReqDTO reqDTO) {
        return ResponseResult.success(fieldworkApplyRecordService.audit(reqDTO));
    }

    @ApiOperation(value = "外勤申请记录-分页查询-小程序", tags = "外勤申请记录")
    @PostMapping("/listFieldworkApplyRecordPage")
    public ResponseResult<PageInfo<FieldworkApplyRecordDTO>> listFieldworkApplyRecordPage(@RequestBody @Valid FieldworkApplyRecordQuery query){
        return ResponseResult.success(fieldworkApplyRecordService.listFieldworkApplyRecordPage(query));
    }

    @ApiOperation(value = "外勤申请记录-B端是否有权限审批", tags = "外勤申请记录")
    @GetMapping("/authority")
    public ResponseResult<Boolean> authority() {
        return ResponseResult.success(fieldworkApplyRecordService.authority());
    }
}

