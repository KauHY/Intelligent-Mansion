package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.ActivityReqDTO;
import com.crlink.smartpark.customer.controller.request.query.ActivityQuery;
import com.crlink.smartpark.customer.controller.response.ActivityRespDTO;
import com.crlink.smartpark.customer.controller.response.ActivityToppingReqDTO;
import com.crlink.smartpark.customer.dto.ActivityDTO;
import com.crlink.smartpark.customer.service.ActivityService;
import com.crlink.smartpark.customer.vo.ActivityList;
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
 * park-customer
 * 活动
 * */
@Slf4j
@RestController
@RequestMapping("/activity")
@Api(tags = "活动管理-活动")
public class ActivityController {

    @Autowired
    private ActivityService activityService;


    @ApiOperation(value = "活动-新增", tags = "活动")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ActivityReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(activityService.saveOrUpdateActivity(reqDTO));
    }


    @ApiOperation(value = "活动-删除", tags = "活动")
    @DeleteMapping("/removeActivity/{id}")
    public ResponseResult<Boolean> removeActivity(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(activityService.removeById(id));
    }


    @ApiOperation(value = "活动-根据ID查询", tags = "活动")
    @GetMapping("/findById/{id}")
    public ResponseResult<ActivityDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(activityService.getActivityById(id));
    }


    @ApiOperation(value = "活动-修改", tags = "活动")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ActivityReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(activityService.saveOrUpdateActivity(reqDTO));
    }


    @ApiOperation(value = "活动-分页查询", tags = "活动")
    @PostMapping("/listActivityPage")
    public ResponseResult<PageInfo<ActivityRespDTO>> listActivityPage(@RequestBody @Valid ActivityQuery query) {
        return ResponseResult.success(activityService.listActivityPage(query));
    }

    @ApiOperation(value = "活动置顶/取消置顶", tags = "活动")
    @PostMapping("/toppingActivity")
    public ResponseResult<Boolean> toppingActivity(@RequestBody @Validated ActivityToppingReqDTO reqDTO) {
        Boolean result = activityService.toppingActivity(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "园区活动", tags = "运营大屏")
    @GetMapping("/getActivityList")
    public ResponseResult<List<ActivityList>> getActivityList(@RequestParam("parkOrgId") String parkOrgId) {
        AssertUtil.requireNotNull(parkOrgId, "10016");
        List<ActivityList> result = activityService.getActivityList(parkOrgId);
        return ResponseResult.success(result);
    }


}

