package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.dto.ApplyForHouseReqDTO;
import com.crlink.smartpark.business.vo.ApplyForHouseVO;
import com.crlink.smartpark.business.entity.ApplyForHouse;
import com.crlink.smartpark.business.service.ApplyForHouseService;
import com.crlink.smartpark.common.web.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * park-business
 * 看房申请表 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-看房申请-小程序")
@RequestMapping("/business/apply-for-house/wx")
public class WxApplyForHouseController {

    @Autowired
    private ApplyForHouseService applyForHouseService;

    /**
     * 【看房申请】根据ID查询
     *
     * @param id 主键
     * @return 看房申请表实体
     */
    @ApiOperation(value = "看房申请-根据ID查询", tags = "看房申请-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<ApplyForHouseVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        ApplyForHouseVO result = applyForHouseService.getApplyForHouseById(id);
        return ResponseResult.success(result);
    }

    /**
     * 【看房申请】新增
     *
     * @param applyForHouseReqDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "看房申请-新增", tags = "看房申请-小程序")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ApplyForHouseReqDTO applyForHouseReqDTO) {
        Boolean result = applyForHouseService.create(applyForHouseReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 【看房申请表】分页查询
     *
     * @param query 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "看房申请-分页查询", tags = "看房申请-小程序")
    @PostMapping("/listApplyForHouseWxPage")
    public ResponseResult<PageInfo<ApplyForHouse>> listApplyForHouseWxPage(@RequestBody BaseQuery<String> query) {
        PageInfo<ApplyForHouse> result = applyForHouseService.listApplyForHouseWxPage(query);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "看房申请-撤回", tags = "看房申请-小程序")
    @PostMapping("/returnApply")
    public ResponseResult<Boolean> returnApply(@RequestParam String id) {
        Boolean result = applyForHouseService.returnApply(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "看房申请-催办", tags = "看房申请-小程序")
    @PostMapping("/remind")
    public ResponseResult<Boolean> remind(@RequestParam String id) {
        //todo
        return ResponseResult.success();
    }

}

