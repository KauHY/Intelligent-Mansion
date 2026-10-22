package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.entity.CarAccessRecordRel;
import com.crlink.smartpark.visitor.query.CarAccessFullRecordListQuery;
import com.crlink.smartpark.visitor.vo.CarAccessFullRecordListVO;
import com.github.pagehelper.PageInfo;

/**
 * 车辆通行记录
 * * @date 2022-10-12
 */
public interface CarAccessRecordRelService extends IService<CarAccessRecordRel> {

    /**
     * 查询分页列表
     *
     * @param query
     * @return
     */
    PageInfo<CarAccessFullRecordListVO> pageList(CarAccessFullRecordListQuery query);

    /**
     * 获取最近一次的入口记录
     *
     * @param carNum
     * @param parkingLotId
     * @return
     */
    CarAccessRecordRel getLatestEnterRecord(String carNum, String parkingLotId);

    /**
     * 导出车辆通行记录
     *
     * @param query
     */
    void exportList(CarAccessFullRecordListQuery query);

}
