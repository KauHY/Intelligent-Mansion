package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.system.dto.house.HouseDTO;
import com.crlink.smartpark.system.dto.house.HouseListDTO;
import com.crlink.smartpark.system.dto.house.HouseQueryDTO;
import com.crlink.smartpark.system.dto.house.HouseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 房屋表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/parkManagement/house", contextId = "HouseProvider")
public interface HouseProvider {

    /**
     * 【房屋表】根据ID查询
     *
     * @param id 主键
     * @return 房屋表实体
     */
    //@GetMapping("/findById/{id}")
    //ResponseResult<HouseSaveDTO> findById(@PathVariable("id") String id);

    /**
     * 根据Id获取房屋
     *
     * @param houseIds
     * @param columns
     * @return
     */
    @GetMapping("/getHouseByHouseId")
    Map<String, HouseDTO> getHouseByHouseId(@RequestParam("houseIds") Set<String> houseIds, @RequestParam("columns") Set<String> columns);

    /**
     * 获取房屋详细信息
     *
     * @param houseIds
     * @return
     */
    @GetMapping("/getHouseDetailByHouseId")
    Map<String, HouseVO> getHouseDetailByHouseId(@RequestParam("houseIds") Collection<String> houseIds);

    /**
     * 获取某个园区所有被删的房屋id
     *
     * @param houseListDTO
     * @return
     */
    @PostMapping("/getDelHouseByParkOrgId")
    Set<String> getDelHouseByParkOrgId(@RequestBody HouseListDTO houseListDTO);


    @PostMapping("/queryHouseColumns")
    Map<String, Map<String, Object>> queryHouseColumns(@RequestBody HouseQueryDTO dto);

    @GetMapping(value = "/getHouseIdsByNames")
    Map<String, String> getHouseIdsByNames(@RequestParam(value = "names", required = false) Set<String> names,
                                           @RequestParam(value = "buildingId") String buildingId,
                                           @RequestParam(value = "parkOrgId") String parkOrgId);

}
