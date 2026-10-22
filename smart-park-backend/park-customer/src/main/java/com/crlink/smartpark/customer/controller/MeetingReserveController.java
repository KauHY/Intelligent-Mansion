package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.dto.MeetingReserveDTO;
import com.crlink.smartpark.customer.model.MeetingReserveReviewDTO;
import com.crlink.smartpark.customer.model.query.MeetingReserveQuery;
import com.crlink.smartpark.customer.service.MeetingReserveService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * park-customer 租赁会议室预订
 * */
@Slf4j
@RestController
@RequestMapping("/meeting-reserve")
@Validated
@Api(tags = "客户模块-会议室租赁预定")
public class MeetingReserveController {

    @Autowired
    private MeetingReserveService meetingReserveService;

    @ApiOperation(value = "租赁会议室预订-根据ID查询", tags = "租赁会议室预订")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeetingReserveDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meetingReserveService.getMeetingReserveById(id));
    }

    @ApiOperation(value = "租赁会议室预订-审核", tags = "租赁会议室预订")
    @PostMapping("/review")
    public ResponseResult<Boolean> review(@RequestBody @Valid MeetingReserveReviewDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meetingReserveService.review(reqDTO));
    }

    @ApiOperation(value = "租赁会议室预订-分页查询", tags = "租赁会议室预订")
    @PostMapping("/listMeetingReservePage")
    public ResponseResult<PageInfo<MeetingReserveDTO>>
    listMeetingReservePage(@RequestBody @Valid MeetingReserveQuery query) {
        return ResponseResult.success(meetingReserveService.listMeetingReservePage(query));
    }

    @ApiOperation(value = "租赁会议室预订-导出", tags = "租赁会议室预订")
    @PostMapping("/export")
    public void export(@RequestBody @Valid MeetingReserveQuery query) {
        meetingReserveService.export(query);
    }

    @ApiOperation(value = "根据园区获取场地预定费用数据", tags = "内部接口")
    @GetMapping("/getMeetingReserveBillList")
    public List<MeetingReserveDTO> getMeetingReserveBillList(@RequestParam("parkOrgId") String parkOrgId,
                                                             @RequestParam("nearTwelveMonths") Boolean nearTwelveMonths) {
        return meetingReserveService.getMeetingReserveBillList(parkOrgId, nearTwelveMonths);
    }
}
