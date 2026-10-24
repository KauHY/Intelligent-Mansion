package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.system.dto.house.*;
import com.crlink.smartpark.system.entity.House;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.HouseListVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 房屋表 服务类
 * </p>
 * */
public interface HouseService extends IService<House> {

    Map<String, String> getBuildingHouseNamesByIds(Set<String> houseIds);

    Map<String, ?> id2ValueMap(Set<String> ids, SFunction<House, ?> sValue);

    Map<String, Map<String, String>> buildingId2HouseIdNameMap(Set<String> buildingIds);

    Map<String, HouseDTO> houseId2House(Set<String> houseIds, Set<String> columns);

    Map<String, HouseVO> houseId2HouseVO(Collection<String> houseIds);

    /**
     * 分页查询
     *
     * @param houseListDTO 查询对象
     * @return 分页结果
     */


    PageInfo<HouseListVO> listHousePage(HouseListDTO houseListDTO);

    /**
     * 新增或修改房屋
     *
     * @param houseSaveDTO
     * @return
     */
    void saveOrUpdateHouse(HouseSaveDTO houseSaveDTO);

    /**
     * 删除房屋
     *
     * @param id
     * @return
     */
    void removeHouseById(String id);

    /**
     * 房屋结构树根据楼栋ID查询房屋
     *
     * @param buildingId
     * @return
     */
    List<DictVO> listHouseDictByBuildingId(String buildingId, Integer floorNum);

    /**
     * 根据id查询房屋信息
     *
     * @param id
     * @return
     */
    HouseSaveDTO getHouseById(String id);

    /**
     * 动态查询房屋信息
     *
     * @param dto
     * @return
     */
    Map<String, Map<String, Object>> queryHouseColumns(HouseQueryDTO dto);

    Set<String> getDelHouseByParkOrgId(HouseListDTO houseListDTO);

    HouseListVO findHouseById(String houseId);

    Map<String, ?> name2IdMap(Set<String> names, @NotNull String buildingId, @NotNull String parkOrgId);

    /**
     * 导入房屋
     *
     * @param file
     * @param parkOrgId
     * @return
     */
    FileImportErrorInfoDTO importHouses(MultipartFile file, String parkOrgId);

    void exportHouses(HouseListDTO houseListDTO);


    Map<String, HouseDTO> listHouseFor3D(String parkOrgId, String buildingId, Integer floor);

//    BusinessHouseData getBusinessHouseData(String parkOrgId);

    HouseRentData getHouseRentData(String parkOrgId, Boolean fromScreen);
}
