package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.house.BuildingSaveDTO;
import com.crlink.smartpark.system.service.parkmanagement.BuildingService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.builiding.BuildingTreeVO;
import com.crlink.smartpark.system.vo.builiding.BuildingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 园区运营平台-房屋管理
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-房屋管理")
@RequestMapping("/parkManagement/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    /**
     * 新增楼栋
     */
    @ApiOperation(value = "新增楼栋", tags = "房屋管理")
    @PostMapping("/save")
    public ResponseResult<BuildingSaveDTO> create(@RequestBody @Valid BuildingSaveDTO buildingSaveDTO) {
        buildingService.saveOrUpdateBuilding(buildingSaveDTO);
        return ResponseResult.success(buildingSaveDTO);
    }

    @ApiOperation(value = "楼栋导入", tags = "楼栋导入")
    @PostMapping("/importBuildings")
    public ResponseResult<FileImportErrorInfoDTO> importBuildings(@RequestPart("file") MultipartFile file, @RequestParam String parkOrgId) {
        FileImportErrorInfoDTO errorInfoDTO = buildingService.importBuildings(file, parkOrgId);
        return ResponseResult.success(errorInfoDTO);
    }

    /**
     * 删除楼栋
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "删除楼栋", tags = "房屋管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult remove(@PathVariable("id") String id) {
        buildingService.removeBuildingById(id);
        return ResponseResult.success();
    }


    @ApiOperation(value = "楼栋-获取总楼层", tags = "房屋管理")
    @GetMapping("/getBuildingTotalFloor")
    public ResponseResult<Integer> getBuildingTotalFloor(@RequestParam("buildingId") String buildingId) {
        return ResponseResult.success(buildingService.getBuildingTotalFloor(buildingId));
    }

    /**
     * 楼栋-根据ID查询
     *
     * @param id 主键
     * @return 楼栋表实体
     */
    @ApiOperation(value = "楼栋-根据ID查询楼栋", tags = "房屋管理")
    @GetMapping("/findBuildingById/{id}")
    public ResponseResult<BuildingVO> findBuildingById(@PathVariable("id") String id) {
        return ResponseResult.success(buildingService.getBuildingById(id));
    }

    /**
     * 查询某园区下的所有楼栋名称
     *
     * @param parkOrgId 所属园区Id
     */
    @ApiOperation(value = "查询某园区下的所有楼栋名称", tags = {"房屋管理", "绑定房屋"})
    @GetMapping("listBuildingDict/{parkOrgId}")
    public ResponseResult<List<DictVO>> listBuildingDictByParkOrgId(@PathVariable("parkOrgId") String parkOrgId) {
        return ResponseResult.success(buildingService.listBuildingDictByParkOrgId(parkOrgId));
    }

    @ApiOperation(value = "房屋管理的树", tags = {"房屋管理", "绑定房屋"})
    @GetMapping("/listBuildingTree/{parkOrgId}")
    public ResponseResult<BuildingTreeVO> listBuildingTreeByParkOrgId(@PathVariable("parkOrgId") String parkOrgId) {
        BuildingTreeVO result = buildingService.listBuildingTreeByParkOrgId(parkOrgId);
        return ResponseResult.success(result);
    }


}

