package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.MeterConsumptionTradeDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionTrade;
import com.crlink.smartpark.contract.model.query.MeterConsumptionTradeQuery;
import com.crlink.smartpark.contract.model.request.ReturnMoneyReqDTO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionTradeListVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 租赁合同交易明细 服务类
 * </p>
 * */
public interface MeterConsumptionTradeService extends IService<MeterConsumptionTrade> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionTrade, ?> sValue);


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeterConsumptionTradeDTO getMeterConsumptionTradeById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterConsumptionTradeListVO> listMeterConsumptionTradePage(MeterConsumptionTradeQuery query);

    Boolean returnMoney(ReturnMoneyReqDTO dto);


    void export(MeterConsumptionTradeQuery query);
}
