package com.crlink.smartpark.system.controller.mobile;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.service.parkmanagement.ParkService;
import com.crlink.smartpark.system.vo.MobileParkInfoVO;
import com.crlink.smartpark.system.vo.MobileParkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理
 *
 */
@Slf4j
@Api(tags = "小程序-园区")
@RestController
@RequestMapping("/mobile/park")
public class WxParkController {

    @Autowired
    private ParkService parkService;

    @GetMapping("/listParks")
    @ApiOperation(value = "首页园区列表", tags = "")
    public ResponseResult<MobileParkVO> list(@RequestParam(required = false) Double longitude,
        @RequestParam(required = false) Double latitude, @RequestParam(required = false) String parkOrgName,
        @RequestParam(required = false) String appId) {
        MobileParkVO mobileParkVO = parkService.listParksForWx(longitude, latitude, parkOrgName, appId);
        return ResponseResult.success(mobileParkVO);
    }

    @GetMapping("/getParks/{parkOrgId}")
    @ApiOperation(value = "园区介绍页面", tags = "")
    public ResponseResult<MobileParkInfoVO> getParkInfo(@PathVariable("parkOrgId") String parkOrgId) {
        MobileParkInfoVO mobileParkInfoVO = parkService.getParkInfo(parkOrgId);
        return ResponseResult.success(mobileParkInfoVO);
    }
}
