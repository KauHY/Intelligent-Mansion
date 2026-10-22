package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.query.ActivityRegisterRecordQuery;
import com.crlink.smartpark.customer.dto.ActivityRegisterRecordDTO;
import com.crlink.smartpark.customer.service.ActivityRegisterRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * park-customer
 * 报名记录
 * */
@Slf4j
@RestController
@RequestMapping("/activity-register-record")
@Api(tags = "活动管理-活动-报名")
public class ActivityRegisterRecordController {

    @Autowired
    private ActivityRegisterRecordService activityRegisterRecordService;


    //@ApiOperation(value = "报名", tags = "报名记录")
    //@PostMapping("/create")
    //public ResponseResult<Boolean> create(@RequestBody @Valid ActivityRegisterRecordReqDTO reqDTO) {
    //    reqDTO.setId(null);
    //    return ResponseResult.success(activityRegisterRecordService.saveOrUpdateActivityRegisterRecord(reqDTO));
    //}
    //
    //
    //@ApiOperation(value = "取消报名-删除", tags = "报名记录")
    //@DeleteMapping("/remove/{id}")
    //public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
    //    return ResponseResult.success(activityRegisterRecordService.removeById(id));
    //}


    //@ApiOperation(value = "报名记录-根据ID查询", tags = "报名记录")
    //@GetMapping("/findById/{id}")
    //public ResponseResult<ActivityRegisterRecordDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
    //    return ResponseResult.success(activityRegisterRecordService.getActivityRegisterRecordById(id));
    //}


    //@ApiOperation(value = "报名记录-修改", tags = "报名记录")
    //@PostMapping("/modify")
    //public ResponseResult<Boolean> modify(@RequestBody @Valid ActivityRegisterRecordReqDTO reqDTO) {
    //    AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
    //    return ResponseResult.success(activityRegisterRecordService.saveOrUpdateActivityRegisterRecord(reqDTO));
    //}


    @ApiOperation(value = "报名记录-分页查询", tags = "报名记录")
    @PostMapping("/listActivityRegisterRecordPage")
    public ResponseResult<PageInfo<ActivityRegisterRecordDTO>> listActivityRegisterRecordPage(@RequestBody @Valid ActivityRegisterRecordQuery query) {
        return ResponseResult.success(activityRegisterRecordService.listActivityRegisterRecordPage(query));
    }

    @ApiOperation(value = "报名记录-导出", tags = "人行管理")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void export(@RequestBody ActivityRegisterRecordQuery query) {
        activityRegisterRecordService.export(query);
    }

}

