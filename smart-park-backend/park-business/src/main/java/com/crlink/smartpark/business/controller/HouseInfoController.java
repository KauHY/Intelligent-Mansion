package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.business.dto.HouseInfoReqDTO;
import com.crlink.smartpark.business.query.HouseInfoQuery;
import com.crlink.smartpark.business.query.HouseQuery;
import com.crlink.smartpark.business.query.WxHouseQuery;
import com.crlink.smartpark.business.service.HouseInfoService;
import com.crlink.smartpark.business.vo.*;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * park-business
 * 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-招商房屋")
@RequestMapping("/business/house-info")
public class HouseInfoController {

    @Autowired
    private HouseInfoService houseInfoService;

    @ApiOperation(value = "新增招商房屋信息", tags = "")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody HouseInfoReqDTO dto) {
        dto.setId(null);
        Boolean result = houseInfoService.saveOrUpdateHouseInfo(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "修改招商房屋信息", tags = "")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody HouseInfoReqDTO dto) {
        AssertUtil.requireNotNull(dto.getId(), "10007");
        Boolean result = houseInfoService.saveOrUpdateHouseInfo(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "删除招商房屋", tags = "")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = houseInfoService.deleteHouseInfoById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "根据招商房源ID查询", tags = "")
    @GetMapping("/findById/{houseInfoId}")
    public ResponseResult<HouseInfoVO> findById(@PathVariable("houseInfoId") @NotNull(message = "id不能为空") String id) {
        HouseInfoVO houseInfoVO = houseInfoService.getHouseInfoById(id);
        return ResponseResult.success(houseInfoVO);
    }


    @ApiOperation(value = "招商房屋选择框，招商信息选择招商房屋", tags = "")
    @PostMapping("/selectListHouseInfoPage")
    public ResponseResult<PageInfo<HouseInfoDictVO>> selectListHouseInfoPage(@RequestBody HouseQuery houseQuery) {
        PageInfo<HouseInfoDictVO> result = houseInfoService.selectListHouseInfoPage(houseQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "商机意向房源分页查询", tags = "")
    @PostMapping("/selectIntentionHousePage")
    public ResponseResult<PageInfo<HouseBaseVO>> selectIntentionHousePage(@RequestBody HouseQuery houseQuery) {
        PageInfo<HouseBaseVO> result = houseInfoService.selectIntentionHousePage(houseQuery);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "招商房屋分页查询", tags = "")
    @PostMapping("/listHouseInfoPage")
    public ResponseResult<PageInfo<HouseInfoVO>> listHouseInfoPage(@RequestBody HouseInfoQuery houseInfoQuery) {
        PageInfo<HouseInfoVO> result = houseInfoService.listHouseInfoPage(houseInfoQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "查询园区下招商房屋的楼栋,对应的是buildingId")
    @GetMapping("/listBuildingDict/{parkOrgId}")
    public ResponseResult<List<DictVO>> listBuildingDict(@PathVariable("parkOrgId") @NotEmpty String parkOrgId) {
        List<DictVO> result = houseInfoService.listBuildingDict(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "查询某楼栋下的招商房屋,对应的是houseId")
    @GetMapping("/listHouseInfoDict/{buildingId}")
    public ResponseResult<List<DictVO>> listBusinessHouseInfoDict(@PathVariable("buildingId") String buildingId) {
        List<DictVO> result = houseInfoService.listBusinessHouseInfoDict(buildingId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "运营小程序：招商详情，楼栋信息", tags = "")
    @GetMapping("/wxBuildings")
    public ResponseResult<List<WxBuildingVO>> queryBuildings() {
        List<WxBuildingVO> result = houseInfoService.queryBuildings();
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "运营小程序：招商详情，楼栋房屋信息统计", tags = "")
    @GetMapping("/wxBuildingHouses/{buildingId}")
    public ResponseResult<WxHouseVO> queryHouses(@PathVariable("buildingId") @NotNull(message = "buildingId不能为空") String buildingId) {
        WxHouseVO result = houseInfoService.queryHouses(buildingId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "运营小程序：招商详情，楼栋房屋信息", tags = "")
    @PostMapping("/wxQueryHousesPage")
    public ResponseResult<PageInfo<BusinessHouseVO>> queryHousesPage(@RequestBody WxHouseQuery query) {
        PageInfo<BusinessHouseVO> result = houseInfoService.queryHousesPage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "运营小程序:根据招商房源ID查询房屋详情", tags = "")
    @GetMapping("/findByHouseInfoId/{houseInfoId}")
    public ResponseResult<HouseInfoVO> findByHouseInfoId(@PathVariable("houseInfoId") @NotNull(message = "id不能为空") String id) {
        HouseInfoVO houseInfoVO = houseInfoService.getHouseInfoById(id);
        return ResponseResult.success(houseInfoVO);
    }

}

