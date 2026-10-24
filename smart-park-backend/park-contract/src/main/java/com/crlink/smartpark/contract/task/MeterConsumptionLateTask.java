package com.crlink.smartpark.contract.task;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.ChargeDetail;
import com.crlink.smartpark.contract.entity.MeterConsumptionBill;
import com.crlink.smartpark.contract.entity.MeterConsumptionFee;
import com.crlink.smartpark.contract.entity.MeterConsumptionLateFee;
import com.crlink.smartpark.contract.model.MeterConsumptionFeeBasic;
import com.crlink.smartpark.contract.model.MeterLateBasic;
import com.crlink.smartpark.contract.service.MeterConsumptionBillService;
import com.crlink.smartpark.contract.service.MeterConsumptionLateFeeService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.device.dto.Consumption;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.feign.FeeProvider;
import com.crlink.smartpark.system.feign.ApartmentPersonHouseRelationProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_CODE_PREFIX;

/**
 * 水电费用生成
 *
 **/
@Service
@Slf4j
public class MeterConsumptionLateTask {

    @Autowired
    private MeterConsumptionLateFeeService meterConsumptionLateFeeService;

    @Autowired
    private MeterConsumptionBillService meterConsumptionBillService;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private ApartmentPersonHouseRelationProvider personHouseRelationProvider;

    @Autowired
    private FeeProvider feeProvider;

    /**
     * 自动生成水电滞纳金费用
     *
     * @return
     */
    @Scheduled(cron = "0 40 0 * * ?")
    public void meterLateCreate() {

        log.info("自动生成水电滞纳金费用启动！");
        //获取未出费用
        List<MeterConsumptionBill> meterConsumptionBillList = meterConsumptionBillService.lambdaQuery()
                .eq(MeterConsumptionBill::getStatus, RentFeeConstants.RentFeeBillEnum.UNRECEIVED.status).lt(MeterConsumptionBill::getLatestPayDate, LocalDate.now()).list();
        if (CollectionUtil.isEmpty(meterConsumptionBillList)) {
            return;
        }
        List<MeterConsumptionLateFee> meterConsumptionLateFeeList = new ArrayList<>();
        Set<String> ids = meterConsumptionBillList.parallelStream().map(MeterConsumptionBill::getContractBillingPlanId).collect(Collectors.toSet());
        Set<String> billIds = meterConsumptionBillList.parallelStream().map(MeterConsumptionBill::getId).collect(Collectors.toSet());
        Map<String, BillingSchemeDTO> billingSchemeDTOMap = feeProvider.getBillingSchemeDTOByIds(ids);
        Map<String, List<MeterConsumptionLateFee>> rentLateMap = meterConsumptionLateFeeService.lambdaQuery().in(MeterConsumptionLateFee::getRentBillId, billIds).list()
                .parallelStream().collect(Collectors.groupingBy(MeterConsumptionLateFee::getRentBillId));
        if (CollectionUtil.isNotEmpty(billingSchemeDTOMap)) {
            //根据应收日期转已出费用并生成账单
            meterConsumptionBillList.forEach(meterConsumptionBill -> {
                MeterLateBasic meterLateBasic = MyBeanUtils.copy(meterConsumptionBill, MeterLateBasic::new);
                MeterConsumptionLateFee rentLateFee = MyBeanUtils.copy(meterLateBasic, MeterConsumptionLateFee::new);
                rentLateFee.setRentBillId(meterConsumptionBill.getId());
                BillingSchemeDTO billingSchemeDTO = billingSchemeDTOMap.get(meterConsumptionBill.getContractBillingPlanId());
                if (billingSchemeDTO != null) {
                    //相差天数
                    Integer lateCharge = billingSchemeDTO.getLateCharge();
                    int days = 0;
                    if (billingSchemeDTO.getExemptDays() != null) {
//                        days = Period.between(meterConsumptionBill.getLatestPayDate(), LocalDate.now()).getDays() - billingSchemeDTO.getExemptDays();
                        days = (int) (meterConsumptionBill.getLatestPayDate().until(LocalDate.now(), ChronoUnit.DAYS)) - billingSchemeDTO.getExemptDays();
                    } else {
//                        days = Period.between(meterConsumptionBill.getLatestPayDate(), LocalDate.now()).getDays();
                        days = (int) meterConsumptionBill.getLatestPayDate().until(LocalDate.now(), ChronoUnit.DAYS);
                    }
                    if (Objects.nonNull(lateCharge) && lateCharge.equals(1) && days > RentFeeConstants.ZERO) {
                        if (billingSchemeDTO.getLateType() != null) {
                            BigDecimal receiveFine = new BigDecimal(1);
                            BigDecimal dayTotal = new BigDecimal(days);
                            //逾期金额计算
                            if (Objects.equals(billingSchemeDTO.getLateType(), RentFeeConstants.RentLateRuleEnum.ONE.status)) {
                                rentLateFee.setReceiveFine(receiveFine.multiply(meterConsumptionBill.getDueAmount()).multiply(dayTotal).multiply(billingSchemeDTO.getLateRate().divide(new BigDecimal(100)))
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else if (Objects.equals(billingSchemeDTO.getLateType(), RentFeeConstants.RentLateRuleEnum.TWO.status)) {
                                rentLateFee.setReceiveFine(receiveFine.multiply(billingSchemeDTO.getLateDayAmount()).multiply(dayTotal).multiply(new BigDecimal(20))
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else {
                                rentLateFee.setReceiveFine(receiveFine.multiply(billingSchemeDTO.getLateDayAmount()).multiply(dayTotal).multiply(new BigDecimal(20))
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                            rentLateFee.setDueDays(days);
                            rentLateFee.setDueAmount(meterConsumptionBill.getDueAmount());
                            rentLateFee.setReceivableFine(rentLateFee.getReceiveFine());
                            rentLateFee.setStatus(RentFeeConstants.RentLateStatusEnum.COMPUTING.status);
                            rentLateFee.setCreateTime(LocalDateTime.now());
                            //判断该账单的滞纳金是否存在 存在则更新
                            if (CollectionUtil.isNotEmpty(rentLateMap)) {
                                List<MeterConsumptionLateFee> lateFeeList = rentLateMap.get(meterConsumptionBill.getId());
                                if (CollectionUtil.isNotEmpty(lateFeeList)) {
                                    rentLateFee.setId(lateFeeList.get(0).getId());
                                    rentLateFee.setStatus(lateFeeList.get(0).getStatus());
                                    rentLateFee.setUpdateTime(LocalDateTime.now());
                                }
                            }
                            meterConsumptionLateFeeList.add(rentLateFee);
                        }
                    }
                }

            });
            meterConsumptionLateFeeService.saveOrUpdateBatch(meterConsumptionLateFeeList);
        }
        log.info("自动生成水电滞纳金费用结束！");
    }


    private List<MeterConsumptionBill> getSaveBillData(List<MeterConsumptionFee> meterConsumptionFeeList) {
        Set<String> allHouseIds = Sets.newHashSet();
        List<MeterConsumptionBill> meterConsumptionBillList = new ArrayList<>();
        meterConsumptionFeeList.forEach(meterConsumptionFee -> {
            meterConsumptionFee.setBillingStatus(RentFeeConstants.RentFeeOutEnum.LOST.status);
            meterConsumptionFee.setDueDate(LocalDate.now());
            int days = Period.between(meterConsumptionFee.getDueDate(), meterConsumptionFee.getLatestPayDate()).getDays();
            meterConsumptionFee.setLatestPayDate(LocalDate.now().plusDays(days));
            if (Objects.equals(APARTMENT_CONTRACT_1.getType(), meterConsumptionFee.getContractType())) {
                //公寓合同，根据房间数量拆分成多条账单数据，每一个房间有一个账单
                List<String> houseIds = meterConsumptionFee.getHouseIds();
                houseIds.forEach(t -> {
                    MeterConsumptionBill meterConsumptionBill = setNewBillData(meterConsumptionFee);
                    //每个房间的应收金额=费用总金额/房间数   保留2位小数、四舍五入
                    BigDecimal billReceivableAmount = meterConsumptionFee.getReceivableAmount()
                            .divide(new BigDecimal(houseIds.size()), 2, BigDecimal.ROUND_HALF_DOWN);
                    meterConsumptionBill.setHouseIds(Lists.newArrayList(t));
                    meterConsumptionBill.setReceivableAmount(billReceivableAmount);
                    meterConsumptionBill.setDueAmount(billReceivableAmount);
                    meterConsumptionBillList.add(meterConsumptionBill);
                    allHouseIds.add(t);
                });
            } else {
                meterConsumptionBillList.add(setNewBillData(meterConsumptionFee));
            }
        });
        if (CollectionUtil.isNotEmpty(allHouseIds)) {
            //取值于公寓人员管理中收款账单对应合同、对应房间的入住人员
            Map<String, Set<String>> employeeIdMap = personHouseRelationProvider.getEmployeeIdsByHouseId(allHouseIds);
            if (CollectionUtil.isNotEmpty(employeeIdMap)) {
                meterConsumptionBillList.forEach(t -> {
                    Set<String> employeeIds = employeeIdMap.get(t.getHouseIds().get(RentFeeConstants.ZERO));
                    if (CollectionUtils.isNotEmpty(employeeIds)) {
                        t.setEmployeeIds(new ArrayList<>(employeeIds));
                    }
                });
            }
        }
        return meterConsumptionBillList;
    }

    private MeterConsumptionBill setNewBillData(MeterConsumptionFee meterConsumptionFee) {
        //新增账单数据
        MeterConsumptionFeeBasic meterConsumptionFeeBasic = MyBeanUtils.copy(meterConsumptionFee, MeterConsumptionFeeBasic::new);
        MeterConsumptionBill meterConsumptionBill = MyBeanUtils.copy(meterConsumptionFeeBasic, MeterConsumptionBill::new);
        meterConsumptionBill.setMeterConsumptionFeeId(meterConsumptionFee.getId());
        //账单编码
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(RENT_BILL_CODE_PREFIX);
        LocalDateTime dateTime = numberSeq.getDateTime();
        String billNo = new StringBuilder(RENT_BILL_CODE_PREFIX).append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHH")))
                .append(numberSeq.getValue()).toString();
        meterConsumptionBill.setBillNo(billNo);
        return meterConsumptionBill;
    }


    private void setMeterFeeCharge(List<MeterConsumptionFee> meterConsumptionFeeList, MeterConsumptionFee meterConsumptionFee, BillingSchemeDTO meterPlan, Consumption consumption, List<ChargeDetail> chargeDetailList, Integer type) {
        if (meterPlan != null) {
            meterConsumptionFee.setContractBillingPlanId(meterPlan.getId());
            meterConsumptionFee.setDueDate(LocalDate.now().plusDays(meterPlan.getDayOfMonth() - 1));
            meterConsumptionFee.setLatestPayDate(meterConsumptionFee.getDueDate().plusDays(meterPlan.getDaysForDelay()));
            BigDecimal roomDecimal = consumption.getRoomAmount().multiply(meterPlan.getUnitPriceWithoutTax());
            BigDecimal shareDecimal = consumption.getShareAmount().multiply(meterPlan.getUnitPriceWithoutTax());
            meterConsumptionFee.setReceivableAmount(roomDecimal.add(shareDecimal));
            meterConsumptionFee.setDueAmount(meterConsumptionFee.getReceivableAmount());
            meterConsumptionFee.setBillingStatus(RentFeeConstants.RentFeeOutEnum.BINDING.status);
            meterConsumptionFee.setFeeStatus(RentFeeConstants.RentFeeBillEnum.UNRECEIVED.status);
            ChargeDetail chargeDetail = new ChargeDetail();
            if (type.equals(RentFeeConstants.DeviceType.WATER_METER.type)) {
                chargeDetail.setChargeName("水费");
            } else {
                chargeDetail.setChargeName("电费");
            }
            chargeDetail.setAmountCount(roomDecimal);
            chargeDetail.setRoomAmount(consumption.getRoomAmount());
            chargeDetail.setTotalAmount(consumption.getTotalAmount());
            chargeDetailList.add(chargeDetail);
            ChargeDetail chargeDetailShare = new ChargeDetail();
            if (type.equals(RentFeeConstants.DeviceType.WATER_METER.type)) {
                chargeDetail.setChargeName("公摊水费");
            } else {
                chargeDetail.setChargeName("公摊电费");
            }
            chargeDetailShare.setAmountCount(shareDecimal);
            chargeDetailShare.setShareAmount(consumption.getShareAmount());
            chargeDetailShare.setPublicAmount(consumption.getPublicAmount());
            chargeDetailList.add(chargeDetailShare);
            meterConsumptionFee.setChargeDetail(chargeDetailList);
            meterConsumptionFeeList.add(meterConsumptionFee);
        }
    }

}
