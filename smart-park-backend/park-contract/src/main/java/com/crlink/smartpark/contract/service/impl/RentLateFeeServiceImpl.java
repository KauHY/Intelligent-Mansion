package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.RentLateFeeDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionLateFee;
import com.crlink.smartpark.contract.entity.RentLateFee;
import com.crlink.smartpark.contract.mapper.RentLateFeeMapper;
import com.crlink.smartpark.contract.model.ChargeStaReqDTO;
import com.crlink.smartpark.contract.model.ChargeStaTrendDTO;
import com.crlink.smartpark.contract.model.excel.RentLateFeeExportDTO;
import com.crlink.smartpark.contract.model.query.RentLateFeeQuery;
import com.crlink.smartpark.contract.model.request.RentLateFeeReqDTO;
import com.crlink.smartpark.contract.model.vo.LateCustomerVO;
import com.crlink.smartpark.contract.model.vo.OverDueTrendVO;
import com.crlink.smartpark.contract.service.MeterConsumptionLateFeeService;
import com.crlink.smartpark.contract.service.RentLateFeeService;
import com.crlink.smartpark.core.utils.*;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 租赁滞纳金费用 服务实现类
 * */
@Service
public class RentLateFeeServiceImpl extends ServiceImpl<RentLateFeeMapper, RentLateFee> implements RentLateFeeService {

    @Autowired
    private MeterConsumptionLateFeeService meterConsumptionLateFeeService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<RentLateFee, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<RentLateFee> lambdaQuery = this.lambdaQuery().in(RentLateFee::getId, ids);
        if (flag) {
            lambdaQuery.select(RentLateFee::getId, sValue);
        }
        Stream<RentLateFee> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(RentLateFee::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(RentLateFee::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateRentLateFee(RentLateFeeReqDTO reqDTO) {
        List<RentLateFee> rentLateFeeList = this.listByIds(reqDTO.getIds());
        if (CollectionUtil.isEmpty(rentLateFeeList)) {
            return false;
        }
        rentLateFeeList.forEach(rentLateFee -> {
            if (rentLateFee.getStatus().equals(RentFeeConstants.RentLateStatusEnum.COMPUTING.status)) {
                rentLateFee.setReduceFine(rentLateFee.getReceiveFine());
                rentLateFee.setReceiveFine(BigDecimal.ZERO);
                rentLateFee.setStatus(RentFeeConstants.RentLateStatusEnum.CANCELED.status);
                rentLateFee.setUserId(SystemUtil.getInstance().getCurrentUserId());
                rentLateFee.setHandleTime(LocalDateTime.now());
                EntityUserUtil.setUpdateBy(rentLateFee);
            }
        });
        return updateBatchById(rentLateFeeList);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public RentLateFeeDTO getRentLateFeeById(String id) {
        RentLateFee entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    @Override
    public void export(RentLateFeeQuery reqDTO) {
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
    public PageInfo<RentLateFeeDTO> listRentLateFeePage(RentLateFeeQuery query) {
        query.startPage();
        List<RentLateFee> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<RentLateFeeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public List<LateCustomerVO> lateCustomers(ChargeStaReqDTO reqDTO) {
        if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.COMPANY_METER_BILL.type)) {
            return getCusMeterList(reqDTO.getMonth(), RentFeeConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.APARTMENT_METER_BILL.type)) {
            return getCusMeterList(reqDTO.getMonth(), RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.RENTAL_BILL.type)) {
            return getCusFeeList(reqDTO.getMonth(), RentFeeConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.PROPERTY_BILL.type)) {
            return getCusFeeList(reqDTO.getMonth(), RentFeeConstants.ContractTypeToSchemeFeeType.PROPERTY_CONTRACT.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.APARTMENT_BILL.type)) {
            return getCusFeeList(reqDTO.getMonth(), RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else {
            List<LateCustomerVO> voList = new ArrayList<>();
            List<RentLateFee> lateFeeList = this.lambdaQuery().select(RentLateFee::getCompanyName, RentLateFee::getDueDays, RentLateFee::getDueAmount)
                    .eq(RentLateFee::getParkOrgId, reqDTO.getParkOrgId())
                    .eq(RentLateFee::getAccountPeriod, reqDTO.getMonth()).eq(RentLateFee::getStatus, RentFeeConstants.RentLateStatusEnum.COMPUTING.status).orderByDesc(RentLateFee::getDueDays)
                    .last("limit 5").list();
            List<LateCustomerVO> lateCustomerVOList = MyBeanUtils.copyList(lateFeeList, LateCustomerVO::new);
            List<MeterConsumptionLateFee> consumptionLateFeeList = meterConsumptionLateFeeService.lambdaQuery().select(MeterConsumptionLateFee::getCompanyName, MeterConsumptionLateFee::getDueDays, MeterConsumptionLateFee::getDueAmount)
                    .eq(MeterConsumptionLateFee::getParkOrgId, reqDTO.getParkOrgId())
                    .eq(MeterConsumptionLateFee::getSettledMonth, reqDTO.getMonth()).eq(MeterConsumptionLateFee::getStatus, RentFeeConstants.RentLateStatusEnum.COMPUTING.status).orderByDesc(MeterConsumptionLateFee::getDueDays)
                    .last("limit 5").list();
            List<LateCustomerVO> lateCustomerVOS = MyBeanUtils.copyList(consumptionLateFeeList, LateCustomerVO::new);
            voList.addAll(lateCustomerVOS);
            voList.addAll(lateCustomerVOList);
            voList.stream().sorted(Comparator.comparing(LateCustomerVO::getDueDays).reversed()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(voList) && voList.size() > RentFeeConstants.FIVE_INT) {
                voList = voList.stream().limit(RentFeeConstants.FIVE_INT).collect(Collectors.toList());
            }
            return voList;
        }

    }

    @Override
    public List<OverDueTrendVO> overdueTrend(ChargeStaTrendDTO reqDTO) {
        List<String> monthList = DateUtil.getBetweenMonthStr(reqDTO.getStartMonth(), reqDTO.getEndMonth());
        List<OverDueTrendVO> trendVOList = new ArrayList<>();
        if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.COMPANY_METER_BILL.type)) {
            getMeterList(trendVOList, monthList, RentFeeConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.APARTMENT_METER_BILL.type)) {
            getMeterList(trendVOList, monthList, RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.RENTAL_BILL.type)) {
            getFeeList(trendVOList, monthList, RentFeeConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.PROPERTY_BILL.type)) {
            getFeeList(trendVOList, monthList, RentFeeConstants.ContractTypeToSchemeFeeType.PROPERTY_CONTRACT.getType(), reqDTO.getParkOrgId());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.APARTMENT_BILL.type)) {
            getFeeList(trendVOList, monthList, RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1.getType(), reqDTO.getParkOrgId());
        } else {
            monthList.forEach(month -> {
                OverDueTrendVO overDueTrendVO = new OverDueTrendVO();
                List<RentLateFee> lateFeeList = this.lambdaQuery().select(RentLateFee::getDueAmount, RentLateFee::getCompanyId)
                        .eq(RentLateFee::getParkOrgId, reqDTO.getParkOrgId())
                        .eq(RentLateFee::getAccountPeriod, month).list();
                List<MeterConsumptionLateFee> meterLateFeeList = meterConsumptionLateFeeService.lambdaQuery()
                        .select(MeterConsumptionLateFee::getDueAmount, MeterConsumptionLateFee::getCompanyId)
                        .eq(MeterConsumptionLateFee::getParkOrgId, reqDTO.getParkOrgId())
                        .eq(MeterConsumptionLateFee::getSettledMonth, month).list();
                BigDecimal amountFee = lateFeeList.stream().map(RentLateFee::getDueAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal amountMeter = meterLateFeeList.stream().map(MeterConsumptionLateFee::getDueAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal amount = amountFee.add(amountMeter).setScale(2, BigDecimal.ROUND_HALF_UP);
                Integer customers = lateFeeList.size() + meterLateFeeList.size();
                overDueTrendVO.setMonth(month);
                overDueTrendVO.setDueAmount(amount.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN));
                overDueTrendVO.setCustomers(customers);
                trendVOList.add(overDueTrendVO);
            });
        }
        return trendVOList;
    }

    private List<LateCustomerVO> getCusMeterList(String month, Integer type, String parkOrgId) {
        List<RentLateFee> lateFeeList = this.lambdaQuery().select(RentLateFee::getCompanyName, RentLateFee::getDueDays, RentLateFee::getDueAmount)
                .eq(RentLateFee::getParkOrgId, parkOrgId)
                .eq(RentLateFee::getContractType, type)
                .eq(RentLateFee::getAccountPeriod, month).eq(RentLateFee::getStatus, RentFeeConstants.RentLateStatusEnum.COMPUTING.status).orderByDesc(RentLateFee::getDueDays)
                .last("limit 5").list();
        List<LateCustomerVO> lateCustomerVOList = MyBeanUtils.copyList(lateFeeList, LateCustomerVO::new);
        return lateCustomerVOList;
    }

    private List<LateCustomerVO> getCusFeeList(String month, Integer type, String parkOrgId) {
        List<MeterConsumptionLateFee> consumptionLateFeeList = meterConsumptionLateFeeService.lambdaQuery().select(MeterConsumptionLateFee::getCompanyName, MeterConsumptionLateFee::getDueDays, MeterConsumptionLateFee::getDueAmount)
                .eq(MeterConsumptionLateFee::getParkOrgId, parkOrgId)
                .eq(MeterConsumptionLateFee::getContractType, type).eq(MeterConsumptionLateFee::getSettledMonth, month).eq(MeterConsumptionLateFee::getStatus, RentFeeConstants.RentLateStatusEnum.COMPUTING.status).orderByDesc(MeterConsumptionLateFee::getDueDays)
                .last("limit 5").list();
        List<LateCustomerVO> lateCustomerVOList = MyBeanUtils.copyList(consumptionLateFeeList, LateCustomerVO::new);
        return lateCustomerVOList;
    }


    private void getMeterList(List<OverDueTrendVO> trendVOList, List<String> monthList, Integer type, String parkOrgId) {
        monthList.forEach(month -> {
            OverDueTrendVO overDueTrendVO = new OverDueTrendVO();
            List<MeterConsumptionLateFee> meterLateFeeList = meterConsumptionLateFeeService.lambdaQuery().select(MeterConsumptionLateFee::getDueAmount, MeterConsumptionLateFee::getCompanyId)
                    .eq(MeterConsumptionLateFee::getParkOrgId, parkOrgId)
                    .eq(MeterConsumptionLateFee::getContractType, type)
                    .eq(MeterConsumptionLateFee::getSettledMonth, month).list();
            BigDecimal amountMeter = meterLateFeeList.stream().map(MeterConsumptionLateFee::getDueAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            overDueTrendVO.setMonth(month);
            overDueTrendVO.setDueAmount(amountMeter.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN));
            overDueTrendVO.setCustomers(meterLateFeeList.size());
            trendVOList.add(overDueTrendVO);
        });
    }

    private void getFeeList(List<OverDueTrendVO> trendVOList, List<String> monthList, Integer type, String parkOrgId) {
        monthList.forEach(month -> {
            OverDueTrendVO overDueTrendVO = new OverDueTrendVO();
            List<RentLateFee> lateFeeList = this.lambdaQuery().select(RentLateFee::getDueAmount, RentLateFee::getCompanyId)
                    .eq(RentLateFee::getParkOrgId, parkOrgId)
                    .eq(RentLateFee::getContractType, type)
                    .eq(RentLateFee::getAccountPeriod, month).list();
            BigDecimal amountFee = lateFeeList.stream().map(RentLateFee::getDueAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            overDueTrendVO.setMonth(month);
            overDueTrendVO.setDueAmount(amountFee.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN));
            overDueTrendVO.setCustomers(lateFeeList.size());
            trendVOList.add(overDueTrendVO);
        });
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<RentLateFee> getConditionWrapper(RentLateFeeQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<RentLateFee> queryWrapper = new LambdaQueryWrapper<RentLateFee>();
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), RentLateFee::getParkOrgId, query.getParkOrgId());
        queryWrapper.like(StringUtils.isNotBlank(query.getCompanyName()), RentLateFee::getCompanyName, query.getCompanyName());
        queryWrapper.eq(query.getStatus() != null, RentLateFee::getStatus, query.getStatus());
        queryWrapper.eq(query.getContractType() != null, RentLateFee::getContractType, query.getContractType());
        queryWrapper.in(RentLateFee::getParkOrgId, parkOrgIds).orderByDesc(RentLateFee::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<RentLateFeeDTO> entitiesToDTOs(List<RentLateFee> entities) {
        return MyBeanUtils.copyList(entities, RentLateFeeDTO::new, (s, t) -> {

        });
    }
}
