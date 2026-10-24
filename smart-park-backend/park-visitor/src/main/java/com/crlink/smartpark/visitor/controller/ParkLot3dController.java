package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.service.CarAccessRecordService;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.vo.CarAccessRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/3d/device")
@Api(tags = "3D大屏-停车场相关接口")
public class ParkLot3dController {

    @Resource
    private ParkingLotService parkingLotService;


    @Resource
    private CarAccessRecordService carAccessRecordService;


    @ApiOperation(value = "15获取停车场列表")
    @GetMapping("/getParkLotDictList")
    public ResponseResult<List<DictVO>> getParkLotDictList(@RequestParam("parkOrgId") String parkOrgId) {
        List<DictVO> result = parkingLotService.getParkLotDictList(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "16获取停车场列表")
    @GetMapping("/getCarAccessRecords")
    public ResponseResult<List<CarAccessRecordVO>> getCarAccessRecords(@RequestParam("parkLotId") String parkLotId,
                                                                       @RequestParam("startTime") LocalDateTime startTime) {
        List<CarAccessRecordVO> result = carAccessRecordService.getCarAccessRecords(parkLotId, startTime);
        return ResponseResult.success(result);
    }
}
