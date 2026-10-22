package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.ChargeDetail;
import com.crlink.smartpark.contract.dto.ComputeAmountDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionLeaveDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionLeave;
import com.crlink.smartpark.contract.model.query.RentContractLeaveQuery;
import com.crlink.smartpark.contract.model.request.MeterConsumptionLeaveReqDTO;
import com.crlink.smartpark.contract.model.request.SettlementPayReqDTO;
import com.crlink.smartpark.contract.model.vo.RentContractLeaveListVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 租赁合同交易明细 服务类
 * </p>
 * */
public interface MeterConsumptionLeaveService extends IService<MeterConsumptionLeave> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionLeave, ?> sValue);

    MeterConsumptionLeaveDTO leaveSettlement(MeterConsumptionLeaveReqDTO reqDTO);

    Boolean settlementPay(SettlementPayReqDTO reqDTO);

    MeterConsumptionLeaveDTO getRentContractLeaveById(String id);

    PageInfo<RentContractLeaveListVO> listRentContractLeavePage(RentContractLeaveQuery query);

    void export(RentContractLeaveQuery query);

    ChargeDetail computeAmount(ComputeAmountDTO dto);

//    /**
//     * 保存或更新数据
//     *
//     * @return 实体对象
//     */
//    Boolean saveOrUpdateMeterConsumptionLeave(MeterConsumptionLeaveReqDTO reqDTO);
//
//    /**
//     *  根据id查询实体对象
//     *
//     * @param id 查询条件
//     * @return   实体对象
//     */
//    MeterConsumptionLeaveDTO getMeterConsumptionLeaveById(String id);
//
//    /**
//     * 分页查询
//     *
//     * @param query 查询对象
//     * @return 分页结果
//     */
//    PageInfo<MeterConsumptionLeaveDTO> listMeterConsumptionLeavePage(MeterConsumptionLeaveQuery query);


}
