package com.crlink.smartpark.common.controller;

import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.dto.OperateLogDTO;
import com.crlink.smartpark.common.model.query.OperateLogQuery;
import com.crlink.smartpark.common.service.OperateLogService;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.annotation.Exclude;
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
 * park-common
 * 操作日志
 * */
@Slf4j
@RestController
@Api(tags = "操作日志")
@RequestMapping("/operate-log")
@Validated
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;


    @ApiOperation(value = "操作日志-新增", tags = "操作日志")
    @PostMapping("/create")
    @Exclude
    public ResponseResult<Boolean> create(@RequestBody @Valid OperateLogDTO reqDTO) {
        reqDTO.setId(null);
        AssertUtil.requireTrue(StrUtil.isAllNotBlank(reqDTO.getRequestUri(), reqDTO.getIp()), "10003");
        return ResponseResult.success(operateLogService.saveOperateLog(reqDTO));
    }


    @ApiOperation(value = "操作日志-删除", tags = "操作日志")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(operateLogService.removeById(id));
    }


    @ApiOperation(value = "操作日志-根据ID查询", tags = "操作日志")
    @GetMapping("/findById/{id}")
    public ResponseResult<OperateLogDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(operateLogService.getOperateLogById(id));
    }


    @ApiOperation(value = "操作日志-分页查询", tags = "操作日志")
    @PostMapping("/listOperateLogPage")
    public ResponseResult<PageInfo<OperateLogDTO>> listOperateLogPage(@RequestBody @Valid OperateLogQuery query) {
        return ResponseResult.success(operateLogService.listOperateLogPage(query));
    }

}

