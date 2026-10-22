package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.ActivityRegisterRecordReqDTO;
import com.crlink.smartpark.customer.controller.response.MyRegisterRecord;
import com.crlink.smartpark.customer.dto.ActivityDTO;
import com.crlink.smartpark.customer.service.ActivityRegisterRecordService;
import com.crlink.smartpark.customer.service.ActivityService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Slf4j
@RestController
@RequestMapping("/wx/activity")
@Api(tags = "活动管理-小程序")
public class WxActivityController {

    @Autowired
    private ActivityService activityService;


    @Autowired
    private ActivityRegisterRecordService activityRegisterRecordService;

    @ApiOperation(value = "园区活动-分页查询,parm参数请传入parkOrgId", tags = "活动")
    @PostMapping("/wxListActivityPage")
    public ResponseResult<PageInfo<ActivityDTO>> listActivityPage(@RequestBody BaseQuery<String> query) {
        PageInfo<ActivityDTO> page = activityService.wxListActivityPage(query);
        return ResponseResult.success(page);
    }

    @ApiOperation(value = "我的报名活动园区活动-分页查询,param参数请传入parkOrgId", tags = "活动")
    @PostMapping("/getMyListActivityPage")
    public ResponseResult<PageInfo<ActivityDTO>> wxMyListActivityPage(@RequestBody BaseQuery<String> query) {
        PageInfo<ActivityDTO> page = activityService.wxMyListActivityPage(query);
        return ResponseResult.success(page);
    }

    @ApiOperation(value = "我的报名详情,测试日志,", tags = "活动")
    @PostMapping("/getMyRegisterRecordDetail")
    public ResponseResult<MyRegisterRecord> getWxMyRegisterRecordDetail(@RequestParam("activityId") String activityId) {
        log.info(">>>>>>>>>>日志我的报名详情<<<<<<<<<<<<");
        MyRegisterRecord result = activityService.getWxMyRegisterRecordDetail(activityId);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "报名", tags = "报名记录")
    @PostMapping("/createRecord")
    public ResponseResult<Boolean> createRecord(@RequestBody @Valid ActivityRegisterRecordReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(activityRegisterRecordService.saveOrUpdateActivityRegisterRecord(reqDTO));
    }


    @ApiOperation(value = "取消报名-删除", tags = "报名记录")
    @DeleteMapping("/removeRecord/{recordId}")
    public ResponseResult<Boolean> remove(@PathVariable("recordId") @NotEmpty(message = "recordId不能为空") String id) {
        return ResponseResult.success(activityRegisterRecordService.removeById(id));
    }

}
