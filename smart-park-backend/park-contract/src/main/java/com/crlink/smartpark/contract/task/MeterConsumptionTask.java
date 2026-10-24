package com.crlink.smartpark.contract.task;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.ChargeDetail;
import com.crlink.smartpark.contract.entity.MeterConsumptionBill;
import com.crlink.smartpark.contract.entity.MeterConsumptionFee;
import com.crlink.smartpark.contract.model.MeterConsumptionFeeBasic;
import com.crlink.smartpark.contract.service.ContractBillingPlanService;
import com.crlink.smartpark.contract.service.MeterConsumptionBillService;
import com.crlink.smartpark.contract.service.MeterConsumptionFeeService;
import com.crlink.smartpark.contract.service.MeterConsumptionLateFeeService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.device.feign.MeterConsumptionProvider;
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

import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_CODE_PREFIX;

/**
 * 水电费用生成
 *
 **/
@Service
@Slf4j
public class MeterConsumptionTask {

    @Autowired
    private MeterConsumptionFeeService meterConsumptionFeeService;

    @Autowired
    private MeterConsumptionBillService meterConsumptionBillService;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private ApartmentPersonHouseRelationProvider personHouseRelationProvider;


    /**
     * 自动未出费用转已出和账单
     *
     * @return
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void meterFeeUpdate() {

        log.info("自动未出费用转已出和账单启动！");
        List<MeterConsumptionFee> meterConsumptionFeeList = meterConsumptionFeeService.lambdaQuery()
                .eq(MeterConsumptionFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.BINDING.status).lt(MeterConsumptionFee::getDueDate, LocalDate.now()).list();
        if (CollectionUtil.isEmpty(meterConsumptionFeeList)) {
            return;
        }
        List<MeterConsumptionBill> meterConsumptionBillList = getSaveBillData(meterConsumptionFeeList);
        meterConsumptionBillService.saveBatch(meterConsumptionBillList);
        meterConsumptionFeeService.updateBatchById(meterConsumptionFeeList);
        log.info("自动未出费用转已出和账单结束！");

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
                    int houseSize = houseIds.size();
                    BigDecimal billReceivableAmount = meterConsumptionFee.getReceivableAmount()
                            .divide(new BigDecimal(houseSize), 2, BigDecimal.ROUND_HALF_DOWN);
                    List<ChargeDetail> chargeDetail = meterConsumptionFee.getChargeDetail();
                    chargeDetail.forEach(charge -> {
                        BigDecimal amountCount = charge.getAmountCount();
                        if (amountCount.compareTo(BigDecimal.ZERO) == Constants.ZERO) {
                            charge.setAmountCount(BigDecimal.ZERO);
                        } else {
                            charge.setAmountCount(amountCount.divide(new BigDecimal(houseSize), 2, BigDecimal.ROUND_HALF_DOWN));
                        }
                    });
                    meterConsumptionBill.setChargeDetail(chargeDetail);
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
        meterConsumptionBill.setAccountPeriod(meterConsumptionFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
        //账单编码
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(RENT_BILL_CODE_PREFIX);
        LocalDateTime dateTime = numberSeq.getDateTime();
        String billNo = new StringBuilder(RENT_BILL_CODE_PREFIX).append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHH")))
                .append(numberSeq.getValue()).toString();
        meterConsumptionBill.setBillNo(billNo);
        return meterConsumptionBill;
    }

}
