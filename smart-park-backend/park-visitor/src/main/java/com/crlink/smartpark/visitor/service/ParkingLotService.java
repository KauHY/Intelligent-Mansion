package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.ParkingLotSaveDTO;
import com.crlink.smartpark.visitor.dto.ParkingLotUpdateDTO;
import com.crlink.smartpark.visitor.entity.ParkingLot;
import com.crlink.smartpark.visitor.query.ParkingLotListQuery;
import com.crlink.smartpark.visitor.vo.ParkingLotListVO;
import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import com.crlink.smartpark.visitor.model.vo.ParkingOccupationRatioVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

/**
 * 停车场
 * */
public interface ParkingLotService extends IService<ParkingLot> {

    /**
     * 修改停车场
     */
    void update(ParkingLotUpdateDTO dto);

    /**
     * 删除停车场
     */
    void removeParkingLotById(String id);

    /**
     * 根据ID获取停车场
     */
    ParkingLotVO getParkingLotById(String id);

    ParkingLotVO getParkingLotByCode(String parkingLotCode);

    List<ParkingLotVO> getByIds(Set<String> ids);

    /**
     * 分页查询停车场
     */
    PageInfo<ParkingLotListVO> pageList(ParkingLotListQuery query);

    /**
     * @param parkOrgId
     * @return
     */
    List<DictVO> getAllByParkOrgId(String parkOrgId);

    List<DictVO> getParkLotDictList(String parkOrgId);

    /**
     * 获取停车场占用率（运营大屏）
     *
     * @param parkOrgId
     * @return
     */
    List<ParkingOccupationRatioVO> getOccupationRatio(String parkOrgId);

    /**
     * 同步车场车道信息
     */
    void sync();

    /**
     * 根据园区id删除
     *
     * @param parkOrgIds
     * @param ids
     * @return
     */
    int removeByParkOrgId(Set<String> parkOrgIds, Set<String> ids);

    /**
     * 获取允许访客停车的车场集合
     *
     * @return
     */
    List<ParkingLotVO> getAllowVisitorList();

}
