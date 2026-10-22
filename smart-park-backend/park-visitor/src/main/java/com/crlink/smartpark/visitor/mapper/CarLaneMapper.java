package com.crlink.smartpark.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.visitor.entity.CarLane;

/**
 * 车道
 * */
public interface CarLaneMapper extends BaseMapper<CarLane> {

    /**
     * 根据场区id删除
     * @param parkingLotId
     * @return
     */
    int deleteByParkingLotId(String parkingLotId);

}
