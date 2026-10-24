package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.dto.CarAccessRecordDTO;
import com.crlink.smartpark.visitor.query.CarAccessFullRecordListQuery;
import com.crlink.smartpark.visitor.query.CarAccessRecordListQuery;
import com.crlink.smartpark.visitor.service.CarAccessRecordRelService;
import com.crlink.smartpark.visitor.service.CarAccessRecordService;
import com.crlink.smartpark.visitor.vo.CarAccessFullRecordListVO;
import com.crlink.smartpark.visitor.vo.CarAccessRecordListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 车牌抓拍记录控制器
 * */
@RestController
@RequestMapping("/carAccessRecord")
@Api(tags = "访客模块-车辆通行记录")
public class CarAccessRecordController {

    @Resource
    private CarAccessRecordService carAccessRecordService;
    @Resource
    private CarAccessRecordRelService carAccessRecordRelService;

    /**
     * 分页查询车牌抓拍记录
     */
    @ApiOperation(value = "分页查询车牌抓拍记录", tags = "车行管理")
    @PostMapping("/pageListRecord")
    public ResponseResult<PageInfo<CarAccessRecordListVO>> pageListRecord(@RequestBody @Valid CarAccessRecordListQuery query) {
        return ResponseResult.success(carAccessRecordService.pageListRecord(query));
    }

    /**
     * 分页查询车辆通行记录
     */
    @ApiOperation(value = "分页查询车辆通行记录", tags = "车行管理")
    @PostMapping("/pageListFullRecord")
    public ResponseResult<PageInfo<CarAccessFullRecordListVO>> pageListFullRecord(@RequestBody @Valid CarAccessFullRecordListQuery query) {
        return ResponseResult.success(carAccessRecordRelService.pageList(query));
    }

    @ApiOperation(value = "保存车辆通行（用于通行事件）", tags = "车行管理")
    @PostMapping("/saveEventRecord")
    public Boolean saveEventRecord(@RequestBody CarAccessRecordDTO recordDTO) {
        return carAccessRecordService.saveEventRecord(recordDTO);
    }

}
