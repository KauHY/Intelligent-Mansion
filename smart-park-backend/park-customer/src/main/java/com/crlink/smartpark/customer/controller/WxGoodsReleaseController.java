package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.GoodsReleaseCheckReqDTO;
import com.crlink.smartpark.customer.controller.request.GoodsReleaseReqDTO;
import com.crlink.smartpark.customer.dto.GoodsReleaseDTO;
import com.crlink.smartpark.customer.service.GoodsReleaseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Slf4j
@Api(tags = "物品放行-小程序")
@RestController
@RequestMapping("/wx/goodsRelease")
public class WxGoodsReleaseController {

    @Autowired
    private GoodsReleaseService goodsReleaseService;

    @ApiOperation(value = "C端小程序-新增", tags = "物品放行-小程序")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid GoodsReleaseReqDTO reqDTO) {
        return ResponseResult.success(goodsReleaseService.saveOrUpdateGoodsRelease(reqDTO));
    }


    @ApiOperation(value = "C端小程序列表,param传入parkOrgId,status=0表示全部,1待审核，2已审核,", tags = "物品放行-小程序")
    @PostMapping("/cWxGoodsReleasePage/{status}")
    public ResponseResult<PageInfo<GoodsReleaseDTO>> cWxGoodsReleasePage(@PathVariable("status") Integer status, @RequestBody BaseQuery<String> query) {
        PageInfo<GoodsReleaseDTO> page = goodsReleaseService.cWxGoodsReleasePage(query, status);
        return ResponseResult.success(page);
    }


    @ApiOperation(value = "B端小程序列表: 0全部，1待审核，2已审核", tags = "物品放行-小程序")
    @PostMapping("/bWxGoodsReleasePage/{status}")
    public ResponseResult<PageInfo<GoodsReleaseDTO>> bWxGoodsReleasePage(@PathVariable("status") Integer status, @RequestBody BaseQuery query) {
        PageInfo<GoodsReleaseDTO> page = goodsReleaseService.bWxGoodsReleasePage(query, status);
        return ResponseResult.success(page);
    }


    @ApiOperation(value = "B端小程序审核", tags = "物品放行-小程序")
    @PostMapping("/check")
    public ResponseResult<Boolean> checkGoodsRelease(@RequestBody @Validated GoodsReleaseCheckReqDTO reqDTO) {
        Boolean result = goodsReleaseService.checkGoodsRelease(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "取消物品放行", tags = "物品放行-小程序")
    @PostMapping("/cancel/{goodsReleaseId}")
    public ResponseResult<Boolean> cancelGoodsRelease(@PathVariable("goodsReleaseId") String goodsReleaseId) {
        Boolean result = goodsReleaseService.cancelGoodsRelease(goodsReleaseId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "-根据ID查询", tags = "物品放行-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<GoodsReleaseDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(goodsReleaseService.getGoodsReleaseById(id));
    }
}
