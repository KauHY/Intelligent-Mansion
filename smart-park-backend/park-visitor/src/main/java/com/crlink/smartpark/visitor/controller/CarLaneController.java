package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.CarLaneUpdateDTO;
import com.crlink.smartpark.visitor.query.CarLaneListQuery;
import com.crlink.smartpark.visitor.service.CarLaneService;
import com.crlink.smartpark.visitor.vo.CarLaneListVO;
import com.crlink.smartpark.visitor.vo.CarLaneVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * 车道控制器
 * */
@RestController
@RequestMapping("/carLane" )
@Api(tags = "访客模块-车道管理")
public class CarLaneController {

	@Resource
    private CarLaneService carLaneService;

    /**
     * 修改车道
     *
     */
    @ApiOperation(value = "修改车道", tags = "车道")
    @PutMapping
    public ResponseResult updateById(@RequestBody @Valid CarLaneUpdateDTO dto) {
    	carLaneService.update(dto);
        return ResponseResult.success();
    }

    /**
     * 通过id删除车道
     *
     */
    @ApiOperation(value = "通过id删除车道", tags = "车道")
    @DeleteMapping("/{id}" )
    public ResponseResult removeById(@PathVariable("id") String id) {
        carLaneService.removeCarLaneById(id);
        return ResponseResult.success();
    }

    /**
     * 通过id查询车道
     *
     */
    @ApiOperation(value = "通过id查询车道", tags = "车道")
    @GetMapping("/{id}" )
    public ResponseResult<CarLaneVO> getById(@PathVariable("id") String id) {
        return ResponseResult.success(carLaneService.getCarLaneById(id));
    }

    /**
     * 分页查询车道
     *
     */
    @ApiOperation(value = "分页查询车道", tags = "车道")
    @PostMapping("/page" )
    public ResponseResult<PageInfo<CarLaneListVO>> pageList(@RequestBody @Valid CarLaneListQuery query) {
        return ResponseResult.success(carLaneService.pageList(query));
    }

    /**
     * 车道下拉列表
     *
     */
    @ApiOperation(value = "车道下拉列表", tags = "车道")
    @GetMapping("/selectCarLane" )
    public ResponseResult<List<DictVO>> selectCarLane(@RequestParam(value = "parkingLotId") String parkingLotId) {
        return ResponseResult.success(carLaneService.selectCarLane(parkingLotId));
    }

    @ApiOperation(value = "通过ip查询车道", tags = "车道")
    @GetMapping("/{ip}/getByIp" )
    public ResponseResult<CarLaneVO> getByIp(@PathVariable("ip") String ip) {
        return ResponseResult.success(carLaneService.getCarLaneByIp(ip));
    }

}
