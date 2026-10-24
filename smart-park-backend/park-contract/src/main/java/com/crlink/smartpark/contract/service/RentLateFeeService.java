package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.entity.RentLateFee;
import com.crlink.smartpark.contract.model.ChargeStaReqDTO;
import com.crlink.smartpark.contract.model.ChargeStaTrendDTO;
import com.crlink.smartpark.contract.model.query.RentLateFeeQuery;
import com.crlink.smartpark.contract.model.request.RentLateFeeReqDTO;
import com.crlink.smartpark.contract.dto.RentLateFeeDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.model.vo.LateCustomerVO;
import com.crlink.smartpark.contract.model.vo.OverDueTrendVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 租赁滞纳金费用 服务类
 * </p>
 * */
public interface RentLateFeeService extends IService<RentLateFee> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<RentLateFee, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateRentLateFee(RentLateFeeReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    RentLateFeeDTO getRentLateFeeById(String id);

    /**
     * 导出
     *
     * @return 实体对象
     */
    void export(RentLateFeeQuery reqDTO);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<RentLateFeeDTO> listRentLateFeePage(RentLateFeeQuery query);


    List<LateCustomerVO> lateCustomers(ChargeStaReqDTO reqDTO);

    List<OverDueTrendVO> overdueTrend(ChargeStaTrendDTO reqDTO);
}
