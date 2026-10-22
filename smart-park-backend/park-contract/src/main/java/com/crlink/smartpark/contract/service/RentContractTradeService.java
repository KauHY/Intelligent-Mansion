package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.RentContractTradeDTO;
import com.crlink.smartpark.contract.entity.RentContractTrade;
import com.crlink.smartpark.contract.model.query.RentContractTradeQuery;
import com.crlink.smartpark.contract.model.request.ReturnMoneyReqDTO;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 租赁合同交易明细 服务类
 * </p>
 * */
public interface RentContractTradeService extends IService<RentContractTrade> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<RentContractTrade, ?> sValue);

//    /**
//     * 保存或更新数据
//     *
//     * @return 实体对象
//     */
//    Boolean saveOrUpdateRentContractTrade(RentContractTradeReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    RentContractTradeDTO getRentContractTradeById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<RentContractTradeDTO> listRentContractTradePage(RentContractTradeQuery query);

    Boolean returnMoney(ReturnMoneyReqDTO dto);


    void export(RentContractTradeQuery query);

    /**
     * 获取交易明细编号
     *
     * @return
     */
    String getTradeNo();

    /**
     * 退款金额:大于0且小于等于“可退金额”的数字
     *
     * @param receivedAmount 可退金额即实付金额
     * @param returnAmount   退款金额
     */
    void judgeReturnAmount(BigDecimal receivedAmount, BigDecimal returnAmount);
}
