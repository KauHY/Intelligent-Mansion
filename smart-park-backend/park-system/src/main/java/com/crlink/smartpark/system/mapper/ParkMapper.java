package com.crlink.smartpark.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.entity.Park;
import com.crlink.smartpark.system.query.ParkListQuery;
import com.crlink.smartpark.system.vo.ParkDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 园区表 Mapper 接口
 * </p>
 * */
public interface ParkMapper extends BaseMapper<Park> {

    /**
     * @param query
     * @return
     */
    List<ParkDTO> listParkByOrgIds(ParkListQuery query);

    /**
     * 通过ids查找园区列表信息
     *
     * @param name
     * @return
     */
    List<ParkDTO> queryParksByName(@Param("parkOrgIds") Set<String> parkOrgIds, @Param("name") String name);
}
