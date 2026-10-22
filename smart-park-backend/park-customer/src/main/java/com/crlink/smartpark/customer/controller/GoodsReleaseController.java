package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.GoodsReleaseCheckReqDTO;
import com.crlink.smartpark.customer.controller.request.query.GoodsReleaseQuery;
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

/**
 * park-customer
 * */
@Slf4j
@RestController
@Api(tags = "物品放行")
@RequestMapping("/goods-release")
public class GoodsReleaseController {

    @Autowired
    private GoodsReleaseService goodsReleaseService;


//    @ApiOperation(value = "-新增", tags = "")
//    @PostMapping("/create")
//    public ResponseResult<Boolean> create(@RequestBody @Valid GoodsReleaseReqDTO reqDTO) {
//        //reqDTO.setId(null);
//        return ResponseResult.success(goodsReleaseService.saveOrUpdateGoodsRelease(reqDTO));
//    }


//    @ApiOperation(value = "-删除", tags = "")
//    @DeleteMapping("/remove/{id}")
//    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(goodsReleaseService.removeById(id));
//    }


    @ApiOperation(value = "-根据ID查询", tags = "")
    @GetMapping("/findById/{id}")
    public ResponseResult<GoodsReleaseDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(goodsReleaseService.getGoodsReleaseById(id));
    }


    //@ApiOperation(value = "-修改", tags = "")
    //@PostMapping("/modify")
    //public ResponseResult<Boolean> modify(@RequestBody @Valid GoodsReleaseReqDTO reqDTO) {
    //    AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
    //    return ResponseResult.success(goodsReleaseService.saveOrUpdateGoodsRelease(reqDTO));
    //}


    @ApiOperation(value = "-分页查询", tags = "")
    @PostMapping("/listGoodsReleasePage")
    public ResponseResult<PageInfo<GoodsReleaseDTO>> listGoodsReleasePage(@RequestBody @Valid GoodsReleaseQuery query) {
        return ResponseResult.success(goodsReleaseService.listGoodsReleasePage(query));
    }

    @ApiOperation(value = "物品放行-导出", tags = "物品放行")
    @PostMapping("/exportGoodsRelease")
    public void exportGoodsRelease(@RequestBody GoodsReleaseQuery query) {
        goodsReleaseService.exportGoodsRelease(query);
    }


    @ApiOperation(value = "放行审核", tags = "")
    @PostMapping("/check")
    public ResponseResult<Boolean> checkGoodsRelease(@RequestBody @Validated GoodsReleaseCheckReqDTO reqDTO) {
        Boolean result = goodsReleaseService.checkGoodsRelease(reqDTO);
        return ResponseResult.success(result);
    }
}

