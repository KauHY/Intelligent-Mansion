package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.device.dto.MeterWarningRecordDTO;
import com.crlink.smartpark.device.entity.MeterWarningRecord;
import com.crlink.smartpark.device.model.MeterWarningRecordReqDTO;
import com.crlink.smartpark.device.model.query.MeterWarningRecordQuery;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 仪表异常告警记录 服务类
 * </p>
 * */
public interface MeterWarningRecordService extends IService<MeterWarningRecord> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterWarningRecord, ?> sValue);

    Integer countMeterWarning(String parkOrgId, LocalDateTime startTime, LocalDateTime endTime, Integer status);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeterWarningRecord(MeterWarningRecordReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeterWarningRecordDTO getMeterWarningRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterWarningRecordDTO> listMeterWarningRecordPage(MeterWarningRecordQuery query);

    @Id2objHandle
    PageInfo<MeterWarningRecordDTO> list3dMeterWarningRecordPage(MeterWarningRecordQuery query);
}
