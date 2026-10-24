package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.dto.DeviceIdsAndCardNosDTO;
import com.crlink.smartpark.visitor.entity.PolicyEmployee;

import java.util.List;

/**
 * <p>
 * 策略员工表 服务类
 * </p>
 * */
public interface PolicyEmployeeService extends IService<PolicyEmployee> {

    /**
     * 刷新策略员工信息
     *
     * @date 2022-11-25     * @param policyId
     * @param policyEmployees
     */
    void refresh(String policyId, List<PolicyEmployee> policyEmployees);

    /**
     *  根据policyId查询
     *
     * @param policyId 查询条件
     * @return
     */
    DeviceIdsAndCardNosDTO getDeviceIdsAndCardNosByPolicyId(String policyId);

    /**
     * 根据policyId查询
     * @param personId
     * @return
     */
    DeviceIdsAndCardNosDTO getDeviceIdsAndCardNosByPersonId(Long personId);

    ///**
    // * 删除
    // * @param policyId
    // */
    //boolean removeByPolicyId(String policyId);

}
