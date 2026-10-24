package com.crlink.smartpark.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.dto.house.HouseListDTO;
import com.crlink.smartpark.system.entity.House;
import com.crlink.smartpark.system.vo.HouseListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 房屋表 Mapper 接口
 * </p>
 * */
public interface HouseMapper extends BaseMapper<House> {

    /**
     * 列表查询
     *
     * @param houseListDTO
     * @return
     */
    List<HouseListVO> listPage(@Param("houseListDTO") HouseListDTO houseListDTO);

    /**
     * 获取某个园区所有被删的房屋id
     * @param houseListDTO
     * @return
     */
    List<HouseListVO> getDelHouseByParkOrgId(@Param("houseListDTO") HouseListDTO houseListDTO);
}
