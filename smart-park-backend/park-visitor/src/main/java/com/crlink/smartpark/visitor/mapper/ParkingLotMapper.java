package com.crlink.smartpark.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.visitor.entity.ParkingLot;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 停车场
 * */
public interface ParkingLotMapper extends BaseMapper<ParkingLot> {

    /**
     * 根据园区id删除
     * @param parkOrgIds
     * @return
     */
    int deleteByParkOrgId(@Param("parkOrgIds") Set<String> parkOrgIds, @Param("ids") Set<String> ids);

}
