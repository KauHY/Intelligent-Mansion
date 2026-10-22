package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.entity.MeterConsumptionBillRecord;
import com.crlink.smartpark.contract.model.query.MeterConsumptionBillRecordQuery;
import com.crlink.smartpark.contract.model.MeterConsumptionBillRecordReqDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionBillRecordDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 租赁合同账单减免记录 服务类
 * </p>
 * */
public interface MeterConsumptionBillRecordService extends IService<MeterConsumptionBillRecord> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionBillRecord, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeterConsumptionBillRecord(MeterConsumptionBillRecordReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeterConsumptionBillRecordDTO getMeterConsumptionBillRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterConsumptionBillRecordDTO> listMeterConsumptionBillRecordPage(MeterConsumptionBillRecordQuery query);
}
