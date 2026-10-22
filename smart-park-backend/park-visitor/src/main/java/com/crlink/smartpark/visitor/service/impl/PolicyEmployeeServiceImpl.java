package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.visitor.dto.DeviceIdsAndCardNosDTO;
import com.crlink.smartpark.visitor.dto.PolicyEmployeeDTO;
import com.crlink.smartpark.visitor.entity.PolicyEmployee;
import com.crlink.smartpark.visitor.mapper.PolicyEmployeeMapper;
import com.crlink.smartpark.visitor.service.PolicyEmployeeService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * 策略员工表 服务实现类
 * */
@Slf4j
@Service
public class PolicyEmployeeServiceImpl extends ServiceImpl<PolicyEmployeeMapper, PolicyEmployee> implements PolicyEmployeeService {

    /**
     * 刷新策略员工信息
     *
     * @param policyId
     * @param policyEmployees
     * @date 2022-11-25     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(String policyId, List<PolicyEmployee> policyEmployees) {
        log.info("刷新策略员工信息policyId: {}", policyId);
        //this.removeByPolicyId(policyId);
        this.remove(Wrappers.<PolicyEmployee>lambdaQuery().eq(PolicyEmployee::getPolicyId, policyId));
        if (CollUtil.isNotEmpty(policyEmployees)) {
            this.saveBatch(policyEmployees);
        }
    }

    /**
     *  根据policyId查询
     *
     * @param policyId 查询条件
     * @return
     */
    @Override
    public DeviceIdsAndCardNosDTO getDeviceIdsAndCardNosByPolicyId(String policyId) {
        List<PolicyEmployee> entities = this.list(Wrappers.<PolicyEmployee>lambdaQuery().eq(PolicyEmployee::getPolicyId, policyId));
        DeviceIdsAndCardNosDTO deviceIdsAndCardNosDTO = parseDeviceIdsAndCardNosDTO(entities);
        log.info("查询策略员工信息policyId: {}, policyEmployeeCollectionDTO: {}", policyId, deviceIdsAndCardNosDTO);
        return deviceIdsAndCardNosDTO;
    }



    @NotNull
    private DeviceIdsAndCardNosDTO parseDeviceIdsAndCardNosDTO(List<PolicyEmployee> entities) {
        Set<String> deviceIds = Sets.newHashSet();
        Set<String> cardNos = Sets.newHashSet();
        for (PolicyEmployee policyEmployee : entities) {
            deviceIds.addAll(policyEmployee.getDeviceIds());
            cardNos.add(policyEmployee.getCardNo());
        }
        DeviceIdsAndCardNosDTO collectionDTO = new DeviceIdsAndCardNosDTO();
        collectionDTO.setDeviceIds(deviceIds);
        collectionDTO.setCardNos(cardNos);
        return collectionDTO;
    }

    @Override
    public DeviceIdsAndCardNosDTO getDeviceIdsAndCardNosByPersonId(Long personId) {
        List<PolicyEmployee> entities = this.list(Wrappers.<PolicyEmployee>lambdaQuery().eq(PolicyEmployee::getDevicePersonId, personId));
        DeviceIdsAndCardNosDTO deviceIdsAndCardNosDTO = parseDeviceIdsAndCardNosDTO(entities);
        log.info("查询策略员工信息personId: {}, policyEmployeeCollectionDTO: {}", personId, deviceIdsAndCardNosDTO);
        return deviceIdsAndCardNosDTO;
    }

    /**
     * 删除
     * @param policyId
     */
    //@Override
    //public boolean removeByPolicyId(String policyId) {
    //    log.info("删除策略员工信息policyId: {}", policyId);
    //    return this.remove(Wrappers.<PolicyEmployee>lambdaQuery().eq(PolicyEmployee::getPolicyId, policyId));
    //}

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<PolicyEmployeeDTO> entitiesToDTOs(List<PolicyEmployee> entities) {
        return MyBeanUtils.copyList(entities, PolicyEmployeeDTO::new, (s, t) -> {

        });
    }
}
