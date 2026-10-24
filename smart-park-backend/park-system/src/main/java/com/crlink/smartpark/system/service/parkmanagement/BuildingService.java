package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.system.dto.house.BuildingSaveDTO;
import com.crlink.smartpark.system.entity.Building;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.builiding.BuildingTreeVO;
import com.crlink.smartpark.system.vo.builiding.BuildingVO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 楼栋表 服务类
 * </p>
 * */
public interface BuildingService extends IService<Building> {

    Map<String, ?> id2ValueMap(Set<String> ids, SFunction<Building, ?> sValue);

    Map<String, ?> name2IdMap(Set<String> names, @NotNull String parkOrgId);

    /**
     * 新增或修改楼栋
     *
     * @param buildingSaveDTO 存储对象
     * @return 存储对象
     */
    void saveOrUpdateBuilding(BuildingSaveDTO buildingSaveDTO);

    FileImportErrorInfoDTO importBuildings(MultipartFile file, String parkOrgId);

    /**
     * 删除楼栋
     *
     * @param id 主键
     * @return
     */
    Boolean removeBuildingById(String id);

    /**
     * 房屋结构树根据园区ID查询楼栋
     *
     * @param parkOrgId 园区id
     * @return 房屋结构数据
     */

    List<DictVO> listBuildingDictByParkOrgId(String parkOrgId);

    /**
     * 根据id获取详情
     *
     * @param id 主键
     * @return 楼栋详情
     */
    BuildingVO getBuildingById(String id);

    /**
     * 房屋的管理的树
     *
     * @param parkOrgId
     * @return
     */
    BuildingTreeVO listBuildingTreeByParkOrgId(String parkOrgId);

    /**
     * 获取楼栋的总楼层
     *
     * @param buildingId
     * @return
     */
    Integer getBuildingTotalFloor(String buildingId);
}
