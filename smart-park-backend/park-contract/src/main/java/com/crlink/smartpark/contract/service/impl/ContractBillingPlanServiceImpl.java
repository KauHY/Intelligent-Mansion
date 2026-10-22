package com.crlink.smartpark.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.contract.model.request.ContractBillingPlanReqDTO;
import com.crlink.smartpark.contract.model.query.ContractBillingPlanQuery;
import com.crlink.smartpark.contract.dto.ContractBillingPlanDTO;
import com.crlink.smartpark.contract.entity.ContractBillingPlan;
import com.crlink.smartpark.contract.mapper.ContractBillingPlanMapper;
import com.crlink.smartpark.contract.service.ContractBillingPlanService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 合同计费信息 服务实现类
 * </p>
 * */
@Service
public class ContractBillingPlanServiceImpl extends ServiceImpl<ContractBillingPlanMapper, ContractBillingPlan> implements ContractBillingPlanService {

    /**
     * 保存或更新数据
     *
     * @return 存储对象
     */
    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ContractBillingPlan, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ContractBillingPlan> lambdaQuery = this.lambdaQuery().in(ContractBillingPlan::getId, ids);
        if (flag) {
            lambdaQuery.select(ContractBillingPlan::getId, sValue);
        }
        Stream<ContractBillingPlan> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ContractBillingPlan::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ContractBillingPlan::getId, Function.identity()));
        }
        return result;
    }

    @Override
    public Map<String, ?> contractId2ValueMap(Collection<String> contractIds, SFunction<ContractBillingPlan, ?> sValue) {
        if (ObjectUtils.isEmpty(contractIds)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ContractBillingPlan> lambdaQuery = this.lambdaQuery().in(ContractBillingPlan::getContractId, contractIds);
        if (flag) {
            lambdaQuery.select(ContractBillingPlan::getContractId, sValue);
        }
        Stream<ContractBillingPlan> stream = lambdaQuery.list().stream();
        Map<String, ?> result = null;
        if (flag) {
            result = stream.collect(Collectors.groupingBy(ContractBillingPlan::getContractId, Collectors.mapping(sValue, Collectors.toSet())));
        } else {
            result = stream.collect(Collectors.groupingBy(ContractBillingPlan::getContractId));
        }
        return result;
    }


    @Override
    public Boolean saveOrUpdateContractBillingPlan(ContractBillingPlanReqDTO reqDTO) {
        ContractBillingPlan entity = MyBeanUtils.copy(reqDTO, ContractBillingPlan::new);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public ContractBillingPlanDTO getContractBillingPlanById(String id) {
        if (StringUtils.isNotBlank(id)) {
            ContractBillingPlan entity = getById(id);
            if (entity != null) {
                ContractBillingPlanDTO dto = new ContractBillingPlanDTO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<ContractBillingPlanDTO> listPage(ContractBillingPlanQuery condition) {
        PageInfo<ContractBillingPlanDTO> pageResult = new PageInfo<ContractBillingPlanDTO>();
        if (condition != null) {
            Page<ContractBillingPlan> page;
            if (condition.getPageSize() != -1) {
                page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
            } else {
                page = new Page<ContractBillingPlan>();
            }
            PageHelper.orderBy("create_time desc");
            baseMapper.selectList(getConditionWrapper(condition));
            PageInfo<ContractBillingPlan> pageInfo = new PageInfo<ContractBillingPlan>(page);
            List<ContractBillingPlanDTO> dataList = listEntityToDto(pageInfo.getList());
            BeanUtils.copyProperties(pageInfo, pageResult, "list");
            pageResult.setList(dataList);
        }
        return pageResult;
    }

    /**
     * 封装查询条件
     *
     * @param condition 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryWrapper<ContractBillingPlan> getConditionWrapper(ContractBillingPlanQuery condition) {
        LambdaQueryWrapper<ContractBillingPlan> queryWrapper = new LambdaQueryWrapper<ContractBillingPlan>();
        if (condition != null) {
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @param list entity集合
     * @return DTO集合
     */
    private List<ContractBillingPlanDTO> listEntityToDto(List<ContractBillingPlan> list) {
        return list.stream().map(entity -> {
            ContractBillingPlanDTO temp = new ContractBillingPlanDTO();
            BeanUtils.copyProperties(entity, temp);
            return temp;
        }).collect(Collectors.toList());
    }
}
