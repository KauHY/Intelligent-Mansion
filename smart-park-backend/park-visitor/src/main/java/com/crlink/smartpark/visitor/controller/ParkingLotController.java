package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.ParkingLotUpdateDTO;
import com.crlink.smartpark.visitor.query.ParkingLotListQuery;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.vo.ParkingLotListVO;
import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * 停车场控制器
 * */
@RestController
@RequestMapping("/parkingLot")
@Api(tags = "访客模块-停车场管理")
public class ParkingLotController {

    @Resource
    private ParkingLotService parkingLotService;

    /**
     * 修改停车场
     */
    @ApiOperation(value = "修改停车场", tags = "停车场")
    @PutMapping
    public ResponseResult updateById(@RequestBody @Valid ParkingLotUpdateDTO dto) {
        parkingLotService.update(dto);
        return ResponseResult.success();
    }

    /**
     * 通过id删除停车场
     */
    @ApiOperation(value = "通过id删除停车场", tags = "停车场")
    @DeleteMapping("/{id}")
    public ResponseResult removeById(@PathVariable("id") String id) {
        parkingLotService.removeParkingLotById(id);
        return ResponseResult.success();
    }

    /**
     * 通过id查询停车场
     */
    @ApiOperation(value = "通过id查询停车场", tags = "停车场")
    @GetMapping("/{id}")
    public ResponseResult<ParkingLotVO> getById(@PathVariable("id") String id) {
        return ResponseResult.success(parkingLotService.getParkingLotById(id));
    }

    /**
     * 分页查询停车场
     */
    @ApiOperation(value = "分页查询停车场", tags = "停车场")
    @PostMapping("/page")
    public ResponseResult<PageInfo<ParkingLotListVO>> pageList(@RequestBody @Valid ParkingLotListQuery query) {
        return ResponseResult.success(parkingLotService.pageList(query));
    }

    /**
     * 通过园区ID查询所有停车场
     */
    @ApiOperation(value = "通过园区ID查询所有停车场", tags = "停车场")
    @GetMapping("/getAllByParkOrgId/{parkOrgId}")
    public ResponseResult<List<DictVO>> getAllByParkOrgId(@PathVariable("parkOrgId") String parkOrgId) {
        return ResponseResult.success(parkingLotService.getAllByParkOrgId(parkOrgId));
    }

    @ApiOperation(value = "同步车场车道信息", tags = "停车场")
    @GetMapping("/sync")
    public ResponseResult sync() {
        parkingLotService.sync();
        return ResponseResult.success();
    }

}
