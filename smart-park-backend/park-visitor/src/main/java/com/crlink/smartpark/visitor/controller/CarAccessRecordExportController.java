package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.annotations.CurrentUserParkOrgIds;
import com.crlink.smartpark.visitor.service.CarAccessRecordRelService;
import com.crlink.smartpark.visitor.service.CarAccessRecordService;
import com.crlink.smartpark.visitor.query.CarAccessFullRecordListQuery;
import com.crlink.smartpark.visitor.query.CarAccessRecordListQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 车牌抓拍记录导出控制器
 * */
@RestController
@RequestMapping("/carAccessRecord/export")
@Api(tags = "访客模块-车辆通行记录导出")
public class CarAccessRecordExportController {

    @Resource
    private CarAccessRecordService carAccessRecordService;
    @Resource
    private CarAccessRecordRelService carAccessRecordRelService;

    /**
     * 导出车牌抓拍记录
     */
    @ApiOperation(value = "导出车牌抓拍记录", tags = "车行管理")
    @RequestMapping(value = "/record", method = RequestMethod.POST)
    @CurrentUserParkOrgIds
    public void pageListRecord(@RequestBody @Valid CarAccessRecordListQuery query) {
        carAccessRecordService.exportListRecord(query);
    }

    /**
     * 导出车辆通行记录
     */
    @ApiOperation(value = "导出车辆通行记录", tags = "车行管理")
    @RequestMapping(value = "/fullRecord", method = RequestMethod.POST)
    @CurrentUserParkOrgIds
    public void pageListFullRecord(@RequestBody @Valid CarAccessFullRecordListQuery query) {
        carAccessRecordRelService.exportList(query);
    }


}
