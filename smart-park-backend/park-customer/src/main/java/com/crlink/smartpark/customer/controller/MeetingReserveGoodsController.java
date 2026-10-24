package com.crlink.smartpark.customer.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.model.query.MeetingReserveGoodsQuery;
import com.crlink.smartpark.customer.model.MeetingReserveGoodsReqDTO;
import com.crlink.smartpark.customer.dto.MeetingReserveGoodsDTO;
import com.crlink.smartpark.customer.service.MeetingReserveGoodsService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 租赁会议室预定物资管理
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-reserve-goods")
@Validated
public class MeetingReserveGoodsController {

    @Autowired
    private MeetingReserveGoodsService meetingReserveGoodsService;


//    @ApiOperation(value = "租赁会议室预定物资管理-新增", tags = "租赁会议室预定物资管理")
//    @PostMapping("/create")
//    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingReserveGoodsReqDTO reqDTO) {
//        reqDTO.setId(null);
//        return ResponseResult.success(meetingReserveGoodsService.saveOrUpdateMeetingReserveGoods(reqDTO));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预定物资管理-删除", tags = "租赁会议室预定物资管理")
//    @DeleteMapping("/remove/{id}")
//    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(meetingReserveGoodsService.removeById(id));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预定物资管理-根据ID查询", tags = "租赁会议室预定物资管理")
//    @GetMapping("/findById/{id}")
//    public ResponseResult<MeetingReserveGoodsDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(meetingReserveGoodsService.getMeetingReserveGoodsById(id));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预定物资管理-修改", tags = "租赁会议室预定物资管理")
//    @PostMapping("/modify")
//    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingReserveGoodsReqDTO reqDTO) {
//        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
//        return ResponseResult.success(meetingReserveGoodsService.saveOrUpdateMeetingReserveGoods(reqDTO));
//    }
//
//
//    @ApiOperation(value = "租赁会议室预定物资管理-分页查询", tags = "租赁会议室预定物资管理")
//    @PostMapping("/listMeetingReserveGoodsPage")
//    public ResponseResult<PageInfo<MeetingReserveGoodsDTO>> listMeetingReserveGoodsPage(@RequestBody @Valid MeetingReserveGoodsQuery query){
//        return ResponseResult.success(meetingReserveGoodsService.listMeetingReserveGoodsPage(query));
//    }

}

