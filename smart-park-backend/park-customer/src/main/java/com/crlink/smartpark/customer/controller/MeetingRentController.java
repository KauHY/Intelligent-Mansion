package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.customer.controller.request.query.MeetingRentStatusDTO;
import com.crlink.smartpark.customer.model.vo.MeetingRentDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.model.query.MeetingRentQuery;
import com.crlink.smartpark.customer.model.MeetingRentReqDTO;
import com.crlink.smartpark.customer.dto.MeetingRentDTO;
import com.crlink.smartpark.customer.service.MeetingRentService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 会议室租赁管理
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-rent")
@Validated
@Api(tags = "客户模块-会议室租赁管理")
public class MeetingRentController {

    @Autowired
    private MeetingRentService meetingRentService;


    @ApiOperation(value = "会议室租赁管理-新增", tags = "会议室租赁管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MeetingRentReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(meetingRentService.saveOrUpdateMeetingRent(reqDTO));
    }


    @ApiOperation(value = "会议室租赁管理-删除", tags = "会议室租赁管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingRentService.removeByMeetingId(id));
    }


    @ApiOperation(value = "会议室租赁管理-根据ID查询", tags = "会议室租赁管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingRentDetailVO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingRentService.getMeetingRentById(id));
    }


    @ApiOperation(value = "会议室租赁管理-修改", tags = "会议室租赁管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MeetingRentReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meetingRentService.saveOrUpdateMeetingRent(reqDTO));
    }

    @ApiOperation(value = "会议室租赁管理-禁启用", tags = "会议室租赁管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@RequestBody @Valid MeetingRentStatusDTO reqDTO) {
        return ResponseResult.success(meetingRentService.updateStatus(reqDTO));
    }


    @ApiOperation(value = "会议室租赁管理-分页查询", tags = "会议室租赁管理")
    @PostMapping("/listMeetingRentPage")
    public ResponseResult<PageInfo<MeetingRentDTO>> listMeetingRentPage(@RequestBody @Valid MeetingRentQuery query){
        return ResponseResult.success(meetingRentService.listMeetingRentPage(query));
    }

}

