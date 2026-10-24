package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.park.ParkSaveDTO;
import com.crlink.smartpark.system.query.ParkListQuery;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementParkService;
import com.crlink.smartpark.system.service.parkmanagement.ParkService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.ParkDTO;
import com.crlink.smartpark.system.vo.ParkVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 园区运营平台-园区管理
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-园区管理")
@RequestMapping("/parkManagement/park")
public class ParkManagementParkController {


    @Autowired
    private ParkService parkService;

    @Autowired
    private ParkManagementParkService parkManagementParkService;

    /**
     * 园区信息完善
     */
    @ApiOperation(value = "园区信息完善", tags = "园区管理")
    @PostMapping("/completeParkInfo")
    public ResponseResult create(@RequestBody @Valid ParkSaveDTO park) {
        Boolean result = parkManagementParkService.completeParkInfo(park);
        return ResponseResult.success(result);
    }

    /**
     * 根据id查询园区信息
     */
    @ApiOperation(value = "根据id查询园区信息", tags = "园区管理")
    @GetMapping("/getParkInfoByParkOrgId/{parkOrgId}")
    public ResponseResult<ParkVO> getParkInfoByParkId(@PathVariable("parkOrgId") String parkOrgId) {
        ParkVO result = parkManagementParkService.getParkInfoByOrgId(parkOrgId);
        return ResponseResult.success(result);
    }

    /**
     * 组织下的园区-分页查询
     */
    @ApiOperation(value = "组织下的园区-分页查询", tags = "园区管理")
    @PostMapping("/listPageByOrgId")
    public ResponseResult<PageInfo<ParkDTO>> listPageByOrgId(@RequestBody @Valid ParkListQuery query) {
        PageInfo<ParkDTO> result = parkManagementParkService.listParkPageByOrgId(query);
        return ResponseResult.success(result);
    }

    /**
     * 查看可见园区名
     */
    @ApiOperation(value = "所属园区列表", tags = "园区管理")
    @PostMapping("/listParkDict")
    public ResponseResult<List<DictVO>> listParkDict() {
        List<DictVO> result = parkService.listParkName();
        return ResponseResult.success(result);
    }

}

