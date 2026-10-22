package com.crlink.smartpark.contract.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.entity.MeterConsumptionLateFee;
import com.crlink.smartpark.contract.entity.RentLateFee;
import com.crlink.smartpark.contract.model.excel.RentLateFeeExportDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionLateFeeQuery;
import com.crlink.smartpark.contract.model.MeterConsumptionLateFeeReqDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionLateFeeDTO;
import com.crlink.smartpark.contract.service.MeterConsumptionLateFeeService;
import com.crlink.smartpark.contract.mapper.MeterConsumptionLateFeeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.collection.CollectionUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import com.google.common.collect.Lists;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 水电滞纳金费用 服务实现类
 * */
@Service
public class MeterConsumptionLateFeeServiceImpl extends ServiceImpl<MeterConsumptionLateFeeMapper, MeterConsumptionLateFee> implements MeterConsumptionLateFeeService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionLateFee, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeterConsumptionLateFee> lambdaQuery = this.lambdaQuery().in(MeterConsumptionLateFee::getId, ids);
        if (flag) {
            lambdaQuery.select(MeterConsumptionLateFee::getId, sValue);
        }
        Stream<MeterConsumptionLateFee> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeterConsumptionLateFee::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeterConsumptionLateFee::getId, Function.identity()));
        }
        return result;
    }

    @Override
    public Boolean saveOrUpdateRentLateFee(MeterConsumptionLateFeeReqDTO reqDTO) {
        List<MeterConsumptionLateFee> meterConsumptionLateFeeList = this.listByIds(reqDTO.getIds());
        if (CollectionUtil.isEmpty(meterConsumptionLateFeeList)) {
            return false;
        }
        meterConsumptionLateFeeList.forEach(meterConsumptionLateFee -> {
            if (meterConsumptionLateFee.getStatus().equals(RentFeeConstants.RentLateStatusEnum.COMPUTING.status)) {
                meterConsumptionLateFee.setReduceFine(meterConsumptionLateFee.getReceiveFine());
                meterConsumptionLateFee.setReceiveFine(BigDecimal.ZERO);
                meterConsumptionLateFee.setStatus(RentFeeConstants.RentLateStatusEnum.CANCELED.status);
                meterConsumptionLateFee.setUserId(SystemUtil.getInstance().getCurrentUserId());
                meterConsumptionLateFee.setHandleTime(LocalDateTime.now());
                EntityUserUtil.setUpdateBy(meterConsumptionLateFee);
            }
        });
        return updateBatchById(meterConsumptionLateFeeList);
    }

    @Override
    public void export(MeterConsumptionLateFeeQuery reqDTO) {
        ExcelExportUtils.export(() -> this.list(getConditionWrapper(reqDTO)),
                sourceList -> MyBeanUtils.copyList(sourceList, RentLateFeeExportDTO::new), RentFeeConstants.RENT_LATE_EXPORT_FILENAME, RentLateFeeExportDTO.class);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeterConsumptionLateFeeDTO> listMeterConsumptionLateFeePage(MeterConsumptionLateFeeQuery query) {
        query.startPage();
        List<MeterConsumptionLateFee> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<MeterConsumptionLateFeeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeterConsumptionLateFee> getConditionWrapper(MeterConsumptionLateFeeQuery query) {
        LambdaQueryWrapper<MeterConsumptionLateFee> queryWrapper = new LambdaQueryWrapper<MeterConsumptionLateFee>();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeterConsumptionLateFee::getParkOrgId, query.getParkOrgId());
        queryWrapper.like(StringUtils.isNotBlank(query.getCompanyName()), MeterConsumptionLateFee::getCompanyName, query.getCompanyName());
        queryWrapper.eq(query.getStatus() != null, MeterConsumptionLateFee::getStatus, query.getStatus());
        queryWrapper.eq(query.getContractType() != null, MeterConsumptionLateFee::getContractType, query.getContractType());
        queryWrapper.in(MeterConsumptionLateFee::getParkOrgId, parkOrgIds).orderByDesc(MeterConsumptionLateFee::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeterConsumptionLateFeeDTO> entitiesToDTOs(List<MeterConsumptionLateFee> entities) {
        return MyBeanUtils.copyList(entities, MeterConsumptionLateFeeDTO::new, (s, t) -> {

        });
    }
}
