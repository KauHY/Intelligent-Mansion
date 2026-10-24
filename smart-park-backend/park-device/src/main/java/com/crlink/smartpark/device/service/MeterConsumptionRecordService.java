package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.dto.MeterConsumptionCountDTO;
import com.crlink.smartpark.device.dto.MeterConsumptionRecordDTO;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.entity.MeterConsumptionRecord;
import com.crlink.smartpark.device.model.query.MeterConsumptionAnalysisReqDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionRecordQuery;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 水电表用量记录 服务类
 * </p>
 * */
public interface MeterConsumptionRecordService extends IService<MeterConsumptionRecord> {


    void exchangeMeterSaveConsumptionRecord(DeviceInfo meter, BigDecimal oldReading);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeterConsumptionRecordDTO getMeterConsumptionRecordById(String id);

    Map<String, MeterConsumptionRecord> listByMeterIds(Collection<String> meterIds, String settledMonth);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterConsumptionRecordDTO> listMeterConsumptionRecordPage(MeterConsumptionRecordQuery query);

    void consumptionRecordDownload(MeterConsumptionRecordQuery query);

    Collection<MeterConsumptionCountDTO> listMeterConsumptionAnalysis(MeterConsumptionAnalysisReqDTO query);

    void downloadAnalysis(MeterConsumptionAnalysisReqDTO query);
}
