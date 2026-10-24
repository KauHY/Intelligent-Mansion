package com.crlink.smartpark.contract.task;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.contract.entity.RentContractBill;
import com.crlink.smartpark.contract.entity.RentContractFee;
import com.crlink.smartpark.contract.model.RentContractFeeBasic;
import com.crlink.smartpark.contract.service.RentContractBillService;
import com.crlink.smartpark.contract.service.RentContractFeeService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
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
 * 租赁未出费用转已出和账单
 *
 **/
@Service
@Slf4j
public class RentContractOutTask {

    @Autowired
    private RentContractFeeService rentContractFeeService;

    @Autowired
    private RentContractBillService rentContractBillService;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private ApartmentPersonHouseRelationProvider personHouseRelationProvider;

    @Scheduled(cron = "0 15 0 * * ?")
    public void rentFeeUpdate() {

        log.info("自动未出费用转已出和账单启动！");
        List<RentContractFee> rentContractFeeList = rentContractFeeService.lambdaQuery()
                .eq(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.BINDING.status).lt(RentContractFee::getDueDate, LocalDate.now()).list();
        if (CollectionUtil.isEmpty(rentContractFeeList)) {
            return;
        }
        List<RentContractBill> rentContractBillList = getSaveBillData(rentContractFeeList);
        rentContractBillService.saveBatch(rentContractBillList);
        rentContractFeeService.updateBatchById(rentContractFeeList);
        log.info("自动未出费用转已出和账单结束！");

    }

    private List<RentContractBill> getSaveBillData(List<RentContractFee> rentContractFeeList) {
        Set<String> allHouseIds = Sets.newHashSet();
        List<RentContractBill> rentContractBillList = new ArrayList<>();
        rentContractFeeList.forEach(rentContractFee -> {
            rentContractFee.setBillingStatus(RentFeeConstants.RentFeeOutEnum.LOST.status);
            int days = Period.between(rentContractFee.getDueDate(), rentContractFee.getLatestPayDate()).getDays();
            rentContractFee.setDueDate(LocalDate.now());
            rentContractFee.setLatestPayDate(LocalDate.now().plusDays(days));
            rentContractFee.setUpdateTime(LocalDateTime.now());
            Set<Integer> feeTypes = APARTMENT_CONTRACT_1.getCalculationValues();
            if (Objects.equals(APARTMENT_CONTRACT_1.getType(), rentContractFee.getContractType()) && feeTypes.contains(rentContractFee.getFeeType())) {
                //公寓合同，根据房间数量拆分成多条账单数据，每一个房间有一个账单
                List<HouseMessage> houseMessages = rentContractFee.getHouseMessages();
                int size = houseMessages.size();
                houseMessages.forEach(t -> {
                    RentContractBill rentContractBill = setNewBillData(rentContractFee);
                    String houseId = t.getHouseId();
                    rentContractBill.setHouseId(houseId);
                    String name = t.getBuildingName() + t.getHouseName();
                    rentContractBill.setHouseName(Lists.newArrayList(name));
                    rentContractBill.setHouseMessages(Lists.newArrayList(t));
                    //每个房间的应收金额=费用总金额/房间数   保留2位小数、四舍五入
                    BigDecimal billReceivableAmount = rentContractFee.getReceivableAmount()
                            .divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_DOWN);
                    rentContractBill.setReceivableAmount(billReceivableAmount);
                    rentContractBill.setDueAmount(billReceivableAmount);
                    rentContractBillList.add(rentContractBill);
                    allHouseIds.add(houseId);
                });
            } else {
                rentContractBillList.add(setNewBillData(rentContractFee));
            }
        });
        if (CollectionUtil.isNotEmpty(allHouseIds)) {
            //取值于公寓人员管理中收款账单对应合同、对应房间的入住人员
            Map<String, Set<String>> employeeIdMap = personHouseRelationProvider.getEmployeeIdsByHouseId(allHouseIds);
            if (CollectionUtil.isNotEmpty(employeeIdMap)) {
                rentContractBillList.forEach(t -> {
                    Set<String> employeeIds = employeeIdMap.get(t.getHouseId());
                    if (CollectionUtils.isNotEmpty(employeeIds)) {
                        t.setEmployeeIds(new ArrayList<>(employeeIds));
                    }
                });
            }
        }
        return rentContractBillList;
    }

    private RentContractBill setNewBillData(RentContractFee rentContractFee) {
        //新增账单数据
        RentContractFeeBasic rentContractFeeBasic = MyBeanUtils.copy(rentContractFee, RentContractFeeBasic::new);
        RentContractBill rentContractBill = MyBeanUtils.copy(rentContractFeeBasic, RentContractBill::new);
        rentContractBill.setRentContractFeeId(rentContractFee.getId());
        //账单编码
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(RENT_BILL_CODE_PREFIX);
        LocalDateTime dateTime = numberSeq.getDateTime();
        String billNo = new StringBuilder(RENT_BILL_CODE_PREFIX).append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHH")))
                .append(numberSeq.getValue()).toString();
        rentContractBill.setBillNo(billNo);
        return rentContractBill;
    }

}
