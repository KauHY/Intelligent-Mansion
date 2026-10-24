package com.crlink.smartpark.customer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.model.query.MeetingGoodsQuery;
import com.crlink.smartpark.customer.model.MeetingGoodsReqDTO;
import com.crlink.smartpark.customer.dto.MeetingGoodsDTO;
import com.crlink.smartpark.customer.service.MeetingGoodsService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 会议室物资管理
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-goods")
@Validated
@Api(tags = "客户模块-会议室物资管理")
public class MeetingGoodsController {

    @Autowired
    private MeetingGoodsService meetingGoodsService;


    @ApiOperation(value = "会议室物资管理-新增", tags = "会议室物资管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingGoodsReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(meetingGoodsService.saveOrUpdateMeetingGoods(reqDTO));
    }


    @ApiOperation(value = "会议室物资管理-删除", tags = "会议室物资管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingGoodsService.removeByGoodsId(id));
    }


    @ApiOperation(value = "会议室物资管理-根据ID查询", tags = "会议室物资管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingGoodsDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingGoodsService.getMeetingGoodsById(id));
    }


    @ApiOperation(value = "会议室物资管理-修改", tags = "会议室物资管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingGoodsReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meetingGoodsService.saveOrUpdateMeetingGoods(reqDTO));
    }


    @ApiOperation(value = "会议室物资管理-分页查询", tags = "会议室物资管理")
    @PostMapping("/listMeetingGoodsPage")
    public ResponseResult<PageInfo<MeetingGoodsDTO>> listMeetingGoodsPage(@RequestBody @Valid MeetingGoodsQuery query) {
        return ResponseResult.success(meetingGoodsService.listMeetingGoodsPage(query));
    }

}

