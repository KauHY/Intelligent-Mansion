package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.RentContractLeaveDTO;
import com.crlink.smartpark.contract.entity.RentContractLeave;
import com.crlink.smartpark.contract.model.query.RentContractLeaveQuery;
import com.crlink.smartpark.contract.model.request.RentContractLeaveReqDTO;
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
public interface RentContractLeaveService extends IService<RentContractLeave> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<RentContractLeave, ?> sValue);

    /**
     * 离场结算-计算费用
     *
     * @return 实体对象
     */
    RentContractLeaveDTO leaveSettlement(RentContractLeaveReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    RentContractLeaveDTO getRentContractLeaveById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<RentContractLeaveListVO> listRentContractLeavePage(RentContractLeaveQuery query);

    Boolean settlementPay(SettlementPayReqDTO reqDTO);


    void export(RentContractLeaveQuery query);
}
