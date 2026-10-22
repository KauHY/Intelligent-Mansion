package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.entity.CarLane;
import com.crlink.smartpark.visitor.dto.CarLaneSaveDTO;
import com.crlink.smartpark.visitor.dto.CarLaneUpdateDTO;
import com.crlink.smartpark.visitor.query.CarLaneListQuery;
import com.crlink.smartpark.visitor.vo.CarLaneListVO;
import com.crlink.smartpark.visitor.vo.CarLaneVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 车道
 * */
public interface CarLaneService extends IService<CarLane> {

    /**
    * 修改车道
    *
    */
    void update(CarLaneUpdateDTO dto);

    /**
    * 删除车道
    *
    */
    void removeCarLaneById(String id);

    /**
    * 根据ID获取车道
    *
    */
    CarLaneVO getCarLaneById(String id);

    CarLaneVO getCarLaneByIp(String ip);

    /**
    * 分页查询车道
    *
    */
    PageInfo<CarLaneListVO> pageList(CarLaneListQuery query);

    List<DictVO> selectCarLane(String parkingLotId);

    int removeByParkingLotId(String parkingLotId);

}

