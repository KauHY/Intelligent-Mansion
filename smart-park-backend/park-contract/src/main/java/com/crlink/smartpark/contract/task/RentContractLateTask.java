package com.crlink.smartpark.contract.task;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.contract.entity.ContractBillingPlan;
import com.crlink.smartpark.contract.entity.RentContractBill;
import com.crlink.smartpark.contract.entity.RentLateFee;
import com.crlink.smartpark.contract.model.vo.RentContractLateBasic;
import com.crlink.smartpark.contract.service.ContractBillingPlanService;
import com.crlink.smartpark.contract.service.RentContractBillService;
import com.crlink.smartpark.contract.service.RentLateFeeService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 滞纳金费用生成
 *
 **/
@Service
@Slf4j
public class RentContractLateTask {

    @Autowired
    private RentContractBillService rentContractBillService;

    @Autowired
    private ContractBillingPlanService contractBillingPlanService;

    @Autowired
    private RentLateFeeService rentLateFeeService;

    //    @Scheduled(cron = "0 0 1 * * ?")
    public void rentFeeLateCreate() {

        log.info("自动滞纳金费用生成启动！");
        List<RentContractBill> rentContractBillList = rentContractBillService.lambdaQuery()
                .eq(RentContractBill::getStatus, RentFeeConstants.RentFeeBillEnum.UNRECEIVED.status).lt(RentContractBill::getLatestPayDate, LocalDate.now()).list();
        if (CollectionUtil.isEmpty(rentContractBillList)) {
            return;
        }
        List<RentLateFee> rentLateFeeList = new ArrayList<>();
        Set<String> ids = rentContractBillList.parallelStream().map(RentContractBill::getContractBillingPlanId).collect(Collectors.toSet());
        Set<String> billIds = rentContractBillList.parallelStream().map(RentContractBill::getId).collect(Collectors.toSet());
        Map<String, List<ContractBillingPlan>> billingPlanMap = contractBillingPlanService.lambdaQuery().in(ContractBillingPlan::getId, ids).list()
                .parallelStream().collect(Collectors.groupingBy(ContractBillingPlan::getId));
        Map<String, List<RentLateFee>> rentLateMap = rentLateFeeService.lambdaQuery().in(RentLateFee::getRentBillId, billIds).list()
                .parallelStream().collect(Collectors.groupingBy(RentLateFee::getRentBillId));
        if (CollectionUtil.isNotEmpty(billingPlanMap)) {
            rentContractBillList.forEach(rentContractBill -> {
                RentContractLateBasic contractLateBasic = MyBeanUtils.copy(rentContractBill, RentContractLateBasic::new);
                RentLateFee rentLateFee = MyBeanUtils.copy(contractLateBasic, RentLateFee::new);
                rentLateFee.setRentBillId(rentContractBill.getId());
                List<ContractBillingPlan> contractBillingPlanList = billingPlanMap.get(rentContractBill.getContractBillingPlanId());
                if (CollectionUtil.isNotEmpty(contractBillingPlanList)) {
                    ContractBillingPlan contractBillingPlan = contractBillingPlanList.get(0);
                    Integer lateCharge = contractBillingPlan.getLateCharge();
                    //相差天数
                    int days = 0;
                    if (contractBillingPlan.getExemptDays() != null) {
//                        days = Period.between(rentContractBill.getLatestPayDate(), LocalDate.now()).getDays() - contractBillingPlan.getExemptDays();
                        days = (int) (rentContractBill.getLatestPayDate().until(LocalDate.now(), ChronoUnit.DAYS)) - contractBillingPlan.getExemptDays();
                    } else {
//                        days = Period.between(rentContractBill.getLatestPayDate(), LocalDate.now()).getDays();
                        days = (int) rentContractBill.getLatestPayDate().until(LocalDate.now(), ChronoUnit.DAYS);
                    }
                    if (Objects.nonNull(lateCharge) && lateCharge.equals(1) && days > RentFeeConstants.ZERO) {
                        if (contractBillingPlan.getLateType() != null) {
                            BigDecimal receiveFine = new BigDecimal(1);
                            BigDecimal dayTotal = new BigDecimal(days);
                            //逾期金额计算
                            if (Objects.equals(contractBillingPlan.getLateType(), RentFeeConstants.RentLateRuleEnum.ONE.status)) {
                                rentLateFee.setReceiveFine(receiveFine.multiply(rentContractBill.getDueAmount()).multiply(dayTotal).multiply(contractBillingPlan.getLateRate().divide(new BigDecimal(100)))
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else if (Objects.equals(contractBillingPlan.getLateType(), RentFeeConstants.RentLateRuleEnum.TWO.status)) {
                                rentLateFee.setReceiveFine(receiveFine.multiply(contractBillingPlan.getLateDayAmount()).multiply(dayTotal).multiply(new BigDecimal(rentContractBill.getTotalArea()))
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else {
                                List<HouseMessage> houseMessageList = rentContractBill.getHouseMessages();
                                AtomicDouble coverArea = new AtomicDouble();
                                houseMessageList.forEach(houseMessage -> {
                                    coverArea.set(houseMessage.getCoverArea() + coverArea.get());
                                });
                                rentLateFee.setReceiveFine(receiveFine.multiply(contractBillingPlan.getLateDayAmount()).multiply(dayTotal).multiply(new BigDecimal(coverArea.get()))
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                            rentLateFee.setDueDays(days);
                            rentLateFee.setDueAmount(rentContractBill.getDueAmount());
                            rentLateFee.setReceivableFine(rentLateFee.getReceiveFine());
                            rentLateFee.setStatus(RentFeeConstants.RentLateStatusEnum.COMPUTING.status);
                            rentLateFee.setCreateTime(LocalDateTime.now());
                            //判断该账单的滞纳金是否存在 存在则更新
                            if (CollectionUtil.isNotEmpty(rentLateMap)) {
                                List<RentLateFee> lateFeeList = rentLateMap.get(rentContractBill.getId());
                                if (CollectionUtil.isNotEmpty(lateFeeList)) {
                                    rentLateFee.setId(lateFeeList.get(0).getId());
                                    rentLateFee.setStatus(lateFeeList.get(0).getStatus());
                                    rentLateFee.setUpdateTime(LocalDateTime.now());
                                }
                            }
                            rentLateFeeList.add(rentLateFee);
                        }
                    }
                }
            });
            rentLateFeeService.saveOrUpdateBatch(rentLateFeeList);
        }
        log.info("自动滞纳金费用生成结束！");
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now().plusDays(33));
        System.out.println(LocalDate.now().until(LocalDate.now().plusDays(33), ChronoUnit.DAYS));
    }
}
