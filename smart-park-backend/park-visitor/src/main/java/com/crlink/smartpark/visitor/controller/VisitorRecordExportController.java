package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.constants.enums.VisitorRegisterTypeEnum;
import com.crlink.smartpark.visitor.query.VisitorRecordListQuery;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 访客记录控制器
 * */
@RestController
@RequestMapping("/visitorRecord/export")
@Api(tags = "访客模块-访客记录导出")
public class VisitorRecordExportController {

    @Resource
    private VisitorRecordService visitorRecordService;

    /**
     * 导出当前访客记录
     */
    @ApiOperation(value = "导出当前访客记录", tags = "访客记录")
    @GetMapping(value = "/currentVisitor")
    public void exportCurrentVisitor(@RequestBody @Valid VisitorRecordListQuery query) {
        visitorRecordService.exportVisitorRegister(query, VisitorRegisterTypeEnum.CURRENT);
    }

    /**
     * 导出历史访客记录
     */
    @ApiOperation(value = "导出历史访客记录", tags = "访客记录")
    @GetMapping(value = "/historyVisitor")
    public void exportListVisitorRegister(@RequestBody @Valid VisitorRecordListQuery query) {
        visitorRecordService.exportVisitorRegister(query, VisitorRegisterTypeEnum.HISTORY);
    }

    /**
     * 导出访客预约记录
     */
    @ApiOperation(value = "导出访客预约记录", tags = "访客记录")
    @GetMapping("/visitorSubscribe")
    public void exportListVisitorSubscribe(@RequestBody @Valid VisitorRecordListQuery query) {
        visitorRecordService.exportVisitorSubscribe(query);
    }

}
