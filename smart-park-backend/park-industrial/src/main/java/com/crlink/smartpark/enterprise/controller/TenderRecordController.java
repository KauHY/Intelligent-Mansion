package com.crlink.smartpark.enterprise.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.enterprise.model.query.TenderRecordQuery;
import com.crlink.smartpark.enterprise.model.TenderRecordReqDTO;
import com.crlink.smartpark.industrial.dto.TenderRecordDTO;
import com.crlink.smartpark.enterprise.service.TenderRecordService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-enterprise
 * 投标记录
 * */
@Slf4j
@RestController
@RequestMapping("/tender-record")
@Validated
@Api(tags = "产业服务")
public class TenderRecordController {

    @Autowired
    private TenderRecordService tenderRecordService;


    @ApiOperation(value = "投标记录-根据ID查询", tags = "投标记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<TenderRecordDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(tenderRecordService.getTenderRecordById(id));
    }

    @ApiOperation(value = "投标记录-确定", tags = "投标记录")
    @GetMapping("/confirm/{id}")
    public ResponseResult<Boolean> confirm(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(tenderRecordService.confirm(id));
    }


    @ApiOperation(value = "投标记录-修改", tags = "投标记录")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid TenderRecordReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(tenderRecordService.saveOrUpdateTenderRecord(reqDTO));
    }


    @ApiOperation(value = "投标记录-分页查询", tags = "投标记录")
    @PostMapping("/listTenderRecordPage")
    public ResponseResult<PageInfo<TenderRecordDTO>> listTenderRecordPage(@RequestBody @Valid TenderRecordQuery query){
        return ResponseResult.success(tenderRecordService.listTenderRecordPage(query));
    }

    @ApiOperation(value = "投标记录-导出", tags = "投标记录")
    @PostMapping("/export")
    public void export(@RequestBody @Valid TenderRecordQuery query) {
        tenderRecordService.export(query);
    }

}

