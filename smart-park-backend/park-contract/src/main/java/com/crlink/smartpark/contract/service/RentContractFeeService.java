package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.entity.Contract;
import com.crlink.smartpark.contract.entity.RentContractBill;
import com.crlink.smartpark.contract.entity.RentContractFee;
import com.crlink.smartpark.contract.model.query.RentContractFeeQuery;
import com.crlink.smartpark.contract.model.request.RentContractFeeCashDTO;
import com.crlink.smartpark.contract.model.request.RentContractFeeReqDTO;
import com.crlink.smartpark.contract.dto.RentContractFeeDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.model.vo.RentContractFeeVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 租赁合同费用 服务类
 * </p>
 * */
public interface RentContractFeeService extends IService<RentContractFee> {

    /**
     * 根据Id查询值
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<RentContractFee, T> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean modifyDate(RentContractFeeReqDTO reqDTO);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean transform(RentContractFeeReqDTO reqDTO);


    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean refund(RentContractFeeCashDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    RentContractFeeVO getRentContractFeeById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<RentContractFeeDTO> listRentContractFeePage(RentContractFeeQuery query);


    void createRentContractFee(Contract contract);

    void createApartContractFee(Contract contract);

    /**
     * 导出
     *
     * @param query 查询对象
     * @return 分页结果
     */
    void exportFee(RentContractFeeQuery query);

    void createLate();
}
