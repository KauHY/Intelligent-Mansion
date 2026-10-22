package com.crlink.smartpark.contract.task;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.ChargeDetail;
import com.crlink.smartpark.contract.entity.ContractBillingPlan;
import com.crlink.smartpark.contract.entity.MeterConsumptionBill;
import com.crlink.smartpark.contract.entity.MeterConsumptionFee;
import com.crlink.smartpark.contract.entity.MeterConsumptionLateFee;
import com.crlink.smartpark.contract.model.MeterConsumptionFeeBasic;
import com.crlink.smartpark.contract.model.MeterLateBasic;
import com.crlink.smartpark.contract.service.ContractBillingPlanService;
import com.crlink.smartpark.contract.service.MeterConsumptionBillService;
import com.crlink.smartpark.contract.service.MeterConsumptionFeeService;
import com.crlink.smartpark.contract.service.MeterConsumptionLateFeeService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.device.dto.Consumption;
import com.crlink.smartpark.device.dto.MeterConsumptionDTO;
import com.crlink.smartpark.device.feign.MeterConsumptionProvider;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.YEAR_MONTH_FORMAT;
import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_CODE_PREFIX;

/**
 * 水电费用生成
 *
 **/
@Service
@Slf4j
public class MeterConsumptionCreateTask {

    @Autowired
    private MeterConsumptionProvider meterConsumptionProvider;

    @Autowired
    private FeeProvider feeProvider;

    @Autowired
    private MeterConsumptionFeeService meterConsumptionFeeService;

    /**
     * 自动水电费用生成
     *
     * @return
     */
    @Scheduled(cron = "0 30 2 1 * ?")
    private void meterFeeCreate() {
        log.info("自动水电费用生成启动！");
        String settledMonth = LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT));
        //获取企业用量
        List<MeterConsumptionDTO> consumptionDTOList = meterConsumptionProvider.listMeterConsumption(settledMonth);
        if (CollectionUtil.isNotEmpty(consumptionDTOList)) {
            Set<String> parkOrgIds = consumptionDTOList.parallelStream().map(MeterConsumptionDTO::getParkOrgId).collect(Collectors.toSet());
            Map<String, List<BillingSchemeDTO>> listMap = feeProvider.listBillingScheme(parkOrgIds);
            if (CollectionUtil.isNotEmpty(listMap)) {
                consumptionDTOList.forEach(meterConsumptionDTO -> {
                    List<MeterConsumptionFee> meterConsumptionFeeList = new ArrayList<>();
                    List<Consumption> consumptionList = meterConsumptionDTO.getConsumption();
                    List<BillingSchemeDTO> schemeDTOList = listMap.get(meterConsumptionDTO.getParkOrgId());
                    if (CollectionUtil.isNotEmpty(schemeDTOList)) {
                        Map<Integer, BillingSchemeDTO> schemeDTOMap = schemeDTOList.parallelStream().collect(Collectors.toMap(BillingSchemeDTO::getFeeType, Function.identity()));
                        if (CollectionUtil.isNotEmpty(schemeDTOMap)) {
                            BillingSchemeDTO waterMeter = schemeDTOMap.get(RentFeeConstants.FeeTypeToCalculation.WATER_METER.getType());
                            BillingSchemeDTO eleMeter = schemeDTOMap.get(RentFeeConstants.FeeTypeToCalculation.ELECTRICITY_METER.getType());
                            //计算水电费及计费周期
                            consumptionList.forEach(consumption -> {
                                List<ChargeDetail> chargeDetailList = new ArrayList<>();
                                MeterConsumptionFee meterConsumptionFee = new MeterConsumptionFee();
                                meterConsumptionFee.setContractType(meterConsumptionDTO.getContractType());
                                meterConsumptionFee.setParkOrgId(meterConsumptionDTO.getParkOrgId());
                                meterConsumptionFee.setParkOrgName(meterConsumptionDTO.getParkOrgName());
                                meterConsumptionFee.setCompanyId(meterConsumptionDTO.getCompanyId());
                                meterConsumptionFee.setCompanyName(meterConsumptionDTO.getCompanyName());
                                meterConsumptionFee.setHouseIds(meterConsumptionDTO.getHouseIds());
                                meterConsumptionFee.setSettledMonth(settledMonth);
                                meterConsumptionFee.setDataSource(RentFeeConstants.RentDataSourceEnum.FEE_CREATE.status);
                                meterConsumptionFee.setSettledTime(LocalDateTime.now());
                                if (Objects.equals(RentFeeConstants.DeviceType.WATER_METER.type, consumption.getDeviceType())) {
                                    meterConsumptionFee.setChargeName(RentFeeConstants.WATER_CHARGE_NAMES);
                                    setMeterFeeCharge(meterConsumptionFeeList, meterConsumptionFee, waterMeter, consumption, chargeDetailList, RentFeeConstants.DeviceType.WATER_METER.type);
                                } else if (Objects.equals(RentFeeConstants.DeviceType.ELECTRICITY_METER.type, consumption.getDeviceType())) {
                                    meterConsumptionFee.setChargeName(RentFeeConstants.POWER_CHARGE_NAMES);
                                    setMeterFeeCharge(meterConsumptionFeeList, meterConsumptionFee, eleMeter, consumption, chargeDetailList, RentFeeConstants.DeviceType.ELECTRICITY_METER.type);
                                }
                            });
                            meterConsumptionFeeService.saveBatch(meterConsumptionFeeList);
                        }
                    }
                });

            }
        }
        log.info("自动水电费用生成结束！");
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
            if (Objects.equals(type, RentFeeConstants.DeviceType.WATER_METER.type)) {
                chargeDetail.setChargeName("水费");
            } else {
                chargeDetail.setChargeName("电费");
            }
            chargeDetail.setAmountCount(roomDecimal);
            chargeDetail.setRoomAmount(consumption.getRoomAmount());
            chargeDetail.setTotalAmount(consumption.getTotalAmount());
            chargeDetailList.add(chargeDetail);
            ChargeDetail chargeDetailShare = new ChargeDetail();
            if (Objects.equals(type, RentFeeConstants.DeviceType.WATER_METER.type)) {
                chargeDetailShare.setChargeName("公摊水费");
            } else {
                chargeDetailShare.setChargeName("公摊电费");
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
