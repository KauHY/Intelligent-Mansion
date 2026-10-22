package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.FireWarningRecordDTO;
import com.crlink.smartpark.device.model.FireWarningRecordHandleReqDTO;
import com.crlink.smartpark.device.model.query.FireWarningRecordQuery;
import com.crlink.smartpark.device.service.FireWarningRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * park-customer
 * 报警记录
 * */
@Slf4j
@RestController
@RequestMapping("/fireWarningRecord")
@Api(tags = "智慧消防")
@Validated
public class FireWarningRecordController {

    @Autowired
    private FireWarningRecordService fireWarningRecordService;


    @ApiOperation(value = "报警记录-根据ID查询", tags = "报警记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<FireWarningRecordDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(fireWarningRecordService.getFireWarningRecordById(id));
    }


    @ApiOperation(value = "报警处理", tags = "报警记录")
    @PostMapping("/handle")
    public ResponseResult<Boolean> handleFireRecord(@RequestBody @Valid FireWarningRecordHandleReqDTO reqDTO) {
        return ResponseResult.success(fireWarningRecordService.handleFireRecord(reqDTO));
    }


    @ApiOperation(value = "报警记录-分页查询", tags = "报警记录")
    @PostMapping("/listFireWarningRecordPage")
    public ResponseResult<PageInfo<FireWarningRecordDTO>> listFireWarningRecordPage(@RequestBody @Valid FireWarningRecordQuery query) {
        query.setDeviceType(8);//消防设备
        return ResponseResult.success(fireWarningRecordService.listFireWarningRecordPage(query));
    }

}

