package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.common.ContractConstants;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.DeductionMessage;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.contract.dto.RentContractBillRecordDTO;
import com.crlink.smartpark.contract.dto.RentContractFeeDTO;
import com.crlink.smartpark.contract.entity.*;
import com.crlink.smartpark.contract.mapper.RentContractFeeMapper;
import com.crlink.smartpark.contract.model.RentContractFeeBasic;
import com.crlink.smartpark.contract.model.excel.RentFeeCashExportDTO;
import com.crlink.smartpark.contract.model.excel.RentFeeInExportDTO;
import com.crlink.smartpark.contract.model.excel.RentFeeOutExportDTO;
import com.crlink.smartpark.contract.model.query.RentContractFeeQuery;
import com.crlink.smartpark.contract.model.request.RentBillCheckoutReqDTO;
import com.crlink.smartpark.contract.model.request.RentContractFeeCashDTO;
import com.crlink.smartpark.contract.model.request.RentContractFeeReqDTO;
import com.crlink.smartpark.contract.model.request.ReturnMoneyReqDTO;
import com.crlink.smartpark.contract.model.vo.RentContractFeeVO;
import com.crlink.smartpark.contract.model.vo.RentContractLateBasic;
import com.crlink.smartpark.contract.service.*;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.feign.ApartmentPersonHouseRelationProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicDouble;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_FORMAT;
import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_CODE_PREFIX;


/**
 * 租赁合同费用 服务实现类
 * */
@Service
public class RentContractFeeServiceImpl extends ServiceImpl<RentContractFeeMapper, RentContractFee> implements RentContractFeeService {

    @Autowired
    private ContractBillingPlanService contractBillingPlanService;
    @Autowired
    private HouseRelationService houseRelationService;

    @Autowired
    private RentContractBillRecordService rentContractBillRecordService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RentContractBillService rentContractBillService;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private ApartmentPersonHouseRelationProvider personHouseRelationProvider;

    @Autowired
    private RentLateFeeService rentLateFeeService;

    @Autowired
    private RentContractTradeService rentContractTradeService;

    @Autowired
    private RentContractLeaveService rentContractLeaveService;

    public static final BigDecimal THIRTY = new BigDecimal(30);

    public static final BigDecimal SEASON_DAYS = new BigDecimal(90);

    public static final BigDecimal YEAR_DAYS = new BigDecimal(360);

    public static final BigDecimal MONTHS_DAY = new BigDecimal(12);

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<RentContractFee, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(RentContractFee::getId, sValue)
                .in(RentContractFee::getId, ids).list().stream()
                .collect(HashMap::new, (map, entity) -> map.put(entity.getId(), sValue.apply(entity)), HashMap::putAll);
        return result;
    }

    /**
     * 日期仅能选当天及之后的日期
     * 应收日期为当前日期则即时将此未出费用更新为已出费用（更新此费用的应收、最晚缴纳日期）、并新增账单；
     * 若应收日期为今天之后的日期则更新此未出费用的应收、最晚缴纳日期。
     *
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyDate(RentContractFeeReqDTO reqDTO) {
        LocalDate dueDate = reqDTO.getDueDate();
        LocalDate now = LocalDate.now();
        if (dueDate.isBefore(now)) {
            throw new ResolinkRuntimeException("300011");
        }
        if (dueDate.isAfter(reqDTO.getLatestPayDate())) {
            throw new ResolinkRuntimeException("300012");
        }
        List<RentContractFee> rentContractFeeList = this.listByIds(reqDTO.getIds());
        if (CollectionUtil.isEmpty(rentContractFeeList)) {
            return false;
        }
        if (dueDate.isEqual(now)) {
            List<RentContractBill> rentContractBillList = getSaveBillData(rentContractFeeList, reqDTO.getLatestPayDate());
            rentContractBillService.saveBatch(rentContractBillList);
        } else {
            rentContractFeeList.forEach(rentContractFee -> {
                rentContractFee.setDueDate(dueDate);
                rentContractFee.setLatestPayDate(reqDTO.getLatestPayDate());
                EntityUserUtil.setUpdateBy(rentContractFee);
            });
        }
        return this.updateBatchById(rentContractFeeList);
    }

    /**
     * 保存或更新数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transform(RentContractFeeReqDTO reqDTO) {
        List<RentContractFee> rentContractFeeList = this.listByIds(reqDTO.getIds());
        if (CollectionUtil.isEmpty(rentContractFeeList)) {
            return false;
        }
        List<RentContractBill> rentContractBillList = getSaveBillData(rentContractFeeList, null);
        rentContractBillService.saveBatch(rentContractBillList);
        return updateBatchById(rentContractFeeList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean refund(RentContractFeeCashDTO reqDTO) {
        RentContractFee entity = Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        Contract contract = contractService.lambdaQuery().select(Contract::getState).eq(Contract::getId, entity.getContractId()).one();
        if (!entity.getCashStatus().equals(RentFeeConstants.RentCashStatusEnum.PAID_UP.status) && !contract.getState().equals(RentFeeConstants.CONTRACT_STATUS)) {
            throw new ResolinkRuntimeException("300008");
        }
        entity.setDeductionMsg(reqDTO.getDeductionMsg());
        entity.setCashStatus(RentFeeConstants.RentCashStatusEnum.CLEARED.status);
        if (reqDTO.getDeduction().equals(DictUtil.getValue(DictConstant.YesOrNo.YES))) {
            entity.setOffsetAmount(reqDTO.getOffsetAmount());
            entity.setRefundedAmount(reqDTO.getRefundedAmount());
        } else {
            entity.setRefundedAmount(entity.getReceivedAmount());
        }
        entity.setDeduction(reqDTO.getDeduction());
        entity.setUserId(SystemUtil.getInstance().getCurrentUserId());
        entity.setHandleTime(LocalDateTime.now());
        entity.setRemark(reqDTO.getRemark());
        EntityUserUtil.setUpdateBy(entity);
        entity.setLeaseDate(LocalDate.now());
        this.updateById(entity);
        List<DeductionMessage> deductionMsgList = reqDTO.getDeductionMsg();
        if (CollectionUtil.isNotEmpty(deductionMsgList)) {
            //抵扣账单处理
            Set<String> billIds = deductionMsgList.parallelStream().map(DeductionMessage::getId).collect(Collectors.toSet());
            RentBillCheckoutReqDTO checkoutReqDTO = new RentBillCheckoutReqDTO();
            checkoutReqDTO.setIds(billIds);
            checkoutReqDTO.setFromLeave(false);
            checkoutReqDTO.setPayType(1);
            checkoutReqDTO.setCheckoutDate(LocalDate.now());
            rentContractBillService.checkout(checkoutReqDTO);
        }
        List<RentContractBill> billList = rentContractBillService.lambdaQuery()
                .eq(RentContractBill::getRentContractFeeId, reqDTO.getId()).list();
        Boolean result = true;
        for (RentContractBill bill : billList) {
            ReturnMoneyReqDTO dto = new ReturnMoneyReqDTO();
            RentContractTrade rentContractTrade = Optional.ofNullable(rentContractTradeService.lambdaQuery().eq
                    (RentContractTrade::getBillId, bill.getId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            dto.setId(rentContractTrade.getId());
            dto.setPayType(RentFeeConstants.ONE_INT);
            dto.setReturnAmount(entity.getRefundedAmount());
            dto.setFromTrade(false);
            result = rentContractTradeService.returnMoney(dto);
        }
//        ReturnMoneyReqDTO dto = new ReturnMoneyReqDTO();
//        RentContractBill rentContractBill = Optional.ofNullable(rentContractBillService.lambdaQuery().eq
//                (RentContractBill::getRentContractFeeId, reqDTO.getId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
//        RentContractTrade rentContractTrade = Optional.ofNullable(rentContractTradeService.lambdaQuery().eq
//                (RentContractTrade::getBillId, rentContractBill.getId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
//        dto.setId(rentContractTrade.getId());
//        dto.setPayType(RentFeeConstants.ONE_INT);
//        dto.setReturnAmount(entity.getRefundedAmount());
//        dto.setFromTrade(false);
//        return rentContractTradeService.returnMoney(dto);
        return result;
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public RentContractFeeVO getRentContractFeeById(String id) {
        RentContractFee entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        RentContractFeeDTO rentContractFeeDTO = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
        RentContractFeeVO rentContractFeeVO = new RentContractFeeVO();
        List<RentContractBill> rentContractBillList = rentContractBillService.lambdaQuery().select(RentContractBill::getId).eq(RentContractBill::getRentContractFeeId, entity.getId()).list();
        if (CollectionUtil.isEmpty(rentContractBillList)) {
            rentContractFeeVO.setDeductionRecordList(null);
        } else {
            Set<String> billIds = rentContractBillList.parallelStream().map(RentContractBill::getId).collect(Collectors.toSet());
            rentContractFeeVO.setDeductionRecordList(getDeductionRecordList(billIds));
        }
        rentContractFeeVO.setRentContractFeeDTO(rentContractFeeDTO);
        return rentContractFeeVO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<RentContractFeeDTO> listRentContractFeePage(RentContractFeeQuery query) {
        query.startPage();
        List<RentContractFee> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<RentContractFeeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void exportFee(RentContractFeeQuery query) {
        List<RentContractFee> list = this.list(getConditionWrapper(query));
        if (RentFeeConstants.ZERO.equals(query.getQueryType())) {
            ExcelExportUtils.export(() -> this.list(getConditionWrapper(query)),
                    sourceList -> MyBeanUtils.copyList(list, RentFeeInExportDTO::new, (s, t) -> completeFeeInInfo(s, t)),
                    RentFeeConstants.RENT_IN_EXPORT_FILENAME, RentFeeInExportDTO.class);

        } else if (RentFeeConstants.ONE_INT.equals(query.getQueryType())) {
            ExcelExportUtils.export(() -> this.list(getConditionWrapper(query)),
                    sourceList -> MyBeanUtils.copyList(list, RentFeeOutExportDTO::new, (s, t) -> completeFeeOutInfo(s, t)),
                    RentFeeConstants.RENT_OUT_EXPORT_FILENAME, RentFeeOutExportDTO.class);
        } else {
            ExcelExportUtils.export(() -> this.list(getConditionWrapper(query)),
                    sourceList -> MyBeanUtils.copyList(list, RentFeeCashExportDTO::new, (s, t) -> completeFeeCashInfo(s, t)),
                    RentFeeConstants.RENT_CASH_EXPORT_FILENAME, RentFeeCashExportDTO.class);
        }
    }

    @Override
    public void createLate() {

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
    }

    @Override
    public void createRentContractFee(Contract contract) {
        List<ContractBillingPlan> billingPlans = contractBillingPlanService.lambdaQuery().eq(ContractBillingPlan::getContractId, contract.getId()).list();
        if (CollectionUtil.isNotEmpty(billingPlans)) {
            Map<String, List<HouseMessage>> houseMap = contract.getHouseMessages().parallelStream().collect(Collectors.groupingBy(HouseMessage::getHouseId));
            List<RentContractFee> rentContractFeeList = new ArrayList<>();
            if (contract.getType().equals(ContractConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT.getType())
                    || contract.getType().equals(ContractConstants.ContractTypeToSchemeFeeType.PROPERTY_CONTRACT.getType())) {
                billingPlans.forEach(contractBillingPlan -> {
                    if (contractBillingPlan.getBillingCycle().equals(ContractConstants.BillingCycleEnum.ONE.status)) {
                        RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                        rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
                        rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
                        rentContractFee.setDueDate(rentContractFee.getStartDate().plusDays(contractBillingPlan.getDayOfMonth() - 1));
                        rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                        rentContractFee.setReceivableAmount(contractBillingPlan.getAmountWithTax());
                        rentContractFee.setDueAmount(contractBillingPlan.getAmountWithTax());
                        rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                        EntityUserUtil.setAllDynamic(rentContractFee);
                        rentContractFeeList.add(rentContractFee);
                    } else {
                        Integer year = Period.between(contractBillingPlan.getBillingStartDate(), contractBillingPlan.getBillingEndDate().plusDays(1)).getYears();
                        BigDecimal monthOfYear = new BigDecimal(year * 12);
                        if (Objects.nonNull(contractBillingPlan.getFreeRentDay()) && Constants.ZERO != contractBillingPlan.getFreeRentDay()) {
                            BigDecimal freeRentMonths = new BigDecimal(contractBillingPlan.getFreeRentDay());
                            monthOfYear = monthOfYear.subtract(freeRentMonths);
                        }
                        BigDecimal amountOfMonth = contractBillingPlan.getAmountWithTax().divide(monthOfYear, 10, BigDecimal.ROUND_HALF_UP);
                        BigDecimal nowAmount = BigDecimal.ZERO;
                        for (int i = 0; i < year; i++) {
                            RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                            rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate().plusYears(i));
                            rentContractFee.setEndDate(contractBillingPlan.getBillingStartDate().plusYears(i + 1).minusDays(1));
                            rentContractFee.setDueDate(rentContractFee.getStartDate().plusDays(contractBillingPlan.getDayOfMonth() - 1));
                            rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                            if (i == 0) {
                                if (Objects.nonNull(contractBillingPlan.getFreeRentDay()) && Constants.ZERO != contractBillingPlan.getFreeRentDay()) {
                                    BigDecimal freeRentMonths = new BigDecimal(contractBillingPlan.getFreeRentDay());
                                    rentContractFee.setReceivableAmount(amountOfMonth.multiply(MONTHS_DAY.subtract(freeRentMonths)));
                                } else {
                                    rentContractFee.setReceivableAmount(amountOfMonth.multiply(MONTHS_DAY));
                                }
                            } else if (i == year - 1) {
                                rentContractFee.setReceivableAmount(contractBillingPlan.getAmountWithTax().subtract(nowAmount));
                            } else {
                                rentContractFee.setReceivableAmount(amountOfMonth.multiply(MONTHS_DAY));
                            }
                            rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
                            nowAmount = nowAmount.add(rentContractFee.getDueAmount());
                            rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                            EntityUserUtil.setAllDynamic(rentContractFee);
                            rentContractFeeList.add(rentContractFee);
                        }
                    }
                });
                saveBatch(rentContractFeeList);
            }
        }
    }


    @Override
    public void createApartContractFee(Contract contract) {
        List<ContractBillingPlan> billingPlans = contractBillingPlanService.lambdaQuery().eq(ContractBillingPlan::getContractId, contract.getId()).list();
        if (CollectionUtil.isNotEmpty(billingPlans)) {
            Map<String, List<HouseMessage>> houseMap = contract.getHouseMessages().parallelStream().collect(Collectors.groupingBy(HouseMessage::getHouseId));
            List<RentContractFee> rentContractFeeList = new ArrayList<>();
            billingPlans.forEach(contractBillingPlan -> {
                if (contractBillingPlan.getChargingCycle().equals(RentFeeConstants.chargingCycle)) {
                    BigDecimal days = new BigDecimal(contractBillingPlan.getBillingEndDate().toEpochDay() - contractBillingPlan.getBillingStartDate().toEpochDay() + 1);
                    if (Objects.nonNull(contractBillingPlan.getFreeRentDay()) && Constants.ZERO != contractBillingPlan.getFreeRentDay()) {
                        BigDecimal freeRentDays = new BigDecimal(contractBillingPlan.getFreeRentDay());
                        days = days.subtract(freeRentDays);
                    }
                    BigDecimal dayOfAmount = contractBillingPlan.getAmountWithTax().divide(days, 2, BigDecimal.ROUND_HALF_UP);
                    //一次性计费
                    if (contractBillingPlan.getBillingCycle().equals(ContractConstants.BillingCycleEnum.ONE.status)) {
                        RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                        rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
                        rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
                        rentContractFee.setDueDate(rentContractFee.getStartDate().plusDays(contractBillingPlan.getDayOfMonth() - 1));
                        rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                        rentContractFee.setReceivableAmount(contractBillingPlan.getAmountWithTax());
                        rentContractFee.setDueAmount(contractBillingPlan.getAmountWithTax());
                        rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                        EntityUserUtil.setAllDynamic(rentContractFee);
                        rentContractFeeList.add(rentContractFee);
                        //按月计费
                    } else if (contractBillingPlan.getBillingCycle().equals(ContractConstants.BillingCycleEnum.MONTH.status)) {
                        List<String> monthList = DateUtil.getBetweenMonth(contractBillingPlan.getBillingStartDate().with(TemporalAdjusters.firstDayOfMonth()), contractBillingPlan.getBillingEndDate());
                        if (monthList.size() == RentFeeConstants.ONE_INT) {
                            LocalDate nowMonth = LocalDate.parse(monthList.get(0), DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT));
                            RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                            setRentParam(rentContractFeeList, contractBillingPlan, rentContractFee, dayOfAmount, nowMonth);
                        } else {
                            for (int i = 0; i < monthList.size(); i++) {
                                LocalDate nowMonth = LocalDate.parse(monthList.get(i), DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT));
                                if (i == 0) {
                                    RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                                    rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
                                    rentContractFee.setEndDate(contractBillingPlan.getBillingStartDate().with(TemporalAdjusters.lastDayOfMonth()));
                                    BigDecimal firstDays = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
                                    if (Objects.nonNull(contractBillingPlan.getFreeRentDay()) && Constants.ZERO != contractBillingPlan.getFreeRentDay()) {
                                        BigDecimal freeRentDays = new BigDecimal(contractBillingPlan.getFreeRentDay());
                                        firstDays = firstDays.subtract(freeRentDays);
                                        if (firstDays.compareTo(BigDecimal.valueOf(RentFeeConstants.ZERO)) < RentFeeConstants.ZERO) {
                                            firstDays = BigDecimal.valueOf(RentFeeConstants.ZERO);
                                        }
                                    }
                                    rentContractFee.setReceivableAmount(dayOfAmount.multiply(firstDays).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    LocalDate beginDate = rentContractFee.getStartDate().with(TemporalAdjusters.firstDayOfMonth()).plusDays(contractBillingPlan.getDayOfMonth() - 1);
                                    if (beginDate.isAfter(rentContractFee.getStartDate())) {
                                        rentContractFee.setDueDate(beginDate);
                                    } else {
                                        rentContractFee.setDueDate(rentContractFee.getStartDate());
                                    }
                                    rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                                    rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
                                    rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                                    EntityUserUtil.setAllDynamic(rentContractFee);
                                    rentContractFeeList.add(rentContractFee);
                                } else if (i == monthList.size() - 1) {
                                    RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                                    rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
                                    rentContractFee.setStartDate(rentContractFee.getEndDate().with(TemporalAdjusters.firstDayOfMonth()));
                                    BigDecimal lastDays = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
                                    rentContractFee.setReceivableAmount(dayOfAmount.multiply(lastDays).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    setRentParams(rentContractFeeList, contractBillingPlan, rentContractFee, nowMonth);
                                } else {
                                    RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                                    rentContractFee.setStartDate(nowMonth.with(TemporalAdjusters.firstDayOfMonth()));
                                    rentContractFee.setEndDate(nowMonth.with(TemporalAdjusters.lastDayOfMonth()));
                                    BigDecimal lastDays = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
                                    rentContractFee.setReceivableAmount(dayOfAmount.multiply(lastDays).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    setRentParams(rentContractFeeList, contractBillingPlan, rentContractFee, nowMonth);
                                }
                            }
                        }
                        //按季度计费
                    } else if (contractBillingPlan.getBillingCycle().equals(ContractConstants.BillingCycleEnum.QUARTER.status)) {
                        List<String> seasonList = DateUtil.getSeasonList(contractBillingPlan.getBillingStartDate(), contractBillingPlan.getBillingEndDate());
                        if (seasonList.size() == RentFeeConstants.ONE_INT) {
                            String year = seasonList.get(0).split("-")[0];
                            String season = seasonList.get(0).split("-")[1];
                            String firstDay = getFirstDay(year, season);
                            RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                            LocalDate nowYear = LocalDate.parse(firstDay, DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT));
                            rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
                            rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
                            BigDecimal firstDays = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
                            rentContractFee.setReceivableAmount(dayOfAmount.divide(firstDays, 2, BigDecimal.ROUND_HALF_UP));
                            rentContractFee.setDueDate(rentContractFee.getStartDate().plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1));
                            rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                            rentContractFee.setReceivableAmount(contractBillingPlan.getAmountWithTax());
                            rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
                            rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                            EntityUserUtil.setAllDynamic(rentContractFee);
                            rentContractFeeList.add(rentContractFee);
                        } else {
                            for (int i = 0; i < seasonList.size(); i++) {
                                String year = seasonList.get(i).split("-")[0];
                                String season = seasonList.get(i).split("-")[1];
                                String firstDay = getFirstDay(year, season);
                                LocalDate nowDay = LocalDate.parse(firstDay, DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT));
                                RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                                if (i == 0) {
                                    rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
                                    rentContractFee.setEndDate(DateUtil.getStartOrEndDayOfQuarter(contractBillingPlan.getBillingStartDate(), false));
                                    LocalDate beginDate = nowDay.plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1);
                                    if (beginDate.isAfter(rentContractFee.getStartDate())) {
                                        rentContractFee.setDueDate(beginDate);
                                    } else {
                                        rentContractFee.setDueDate(rentContractFee.getStartDate());
                                    }
                                } else if (i == seasonList.size() - 1) {
                                    rentContractFee.setStartDate(DateUtil.getStartOrEndDayOfQuarter(contractBillingPlan.getBillingEndDate(), true));
                                    rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
                                    rentContractFee.setDueDate(rentContractFee.getStartDate().plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1));
                                } else {
                                    rentContractFee.setStartDate(nowDay.with(TemporalAdjusters.firstDayOfMonth()));
                                    rentContractFee.setEndDate(nowDay.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()));
                                    rentContractFee.setDueDate(rentContractFee.getStartDate().plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1));
                                }
                                rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                                BigDecimal daysTotal = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
                                rentContractFee.setReceivableAmount(dayOfAmount.multiply(daysTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
                                rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
                                rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                                EntityUserUtil.setAllDynamic(rentContractFee);
                                rentContractFeeList.add(rentContractFee);
                            }
                        }
                        //按年计费
                    } else if (contractBillingPlan.getBillingCycle().equals(ContractConstants.BillingCycleEnum.YEAR.status)) {
                        List<String> yearList = DateUtil.getTimePeriodFromTwoTime(contractBillingPlan.getBillingStartDate(), contractBillingPlan.getBillingEndDate(), Constants.TimeTypeEnum.YEAR);
                        if (yearList.size() == RentFeeConstants.ONE_INT) {
                            String yearOf = yearList.get(0) + "-01-01";
                            RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                            LocalDate nowYear = LocalDate.parse(yearOf, DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT));
                            rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
                            rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
                            BigDecimal firstDays = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
                            rentContractFee.setReceivableAmount(dayOfAmount.divide(firstDays, 2, BigDecimal.ROUND_HALF_UP));
                            rentContractFee.setDueDate(rentContractFee.getStartDate().plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1));
                            rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                            rentContractFee.setReceivableAmount(contractBillingPlan.getAmountWithTax());
                            rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
                            rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                            EntityUserUtil.setAllDynamic(rentContractFee);
                            rentContractFeeList.add(rentContractFee);
                        } else {
                            for (int i = 0; i < yearList.size(); i++) {
                                String yearOf = yearList.get(i) + "-01-01";
                                LocalDate nowYear = LocalDate.parse(yearOf, DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT));
                                RentContractFee rentContractFee = convertToRentFee(contract, contractBillingPlan, houseMap);
                                if (i == 0) {
                                    rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
                                    rentContractFee.setEndDate(contractBillingPlan.getBillingStartDate().with(TemporalAdjusters.lastDayOfYear()));
                                    LocalDate beginDate = nowYear.plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1);
                                    if (beginDate.isAfter(rentContractFee.getStartDate())) {
                                        rentContractFee.setDueDate(beginDate);
                                    } else {
                                        rentContractFee.setDueDate(rentContractFee.getStartDate());
                                    }
                                } else if (i == yearList.size() - 1) {
                                    rentContractFee.setStartDate(contractBillingPlan.getBillingEndDate().with(TemporalAdjusters.firstDayOfYear()));
                                    rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
                                    rentContractFee.setDueDate(rentContractFee.getStartDate().with(TemporalAdjusters.firstDayOfYear()).plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1));
                                } else {
                                    rentContractFee.setStartDate(nowYear.with(TemporalAdjusters.firstDayOfYear()));
                                    rentContractFee.setEndDate(nowYear.with(TemporalAdjusters.lastDayOfYear()));
                                    rentContractFee.setDueDate(rentContractFee.getStartDate().with(TemporalAdjusters.firstDayOfYear()).plusMonths(contractBillingPlan.getMonthBy() - 1).plusDays(contractBillingPlan.getDayOfMonth() - 1));
                                }
                                rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
                                BigDecimal daysTotal = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
                                rentContractFee.setReceivableAmount(dayOfAmount.multiply(daysTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
                                rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
                                rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
                                EntityUserUtil.setAllDynamic(rentContractFee);
                                rentContractFeeList.add(rentContractFee);
                            }
                        }
                    }

                } else {

                }
            });
            saveBatch(rentContractFeeList);

        }

    }

    /**
     * 修改应收日期、最晚缴纳日期；批量转已出时使用
     *
     * @param rentContractFeeList 费用数据
     * @param latestPayDate       最晚缴纳日期（批量转已出方法调用时为null）
     * @return
     */
    private List<RentContractBill> getSaveBillData(List<RentContractFee> rentContractFeeList, LocalDate latestPayDate) {
        Set<String> allHouseIds = Sets.newHashSet();
        List<RentContractBill> rentContractBillList = new ArrayList<>();
        rentContractFeeList.forEach(rentContractFee -> {
            rentContractFee.setBillingStatus(RentFeeConstants.RentFeeOutEnum.LOST.status);
            rentContractFee.setDueDate(LocalDate.now());
            if (Objects.nonNull(latestPayDate)) {
                rentContractFee.setLatestPayDate(latestPayDate);
            } else {
                int days = Period.between(rentContractFee.getDueDate(), rentContractFee.getLatestPayDate()).getDays();
                rentContractFee.setLatestPayDate(LocalDate.now().plusDays(days));
            }
            EntityUserUtil.setUpdateBy(rentContractFee);
            //公寓租金才按房间拆分账单
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
        EntityUserUtil.setAll(rentContractBill);
        return rentContractBill;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<RentContractFee> getConditionWrapper(RentContractFeeQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        LambdaQueryWrapper<RentContractFee> queryWrapper = new LambdaQueryWrapper<RentContractFee>();
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), RentContractFee::getParkOrgId, query.getParkOrgId());
        queryWrapper.like(StringUtils.isNotBlank(query.getCompanyName()), RentContractFee::getCompanyName, query.getCompanyName());
        queryWrapper.like(StringUtils.isNotBlank(query.getContractNo()), RentContractFee::getContractNo, query.getContractNo());
        if (query.getDueDateStart() != null && query.getDueDateEnd() != null) {
            queryWrapper.between(RentContractFee::getDueDate, query.getDueDateStart(), query.getDueDateEnd());
        }
        if (query.getLeaseDateStart() != null && query.getLeaseDateEnd() != null) {
            queryWrapper.between(RentContractFee::getLeaseDate, query.getLeaseDateStart(), query.getLeaseDateEnd());
        }
        if (query.getLatestPayDateStart() != null && query.getLatestPayDateEnd() != null) {
            queryWrapper.between(RentContractFee::getLatestPayDate, query.getLatestPayDateStart(), query.getLatestPayDateEnd());
        }
        queryWrapper.eq(RentContractFee::getContractType, query.getContractType());
        //租赁收费模块
        if (ContractConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT.getType() == query.getContractType()) {
            if (query.getQueryType() != null) {
                if (query.getQueryType().equals(RentFeeConstants.ZERO)) {
                    queryWrapper.notLike(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                    if (query.getBillingStatus() != null) {
                        queryWrapper.eq(RentContractFee::getBillingStatus, query.getBillingStatus());
                    }
                } else if (query.getQueryType().equals(RentFeeConstants.ONE_INT)) {
                    queryWrapper.eq(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                    queryWrapper.in(RentContractFee::getFeeType, RentFeeConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT_1.getCalculationValues());
                } else {
                    queryWrapper.eq(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                    queryWrapper.in(RentContractFee::getFeeType, RentFeeConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT_2.getCalculationValues());
                }
            }
        }

        //物业收费模块
        if (ContractConstants.ContractTypeToSchemeFeeType.PROPERTY_CONTRACT.getType() == query.getContractType()) {
            if (query.getQueryType() != null) {
                if (query.getQueryType().equals(RentFeeConstants.ZERO)) {
                    queryWrapper.notLike(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                    if (query.getBillingStatus() != null) {
                        queryWrapper.eq(RentContractFee::getBillingStatus, query.getBillingStatus());
                    }
                } else {
                    queryWrapper.eq(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                }
            }
        }

        //公寓收费模块
        if (ContractConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT.getType() == query.getContractType()) {
            if (query.getQueryType() != null) {
                if (query.getQueryType().equals(RentFeeConstants.ZERO)) {
                    queryWrapper.notLike(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                    if (query.getBillingStatus() != null) {
                        queryWrapper.eq(RentContractFee::getBillingStatus, query.getBillingStatus());
                    }
                } else if (query.getQueryType().equals(RentFeeConstants.ONE_INT)) {
                    queryWrapper.eq(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                    queryWrapper.in(RentContractFee::getFeeType, APARTMENT_CONTRACT_1.getCalculationValues());
                } else {
                    queryWrapper.eq(RentContractFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
                    queryWrapper.in(RentContractFee::getFeeType, RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_2.getCalculationValues());
                }
            }
        }

        queryWrapper.eq(query.getFeeStatus() != null, RentContractFee::getFeeStatus, query.getFeeStatus());
        queryWrapper.eq(query.getCashStatus() != null, RentContractFee::getCashStatus, query.getCashStatus());
        queryWrapper.in(RentContractFee::getParkOrgId, parkOrgIds).orderByDesc(RentContractFee::getDueDate, RentContractFee::getContractNo);
        return queryWrapper;
    }


    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<RentContractFeeDTO> entitiesToDTOs(List<RentContractFee> entities) {
        return MyBeanUtils.copyList(entities, RentContractFeeDTO::new, (s, t) -> {

        });
    }

    private List<RentContractBillRecordDTO> getDeductionRecordList(Set<String> ids) {
        List<RentContractBillRecord> list = rentContractBillRecordService.lambdaQuery()
                .in(RentContractBillRecord::getBillId, ids).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return MyBeanUtils.copyList(list, RentContractBillRecordDTO::new);
    }

    private String getFirstDay(String year, String season) {
        String firstDay;
        if (RentFeeConstants.SeasonEnum.ONE.status == Integer.parseInt(season)) {
            firstDay = year + "-01-01";
        } else if (RentFeeConstants.SeasonEnum.TWO.status == Integer.parseInt(season)) {
            firstDay = year + "-04-01";
        } else if (RentFeeConstants.SeasonEnum.THREE.status == Integer.parseInt(season)) {
            firstDay = year + "-07-01";
        } else {
            firstDay = year + "-10-01";
        }
        return firstDay;
    }

    private void setRentParams(List<RentContractFee> rentContractFeeList, ContractBillingPlan contractBillingPlan, RentContractFee rentContractFee, LocalDate nowDay) {
        rentContractFee.setDueDate(rentContractFee.getStartDate().plusDays(contractBillingPlan.getDayOfMonth() - 1));
        rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
        rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
        rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
        EntityUserUtil.setAllDynamic(rentContractFee);
        rentContractFeeList.add(rentContractFee);
    }

    private void getRentFee(List<RentContractFee> rentContractFeeList, ContractBillingPlan contractBillingPlan, RentContractFee rentContractFee, BigDecimal dayOfAmount, String yearOf) {
        LocalDate nowYear = LocalDate.parse(yearOf, DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT));
        setRentParam(rentContractFeeList, contractBillingPlan, rentContractFee, dayOfAmount, nowYear);
    }

    private void setRentParam(List<RentContractFee> rentContractFeeList, ContractBillingPlan contractBillingPlan, RentContractFee rentContractFee, BigDecimal dayOfAmount, LocalDate nowYear) {
        rentContractFee.setStartDate(contractBillingPlan.getBillingStartDate());
        rentContractFee.setEndDate(contractBillingPlan.getBillingEndDate());
        BigDecimal firstDays = new BigDecimal(rentContractFee.getEndDate().toEpochDay() - rentContractFee.getStartDate().toEpochDay() + 1);
        rentContractFee.setReceivableAmount(dayOfAmount.divide(firstDays, 2, BigDecimal.ROUND_HALF_UP));
        rentContractFee.setDueDate(nowYear.plusDays(contractBillingPlan.getDayOfMonth() - 1));
        rentContractFee.setLatestPayDate(rentContractFee.getDueDate().plusDays(contractBillingPlan.getDaysForDelay()));
        rentContractFee.setReceivableAmount(contractBillingPlan.getAmountWithTax());
        rentContractFee.setDueAmount(rentContractFee.getReceivableAmount());
        rentContractFee.setAccountPeriod(rentContractFee.getDueDate().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)));
        EntityUserUtil.setAllDynamic(rentContractFee);
        rentContractFeeList.add(rentContractFee);
    }

    private RentContractFee convertToRentFee(Contract contract, ContractBillingPlan contractBillingPlan, Map<String, List<HouseMessage>> houseMap) {
        RentContractFee rentContractFee = new RentContractFee();
        rentContractFee.setContractBillingPlanId(contractBillingPlan.getId());
        rentContractFee.setContractId(contract.getId());
        rentContractFee.setContractNo(contract.getNo());
        rentContractFee.setContractType(contract.getType());
        rentContractFee.setCompanyId(contract.getCustomerIdB());
        rentContractFee.setCompanyName(contract.getNameB());
        rentContractFee.setParkOrgId(contract.getParkOrgId());
        rentContractFee.setParkOrgName(contract.getParkOrgName());
        rentContractFee.setFeeType(contractBillingPlan.getChargeItemMessage().getFeeType());
        rentContractFee.setChargeName(contractBillingPlan.getChargeItemMessage().getChargeName());
        rentContractFee.setDataSource(RentFeeConstants.RentDataSourceEnum.CON_CREATE.status);
        rentContractFee.setBillingStatus(RentFeeConstants.RentFeeOutEnum.BINDING.status);
        Set<String> houseIds = houseRelationService.lambdaQuery().eq(HouseRelation::getPkId, contractBillingPlan.getId())
                .eq(HouseRelation::getType, ContractConstants.HouseRelationType.FEE_BILLING.value).list().parallelStream().map(HouseRelation::getHouseId).collect(Collectors.toSet());
        List<HouseMessage> houseMessageList = new ArrayList<>();
        houseIds.forEach(houseId -> {
            List<HouseMessage> planHouses = houseMap.get(houseId);
            houseMessageList.addAll(planHouses);
        });
        AtomicDouble totalArea = new AtomicDouble();
        AtomicDouble coverArea = new AtomicDouble();
        List<String> HouseName = new ArrayList<>();
        houseMessageList.forEach(houseMessage -> {
            HouseName.add(houseMessage.getBuildingName() + "-" + houseMessage.getHouseName());
            totalArea.set(Double.parseDouble(houseMessage.getBuildArea()) + totalArea.get());
            coverArea.set(houseMessage.getCoverArea() + coverArea.get());
        });
        //房源
        rentContractFee.setHouseName(HouseName);
        rentContractFee.setTotalArea(totalArea.get());
        rentContractFee.setHouseMessages(houseMessageList);
        return rentContractFee;
    }

    private void completeFeeInInfo(RentContractFee contractFee, RentFeeInExportDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        String start = formatter.format(contractFee.getStartDate());
        String end = formatter.format(contractFee.getEndDate());
        dto.setCycleDate(new StringBuilder(start).append("-").append(end).toString());
        dto.setHouseName(CollectionUtil.join(contractFee.getHouseName(), "；"));
    }

    private void completeFeeOutInfo(RentContractFee contractFee, RentFeeOutExportDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        String end = formatter.format(contractFee.getEndDate());
        dto.setCycleDate(new StringBuilder(formatter.format(contractFee.getStartDate())).append("-").append(end).toString());
        dto.setHouseName(CollectionUtil.join(contractFee.getHouseName(), ""));
    }

    private void completeFeeCashInfo(RentContractFee contractFee, RentFeeCashExportDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        String start = formatter.format(contractFee.getStartDate());
        dto.setCycleDate(new StringBuilder(start).append("-").append(formatter.format(contractFee.getStartDate())).toString());
        dto.setHouseName(CollectionUtil.join(contractFee.getHouseName(), ""));
    }
}
