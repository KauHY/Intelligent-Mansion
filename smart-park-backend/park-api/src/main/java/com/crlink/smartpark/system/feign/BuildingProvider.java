package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.house.BuildingSaveDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.builiding.BuildingVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 楼栋表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/parkManagement/building", contextId = "BuildingProvider")
public interface BuildingProvider {

    /**
     * 【楼栋表】保存
     *
     * @param building 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/save")
    ResponseResult<BuildingSaveDTO> create(@RequestBody BuildingSaveDTO building);

    /**
     * 【楼栋表】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @DeleteMapping("/remove/{id}")
    ResponseResult<Boolean> remove(@PathVariable("id") String id);

    /**
     * 【楼栋表】根据ID查询
     *
     * @param id 主键
     * @return 楼栋表实体
     */
    @GetMapping("/findById/{id}")
    ResponseResult<BuildingVO> findById(@PathVariable("id") String id);

    /**
     * @param parkOrgId 主键
     * @return 楼栋表实体
     */
    @ApiOperation(value = "查询某园区下的所有楼栋名称", tags = {"房屋管理", "绑定房屋"})
    @GetMapping("/listBuildingDict/{parkOrgId}")
    ResponseResult<List<DictVO>> listBuildingDictByParkOrgId(@PathVariable("parkOrgId") String parkOrgId);

    /**
     * 楼栋-获取总楼层
     *
     * @param buildingId
     * @return
     */
    @GetMapping("/getBuildingTotalFloor")
    ResponseResult<Integer> getBuildingTotalFloor(@RequestParam("buildingId") String buildingId);

}
