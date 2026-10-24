package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.dto.RentLateFeeDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionLateFee;
import com.crlink.smartpark.contract.model.query.MeterConsumptionLateFeeQuery;
import com.crlink.smartpark.contract.model.MeterConsumptionLateFeeReqDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionLateFeeDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.model.query.RentLateFeeQuery;
import com.crlink.smartpark.contract.model.request.RentLateFeeReqDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 水电滞纳金费用 服务类
 * </p>
 * */
public interface MeterConsumptionLateFeeService extends IService<MeterConsumptionLateFee> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionLateFee, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateRentLateFee(MeterConsumptionLateFeeReqDTO reqDTO);

    /**
     * 导出
     *
     * @return 实体对象
     */
    void export(MeterConsumptionLateFeeQuery reqDTO);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterConsumptionLateFeeDTO> listMeterConsumptionLateFeePage(MeterConsumptionLateFeeQuery query);
}
