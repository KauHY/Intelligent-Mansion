package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.dto.CarAccessRecordDTO;
import com.crlink.smartpark.visitor.entity.CarAccessRecord;
import com.crlink.smartpark.visitor.query.CarAccessRecordListQuery;
import com.crlink.smartpark.visitor.vo.CarAccessRecordListVO;
import com.crlink.smartpark.visitor.vo.CarAccessRecordVO;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车牌抓拍记录
 * */
public interface CarAccessRecordService extends IService<CarAccessRecord> {

    /**
     * 分页查询车牌抓拍记录
     */
    PageInfo<CarAccessRecordListVO> pageListRecord(CarAccessRecordListQuery query);

    /**
     * 导出车牌抓拍记录
     */
    void exportListRecord(CarAccessRecordListQuery query);

    List<CarAccessRecordVO> getCarAccessRecords(String parkLotId, LocalDateTime startTime);

    /**
     * 保存车辆通行（用于通行事件）
     * @param recordDTO
     * @return
     */
    Boolean saveEventRecord(CarAccessRecordDTO recordDTO);

}
