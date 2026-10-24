package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.house.*;
import com.crlink.smartpark.system.service.parkmanagement.HouseService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.HouseListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 园区运营平台-房屋管理
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-房屋管理")
@RequestMapping("/parkManagement/house")
public class HouseController {


    @Autowired
    private HouseService houseService;

    /**
     * 新增房屋
     *
     * @param house 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "新增房屋", tags = "房屋管理")
    @PostMapping("/save")
    public ResponseResult<HouseSaveDTO> create(@RequestBody @Valid HouseSaveDTO house) {
        houseService.saveOrUpdateHouse(house);
        return ResponseResult.success();
    }


    /**
     * 房屋-删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "房屋-删除", tags = "房屋管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult remove(@PathVariable("id") String id) {
        houseService.removeHouseById(id);
        return ResponseResult.success();
    }


    /**
     * 房屋分页查询
     *
     * @param houseListDTO 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "房屋分页查询", tags = "房屋管理")
    @PostMapping("/listHousePage")
    public ResponseResult<PageInfo<HouseListVO>> listHousePage(@RequestBody HouseListDTO houseListDTO) {
        PageInfo result = houseService.listHousePage(houseListDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "房屋-根据ID查询", tags = "房屋管理")
    @GetMapping("/findHouseById/{houseId}")
    public ResponseResult<HouseListVO> findById(@PathVariable("houseId") @NotEmpty(message = "id不能为空") String houseId) {
        return ResponseResult.success(houseService.findHouseById(houseId));
    }

    @ApiOperation(value = "运营大屏楼栋房屋平面图", tags = "运营大屏")
    @GetMapping("/listHouseFor3D")
    public ResponseResult<Map<String, HouseDTO>> listHouseFor3D(String parkOrgId, String buildingId,
                                                                @RequestParam(value = "floor", required = false) Integer floor) {
        Map<String, HouseDTO> result = houseService.listHouseFor3D(parkOrgId, buildingId, floor);
        return ResponseResult.success(result);
    }

    /**
     * 查询某楼栋下的所有房屋名称
     *
     * @param buildingId 主键
     */
    @ApiOperation(value = "查询某楼栋下的所有房屋名称", tags = {"房屋管理", "绑定房屋"})
    @GetMapping("/listHouseDict/{buildingId}")
    public ResponseResult<List<DictVO>> listNodeByBuildingId(@PathVariable("buildingId") String buildingId,
                                                             @RequestParam(required = false) Integer floorNum) {
        return ResponseResult.success(houseService.listHouseDictByBuildingId(buildingId, floorNum));
    }

    /**
     * 查询某楼栋下的所有房屋名称
     *
     * @param buildingId 主键
     */
    @ApiOperation(value = "查询某楼栋某楼层下的所有房屋名称", tags = {"房屋管理", "绑定房屋"})
    @GetMapping("/listHouseDict/{buildingId}/{floorNum}")
    public ResponseResult<List<DictVO>> listNodeByBuildingIdAndFloor(@PathVariable("buildingId") String buildingId,
                                                                     @PathVariable("floorNum") Integer floorNum) {
        return ResponseResult.success(houseService.listHouseDictByBuildingId(buildingId, floorNum));
    }

    @ApiOperation(value = "房屋导入", tags = "房屋导入")
    @PostMapping("/importHouses")
    public ResponseResult<FileImportErrorInfoDTO> importHouses(@RequestPart("file") MultipartFile file, @RequestParam String parkOrgId) {
        FileImportErrorInfoDTO errorInfoDTO = houseService.importHouses(file, parkOrgId);
        return ResponseResult.success(errorInfoDTO);
    }

    @ApiOperation(value = "房屋导出", tags = "房屋导出")
    @PostMapping("/exportHouses")
    public void exportHouses(@RequestBody HouseListDTO houseListDTO) {
        houseService.exportHouses(houseListDTO);
    }


    @ApiOperation(value = "获取房屋信息", tags = "内部接口")
    @GetMapping("/getHouseByHouseId")
    public Map<String, HouseDTO> getHouseByHouseId(@RequestParam Set<String> houseIds, @RequestParam Set<String> columns) {
        Map<String, HouseDTO> houseId2HouseMap = houseService.houseId2House(houseIds, columns);
        return houseId2HouseMap;
    }

    @ApiOperation(value = "获取房屋详细信息", tags = "内部接口")
    @GetMapping("/getHouseDetailByHouseId")
    public Map<String, HouseVO> getHouseDetailByHouseId(@RequestParam Collection<String> houseIds) {
        Map<String, HouseVO> houseId2HouseMap = houseService.houseId2HouseVO(houseIds);
        return houseId2HouseMap;
    }

    @ApiOperation(value = "获取某个园区所有被删的房屋id", tags = "内部接口")
    @PostMapping("/getDelHouseByParkOrgId")
    public Set<String> getDelHouseByParkOrgId(@RequestBody HouseListDTO houseListDTO) {
        Set<String> result = houseService.getDelHouseByParkOrgId(houseListDTO);
        return result;
    }


    @ApiOperation(value = "条件查询房屋", tags = "内部接口")
    @PostMapping("/queryHouseColumns")
    public Map<String, Map<String, Object>> queryHouseColumns(@RequestBody HouseQueryDTO dto) {
        Map<String, Map<String, Object>> map = houseService.queryHouseColumns(dto);

        return map;
    }

    @ApiOperation(value = "根据房屋名获取houseId", tags = "内部接口")
    @GetMapping(value = "/getHouseIdsByNames")
    public Map<String, String> getHouseIdsByNames(@RequestParam(value = "names", required = false) Set<String> names,
                                                  @RequestParam(value = "buildingId") String buildingId,
                                                  @RequestParam(value = "parkOrgId") String parkOrgId) {
        return (Map<String, String>) houseService.name2IdMap(names, buildingId, parkOrgId);
    }


//    @ApiOperation(value = "智慧招商-房间总数、总面积、总商机", tags = "运营大屏")
//    @GetMapping("/getBusinessHouseData")
//    public ResponseResult<BusinessHouseData> getBusinessHouseData(@RequestParam("parkOrgId") String parkOrgId) {
//        AssertUtil.requireNotNull(parkOrgId, "10016");
//        BusinessHouseData result = houseService.getBusinessHouseData(parkOrgId);
//        return ResponseResult.success(result);
//    }

    @ApiOperation(value = "智慧招商", tags = "运营大屏")
    @GetMapping("/getHouseRentData")
    public ResponseResult<HouseRentData> getHouseRentData(@RequestParam("parkOrgId") String parkOrgId,
                                                          @RequestParam("fromScreen") Boolean fromScreen) {
        AssertUtil.requireNotNull(parkOrgId, "10016");
        HouseRentData result = houseService.getHouseRentData(parkOrgId, fromScreen);
        return ResponseResult.success(result);
    }
}

