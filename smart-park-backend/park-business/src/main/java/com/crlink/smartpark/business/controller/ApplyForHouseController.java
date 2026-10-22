package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.business.query.ApplyForHouseQuery;
import com.crlink.smartpark.business.service.ApplyForHouseService;
import com.crlink.smartpark.business.vo.ApplyForHouseVO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * park-business
 * 看房申请表 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-看房申请")
@RequestMapping("/business/apply-for-house")
public class ApplyForHouseController {

    /**
     * 看房申请表
     */
    @Autowired
    private ApplyForHouseService applyForHouseService;

    /**
     * 【看房申请表】根据ID查询
     *
     * @param id 主键
     * @return 看房申请表实体
     */
    @ApiOperation(value = "看房申请-根据ID查询", tags = "看房申请")
    @GetMapping("/findById/{id}")
    public ResponseResult<ApplyForHouseVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        ApplyForHouseVO result = applyForHouseService.getApplyForHouseById(id);
        return ResponseResult.success(result);
    }


    /**
     * 【看房申请表】分页查询
     *
     * @param applyForHouseQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "看房申请-分页查询", tags = "看房申请")
    @PostMapping("/listApplyForHousePage")
    public ResponseResult<PageInfo<ApplyForHouseVO>> listApplyForHousePage(@RequestBody ApplyForHouseQuery applyForHouseQuery) {
        PageInfo<ApplyForHouseVO> result = applyForHouseService.listApplyForHousePage(applyForHouseQuery);
        return ResponseResult.success(result);
    }


    /**
     * @param id
     * @return
     */
    @ApiOperation(value = "看房申请-受理", tags = "看房申请")
    @PostMapping("/accept")
    public ResponseResult<Boolean> accept(@RequestParam @NotNull String id) {
        Boolean result = applyForHouseService.accept(id);
        return ResponseResult.success(result);
    }


}

