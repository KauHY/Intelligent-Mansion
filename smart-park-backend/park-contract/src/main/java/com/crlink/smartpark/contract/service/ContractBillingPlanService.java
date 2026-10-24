package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.model.request.ContractBillingPlanReqDTO;
import com.crlink.smartpark.contract.model.query.ContractBillingPlanQuery;
import com.crlink.smartpark.contract.dto.ContractBillingPlanDTO;
import com.crlink.smartpark.contract.entity.ContractBillingPlan;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 合同计费信息 服务类
 * </p>
 * */
public interface ContractBillingPlanService extends IService<ContractBillingPlan> {


    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ContractBillingPlan, ?> sValue);

    Map<String, ?> contractId2ValueMap(Collection<String> contractIds, SFunction<ContractBillingPlan, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean saveOrUpdateContractBillingPlan(ContractBillingPlanReqDTO dto);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ContractBillingPlanDTO getContractBillingPlanById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<ContractBillingPlanDTO> listPage(ContractBillingPlanQuery condition);
}
