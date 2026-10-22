package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.AdvertisementReqDTO;
import com.crlink.smartpark.customer.controller.request.query.AdvertisementQuery;
import com.crlink.smartpark.customer.dto.AdvertisementDTO;
import com.crlink.smartpark.customer.service.AdvertisementService;
import com.crlink.smartpark.customer.vo.WxAdvertisementVO;
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
 * 广告
 * */
@Slf4j
@RestController
@Api(tags = "客户模块-广告管理")
@RequestMapping("/advertisement")
@Validated
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;


    @ApiOperation(value = "广告-新增", tags = "广告管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid AdvertisementReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = advertisementService.saveOrUpdateAdvertisement(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "广告-删除", tags = "广告管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        Boolean result = advertisementService.removeAdvertisementById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "广告-根据ID查询", tags = "广告管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<AdvertisementDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        AdvertisementDTO result = advertisementService.getAdvertisementById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "广告-修改", tags = "广告管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid AdvertisementReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        Boolean result = advertisementService.saveOrUpdateAdvertisement(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "广告-分页查询", tags = "广告管理")
    @PostMapping("/listAdvertisementPage")
    public ResponseResult<PageInfo<AdvertisementDTO>> listAdvertisementPage(@RequestBody @Valid AdvertisementQuery query) {
        PageInfo<AdvertisementDTO> result = advertisementService.listAdvertisementPage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "广告-C端小城程序首页", tags = "广告管理")
    @GetMapping("/wx/wxAdvertisementList/{parkOrgId}")
    public ResponseResult<List<WxAdvertisementVO>> wxAdvertisementList(@PathVariable("parkOrgId") @NotEmpty(message = "parkOrgId不能为空") String parkOrgId) {
        List<WxAdvertisementVO> result = advertisementService.wxAdvertisementList(parkOrgId);
        return ResponseResult.success(result);
    }
}

