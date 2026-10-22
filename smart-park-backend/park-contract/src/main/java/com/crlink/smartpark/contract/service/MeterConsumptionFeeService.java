package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.MeterConsumptionFeeDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionFee;
import com.crlink.smartpark.contract.model.MeterConsumptionFeeReqDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionFeeQuery;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionFeeVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 水电滞纳金费用 服务类
 * </p>
 * */
public interface MeterConsumptionFeeService extends IService<MeterConsumptionFee> {

    /**
     * 根据Id查询值
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionFee, T> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean modifyDate(MeterConsumptionFeeReqDTO reqDTO);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean transform(MeterConsumptionFeeReqDTO reqDTO);


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeterConsumptionFeeVO getMeterConsumptionFee(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterConsumptionFeeDTO> listMeterConsumptionFeePage(MeterConsumptionFeeQuery query);


    /**
     * 导出
     *
     * @param query 查询对象
     * @return 分页结果
     */
    void exportFee(MeterConsumptionFeeQuery query);

    void create(String month);

    void createLate();
}
