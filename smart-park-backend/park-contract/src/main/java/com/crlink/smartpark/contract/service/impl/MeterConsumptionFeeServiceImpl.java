package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.ChargeDetail;
import com.crlink.smartpark.contract.dto.MeterConsumptionBillRecordDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionFeeDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionBill;
import com.crlink.smartpark.contract.entity.MeterConsumptionBillRecord;
import com.crlink.smartpark.contract.entity.MeterConsumptionFee;
import com.crlink.smartpark.contract.entity.MeterConsumptionLateFee;
import com.crlink.smartpark.contract.mapper.MeterConsumptionFeeMapper;
import com.crlink.smartpark.contract.model.MeterConsumptionFeeBasic;
import com.crlink.smartpark.contract.model.MeterConsumptionFeeReqDTO;
import com.crlink.smartpark.contract.model.MeterLateBasic;
import com.crlink.smartpark.contract.model.excel.MeterFeeInExportDTO;
import com.crlink.smartpark.contract.model.excel.MeterFeeOutExportDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionFeeQuery;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionFeeVO;
import com.crlink.smartpark.contract.service.*;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.Consumption;
import com.crlink.smartpark.device.dto.MeterConsumptionDTO;
import com.crlink.smartpark.device.feign.MeterConsumptionProvider;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.feign.FeeProvider;
import com.crlink.smartpark.system.feign.ApartmentPersonHouseRelationProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_CODE_PREFIX;


/**
 * 水电滞纳金费用 服务实现类
 * */
@Service
public class MeterConsumptionFeeServiceImpl extends ServiceImpl<MeterConsumptionFeeMapper, MeterConsumptionFee> implements MeterConsumptionFeeService {


    @Autowired
    private MeterConsumptionBillService meterConsumptionBillService;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private ApartmentPersonHouseRelationProvider personHouseRelationProvider;

    @Autowired
    private MeterConsumptionBillRecordService meterConsumptionBillRecordService;

    @Autowired
    private FeeProvider feeProvider;

    @Autowired
    private MeterConsumptionProvider meterConsumptionProvider;

    @Autowired
    private MeterConsumptionLateFeeService meterConsumptionLateFeeService;

    @Autowired
    private ContractBillingPlanService contractBillingPlanService;


    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionFee, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(MeterConsumptionFee::getId, sValue)
                .in(MeterConsumptionFee::getId, ids).list().stream()
                .collect(HashMap::new, (map, entity) -> map.put(entity.getId(), sValue.apply(entity)), HashMap::putAll);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyDate(MeterConsumptionFeeReqDTO reqDTO) {
        LocalDate dueDate = reqDTO.getDueDate();
        LocalDate now = LocalDate.now();
        if (dueDate.isBefore(now)) {
            throw new ResolinkRuntimeException("300011");
        }
        if (dueDate.isAfter(reqDTO.getLatestPayDate())) {
            throw new ResolinkRuntimeException("300012");
        }
        List<MeterConsumptionFee> meterConsumptionFeeList = this.listByIds(reqDTO.getIds());
        if (CollectionUtil.isEmpty(meterConsumptionFeeList)) {
            return false;
        }
        if (dueDate.isEqual(now)) {
            List<MeterConsumptionBill> meterConsumptionBillList = getSaveBillData(meterConsumptionFeeList, reqDTO.getLatestPayDate());
            meterConsumptionBillService.saveBatch(meterConsumptionBillList);
        } else {
            meterConsumptionFeeList.forEach(meterConsumptionFee -> {
                meterConsumptionFee.setDueDate(dueDate);
                meterConsumptionFee.setLatestPayDate(reqDTO.getLatestPayDate());
                EntityUserUtil.setUpdateBy(meterConsumptionFee);
            });
        }
        return this.updateBatchById(meterConsumptionFeeList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transform(MeterConsumptionFeeReqDTO reqDTO) {
        List<MeterConsumptionFee> meterConsumptionFeeList = this.listByIds(reqDTO.getIds());
        if (CollectionUtil.isEmpty(meterConsumptionFeeList)) {
            return false;
        }
        List<MeterConsumptionBill> meterConsumptionBillList = getSaveBillData(meterConsumptionFeeList, null);
        meterConsumptionBillService.saveBatch(meterConsumptionBillList);
        return updateBatchById(meterConsumptionFeeList);
    }

    @Override
    @Id2objHandle
    public MeterConsumptionFeeVO getMeterConsumptionFee(String id) {
        MeterConsumptionFee entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeterConsumptionFeeDTO meterConsumptionFeeDTO = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
        MeterConsumptionFeeVO meterConsumptionFeeVO = new MeterConsumptionFeeVO();
        List<MeterConsumptionBill> meterConsumptionBillList = meterConsumptionBillService.lambdaQuery().select(MeterConsumptionBill::getId).eq(MeterConsumptionBill::getMeterConsumptionFeeId, entity.getId()).list();
        if (CollectionUtil.isEmpty(meterConsumptionBillList)) {
            meterConsumptionFeeVO.setDeductionRecordList(null);
        } else {
            Set<String> billIds = meterConsumptionBillList.parallelStream().map(MeterConsumptionBill::getId).collect(Collectors.toSet());
            meterConsumptionFeeVO.setDeductionRecordList(getDeductionRecordList(billIds));
        }
        meterConsumptionFeeVO.setMeterConsumptionFeeDTO(meterConsumptionFeeDTO);
        return meterConsumptionFeeVO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeterConsumptionFeeDTO> listMeterConsumptionFeePage(MeterConsumptionFeeQuery query) {
        query.startPage();
        List<MeterConsumptionFee> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<MeterConsumptionFeeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void exportFee(MeterConsumptionFeeQuery query) {
        List<MeterConsumptionFee> list = this.list(getConditionWrapper(query));
        List<MeterConsumptionFeeDTO> result = entitiesToDTOs(list);
        if (RentFeeConstants.ZERO.equals(query.getQueryType())) {
            ExcelExportUtils.export(() -> entitiesToDTOs(list),
                    sourceList -> MyBeanUtils.copyList(result, MeterFeeInExportDTO::new), RentFeeConstants.RENT_IN_EXPORT_FILENAME, MeterFeeInExportDTO.class);

        } else if (RentFeeConstants.ONE_INT.equals(query.getQueryType())) {
            ExcelExportUtils.export(() -> this.list(getConditionWrapper(query)),
                    sourceList -> MyBeanUtils.copyList(result, MeterFeeOutExportDTO::new, (s, t) -> completeFeeOutInfo(s, t)),
                    RentFeeConstants.RENT_OUT_EXPORT_FILENAME, MeterFeeOutExportDTO.class);
        }
    }

    @Override
    public void create(String month) {
        String settledMonth = month;
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
                                System.out.println("水电用量记录==============:" + consumption);
                                if (Objects.equals(RentFeeConstants.DeviceType.WATER_METER.type, consumption.getDeviceType())) {
                                    System.out.println("水用量记录==============:" + consumption);
                                    meterConsumptionFee.setChargeName(RentFeeConstants.WATER_CHARGE_NAMES);
                                    setMeterFeeCharge(meterConsumptionFeeList, meterConsumptionFee, waterMeter, consumption, chargeDetailList, RentFeeConstants.DeviceType.WATER_METER.type);
                                } else if (Objects.equals(RentFeeConstants.DeviceType.ELECTRICITY_METER.type, consumption.getDeviceType())) {
                                    System.out.println("电用量记录==============:" + consumption);
                                    meterConsumptionFee.setChargeName(RentFeeConstants.POWER_CHARGE_NAMES);
                                    setMeterFeeCharge(meterConsumptionFeeList, meterConsumptionFee, eleMeter, consumption, chargeDetailList, RentFeeConstants.DeviceType.ELECTRICITY_METER.type);
                                }
                            });
                            System.out.println("水电用量记录集合==============:" + meterConsumptionFeeList);
                            this.saveBatch(meterConsumptionFeeList);
                        }
                    }
                });

            }
        }
    }

    @Override
    public void createLate() {
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
        System.out.println("计费方案==============:" + billingSchemeDTOMap);
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

    private void completeFeeOutInfo(MeterConsumptionFeeDTO contractFee, MeterFeeOutExportDTO dto) {
        dto.setHouseName(CollectionUtil.join(contractFee.getHouseName(), ""));
    }

    private List<MeterConsumptionBillRecordDTO> getDeductionRecordList(Set<String> ids) {
        List<MeterConsumptionBillRecord> list = meterConsumptionBillRecordService.lambdaQuery()
                .in(MeterConsumptionBillRecord::getBillId, ids).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return MyBeanUtils.copyList(list, MeterConsumptionBillRecordDTO::new);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeterConsumptionFee> getConditionWrapper(MeterConsumptionFeeQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        LambdaQueryWrapper<MeterConsumptionFee> queryWrapper = new LambdaQueryWrapper<MeterConsumptionFee>();
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeterConsumptionFee::getParkOrgId, query.getParkOrgId());
        queryWrapper.like(StringUtils.isNotBlank(query.getCompanyName()), MeterConsumptionFee::getCompanyName, query.getCompanyName());
        queryWrapper.like(StringUtils.isNotBlank(query.getSettledMonth()), MeterConsumptionFee::getSettledMonth, query.getSettledMonth());
        queryWrapper.eq(query.getFeeStatus() != null, MeterConsumptionFee::getFeeStatus, query.getFeeStatus());
        queryWrapper.eq(MeterConsumptionFee::getContractType, query.getContractType());
        if (query.getQueryType() != null) {
            if (query.getQueryType().equals(RentFeeConstants.ZERO)) {
                queryWrapper.notLike(MeterConsumptionFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
            } else if (query.getQueryType().equals(RentFeeConstants.ONE_INT)) {
                queryWrapper.eq(MeterConsumptionFee::getBillingStatus, RentFeeConstants.RentFeeOutEnum.LOST.status);
            }
        }
        queryWrapper.eq(query.getFeeStatus() != null, MeterConsumptionFee::getFeeStatus, query.getFeeStatus());
        queryWrapper.in(MeterConsumptionFee::getParkOrgId, parkOrgIds).orderByDesc(MeterConsumptionFee::getDueDate, MeterConsumptionFee::getCompanyName);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeterConsumptionFeeDTO> entitiesToDTOs(List<MeterConsumptionFee> entities) {
        return MyBeanUtils.copyList(entities, MeterConsumptionFeeDTO::new, (s, t) -> {

        });
    }

    /**
     * 修改应收日期、最晚缴纳日期；批量转已出时使用
     *
     * @param meterConsumptionFeeList 费用数据
     * @param latestPayDate           最晚缴纳日期（批量转已出方法调用时为null）
     * @return
     */
    private List<MeterConsumptionBill> getSaveBillData(List<MeterConsumptionFee> meterConsumptionFeeList, LocalDate latestPayDate) {
        Set<String> allHouseIds = Sets.newHashSet();
        List<MeterConsumptionBill> meterConsumptionBillList = new ArrayList<>();
        meterConsumptionFeeList.forEach(meterConsumptionFee -> {
            meterConsumptionFee.setBillingStatus(RentFeeConstants.RentFeeOutEnum.LOST.status);
            meterConsumptionFee.setDueDate(LocalDate.now());
            if (Objects.nonNull(latestPayDate)) {
                meterConsumptionFee.setLatestPayDate(latestPayDate);
            } else {
                int days = Period.between(meterConsumptionFee.getDueDate(), meterConsumptionFee.getLatestPayDate()).getDays();
                meterConsumptionFee.setLatestPayDate(LocalDate.now().plusDays(days));
            }
            EntityUserUtil.setUpdateBy(meterConsumptionFee);
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
        EntityUserUtil.setAll(meterConsumptionBill);
        return meterConsumptionBill;
    }
}
