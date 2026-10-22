package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.model.vo.ParkingOccupationRatioVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 停车场数据统计
 *
 */
@Slf4j
@Api(value = "停车场数据统计", tags = "停车场数据统计")
@RestController
@RequestMapping("/parking-statistics")
public class ParkingStatisticsController {

    @Resource
    private ParkingLotService parkingLotService;

    @ApiOperation(value = "获取停车场占用率", tags = "获取停车场占用率")
    @GetMapping("/{parkOrgId}/get")
    public ResponseResult<Object> get(@PathVariable String parkOrgId) {
        List<ParkingOccupationRatioVO> occupationRatio = parkingLotService.getOccupationRatio(parkOrgId);
        return ResponseResult.success(occupationRatio);
    }
}
