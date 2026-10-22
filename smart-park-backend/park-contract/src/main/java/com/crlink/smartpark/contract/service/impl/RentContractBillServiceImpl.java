package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.*;
import com.crlink.smartpark.contract.entity.*;
import com.crlink.smartpark.contract.mapper.RentContractBillMapper;
import com.crlink.smartpark.contract.model.ChargeStaReqDTO;
import com.crlink.smartpark.contract.model.ChargeStaTrendDTO;
import com.crlink.smartpark.contract.model.ChargeTopStatisticDTO;
import com.crlink.smartpark.contract.model.excel.RentContractBillExcelExportDTO;
import com.crlink.smartpark.contract.model.query.CompanyPortBillQuery;
import com.crlink.smartpark.contract.model.query.RentContractBillQuery;
import com.crlink.smartpark.contract.model.request.*;
import com.crlink.smartpark.contract.model.vo.*;
import com.crlink.smartpark.contract.service.*;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.dto.MeetingReserveDTO;
import com.crlink.smartpark.customer.feign.MeetingReserveProvider;
import com.crlink.smartpark.system.dto.house.HouseQueryDTO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_YEAR_MONTH_FORMAT;
import static com.crlink.smartpark.common.constants.DictConstant.ContractState.IN_EFFECT;
import static com.crlink.smartpark.common.constants.DictConstant.ContractState.TO_BE_EFFECTIVE;
import static com.crlink.smartpark.common.constants.DictConstant.ContractType.APARTMENT_CONTRACT;
import static com.crlink.smartpark.common.constants.enums.OperationScreenConstants.BillType.*;
import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.*;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_CODE_PREFIX;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_EXPORT_FILENAME;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeBillEnum.RECEIVED;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeBillEnum.UNRECEIVED;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeReturnOrderEnum.CAN_RETURN;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.PAY_ORDER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.RETURN_ORDER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentLateStatusEnum.PAID_UP;
import static com.crlink.smartpark.system.enums.Constant.HousePropertyEnum.RENT;
import static com.crlink.smartpark.system.enums.Constant.HousePropertyEnum.SELL;


/**
 * 租赁合同账单 服务实现类
 * */
@Service
public class RentContractBillServiceImpl extends ServiceImpl<RentContractBillMapper, RentContractBill> implements RentContractBillService {

    @Autowired
    private RentLateFeeService rentLateFeeService;

    @Autowired
    private RentContractBillRecordService rentContractBillRecordService;

    @Autowired
    private RentContractFeeService rentContractFeeService;

    @Autowired
    private RentContractTradeService rentContractTradeService;

    @Autowired
    NumberProvider numberProvider;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private FileProvider fileProvider;

    @Autowired
    private CompanyProvider companyProvider;

    @Autowired
    private MeterConsumptionBillService meterConsumptionBillService;

    @Autowired
    private MeetingReserveProvider meetingReserveProvider;

    @Autowired
    private HouseProvider houseProvider;

    @Autowired
    private ContractService contractService;

    @Autowired
    private MeterConsumptionLeaveService meterConsumptionLeaveService;

    @Autowired
    private MeterConsumptionLateFeeService meterConsumptionLateFeeService;

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<RentContractBill, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(RentContractBill::getId, sValue)
                .in(RentContractBill::getId, ids).list().stream()
                .collect(HashMap::new, (map, meter) -> map.put(meter.getId(), sValue.apply(meter)), HashMap::putAll);
        return result;
    }

    /**
     * 修改账单的所在账期
     */
    @Override
    public Boolean updateRentContractBill(RentBillUpdateReqDTO reqDTO) {
        Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return this.lambdaUpdate()
                .eq(RentContractBill::getId, reqDTO.getId())
                .set(RentContractBill::getAccountPeriod, reqDTO.getAccountPeriod())
                .set(RentContractBill::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .update();
    }

    /**
     * 减免费用
     * 1、账单表中更改待收金额、减免费用
     * 2、费用表中更改待收金额、减免费用
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deductionFee(RentBillDeductionFeeDTO dto) {
        RentContractBill bill = Optional.ofNullable(getById(dto.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (!UNRECEIVED.status.equals(bill.getStatus())) {
            throw new ResolinkRuntimeException("300014");
        }
        BigDecimal deductionAmount = dto.getDeductionAmount();
        //减免金额应小于等于待收金额
        if (deductionAmount.compareTo(bill.getDueAmount()) > 0) {
            throw new ResolinkRuntimeException("300009");
        }
        //当状态为“未收”时，且当前日期小于等于“最晚缴纳日期”，支持“减免”操作
        if (LocalDate.now().isAfter(bill.getLatestPayDate())) {
            throw new ResolinkRuntimeException("300014");
        }
        LambdaUpdateChainWrapper<RentContractBill> updateWrapper = this.lambdaUpdate();
        //当减免金额=待收金额，且无滞纳金，账单状态由“未收”更新为“已收”。
        //当减免金额=待收金额，有滞纳金，账单状态不更新，为“未收”。
        BigDecimal dueAmount = bill.getDueAmount().subtract(deductionAmount);
        if (dueAmount.compareTo(BigDecimal.ZERO) == 0) {
            RentLateFee lateFee = rentLateFeeService.lambdaQuery().eq(RentLateFee::getRentBillId, dto.getId()).one();
            if (Objects.isNull(lateFee)) {
                updateWrapper.set(RentContractBill::getStatus, RECEIVED.status)
                        .set(RentContractBill::getCheckoutDate, LocalDate.now())
                        .set(RentContractBill::getCheckoutType, 1);
            }
        }
        //更新账单表
        String updateByUserId = SystemUtil.getInstance().getCurrentUserId();
        updateWrapper.eq(RentContractBill::getId, dto.getId())
                //待收金额
                .set(RentContractBill::getDueAmount, dueAmount)
                //减免费用
                .set(RentContractBill::getDeductionAmount, deductionAmount.add(bill.getDeductionAmount()))
                .set(RentContractBill::getUpdateBy, updateByUserId)
                .update();
        //更新费用表
        List<RentContractFee> rentContractFeeUpdateList =
                getRentContractFeeUpdateList(Sets.newHashSet(bill.getRentContractFeeId()), deductionAmount, updateByUserId);
        rentContractFeeService.updateBatchById(rentContractFeeUpdateList);
        //更新账单减免记录表
        RentContractBillRecord record = new RentContractBillRecord();
        record.setBillId(dto.getId());
        record.setContractType(bill.getContractType());
        record.setDeductionAmount(deductionAmount);
        record.setFileList(setFiles(dto.getFile()));
        record.setRemark(dto.getRemark());
        EntityUserUtil.setAll(record);
        return rentContractBillRecordService.save(record);
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public RentContractBillVO getRentContractBillById(String id) {
        RentContractBill entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        RentContractBillVO result = MyBeanUtils.copy(entity, RentContractBillVO::new);
        result.setFeeDetailList(getFeeDetailList(entity));
        result.setDeductionRecordList(getDeductionRecordList(entity));
        result.setLateFeeDetailList(getLateFeeDetailList(entity));
        result.setCheckoutRecordList(getCheckoutRecordList(entity));
        return result;
    }


    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<RentContractBillListVO> listRentContractBillPage(RentContractBillQuery query) {
        LambdaQueryWrapper<RentContractBill> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<RentContractBill> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<RentContractBillListVO> result = convert(list, query.getContractType());
        return query.createPageInfo(result);
    }


    /**
     * 企业端账单查询
     *
     * @param query
     * @return
     */
    @Override
    @Id2objHandle
    public PageInfo<CompanyPortBillListVO> listCompanyPortBillPage(CompanyPortBillQuery query) {
        LambdaQueryWrapper<RentContractBill> wrapper = getConditionWrapperForCompanyPort(query);
        query.startPage();
        List<RentContractBill> list = list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<CompanyPortBillListVO> result = MyBeanUtils.copyList(list, CompanyPortBillListVO::new);
        return query.createPageInfo(result);
    }

    /**
     * 企业端上传凭证
     *
     * @param reqDTO
     * @return
     */
    @Override
    public Boolean uploadFiles(BillUploadFileReqDTO reqDTO) {
        Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return this.lambdaUpdate()
                .eq(RentContractBill::getId, reqDTO.getId())
                .set(RentContractBill::getPayType, reqDTO.getPayType())
                .set(RentContractBill::getCompanyFiles, reqDTO.getCompanyFiles())
                .set(RentContractBill::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .update();
    }

    @Override
    public void export(RentContractBillQuery query) {
        ExcelExportUtils.export(() -> this.list(getConditionWrapper(query)),
                sourceList -> MyBeanUtils.copyList(convert(sourceList, query.getContractType()), RentContractBillExcelExportDTO::new,
                        (s, t) -> t.setFeeDate(s.getStartDate().toString() + "-" + s.getEndDate().toString())),
                RENT_BILL_EXPORT_FILENAME, RentContractBillExcelExportDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkout(RentBillCheckoutReqDTO reqDTO) {
        Set<String> ids = reqDTO.getIds();
        Map<String, BigDecimal> dueAmountMap = id2ValueMap(ids, RentContractBill::getDueAmount);
        List<RentContractBill> list = new LinkedList<>();
        String updateByUserId = SystemUtil.getInstance().getCurrentUserId();
        Map<String, RentLateFee> lateFeeMaps = getLateFeeMapByBillIds(ids);
        ids.forEach(t -> {
            RentContractBill bill = new RentContractBill();
            bill.setId(t);
            bill.setCheckoutDate(reqDTO.getCheckoutDate());
            if (reqDTO.getFromLeave()) {
                //离场结算
                bill.setCheckoutType(2);
            } else {
                //正常收款
                bill.setCheckoutType(1);
            }
            bill.setPayType(reqDTO.getPayType());
            bill.setRemark(reqDTO.getRemark());
            //状态改为已收
            bill.setStatus(RECEIVED.status);
            bill.setDueAmount(BigDecimal.ZERO);
            bill.setReceivedAmount(dueAmountMap.get(t));
            if (CollectionUtil.isNotEmpty(lateFeeMaps)) {
                RentLateFee rentLateFee = lateFeeMaps.get(t);
                if (Objects.nonNull(rentLateFee)) {
                    bill.setReceivedAmount(bill.getReceivedAmount().add(rentLateFee.getReceiveFine()));
                }
            }
            bill.setUpdateBy(updateByUserId);
            list.add(bill);
        });
        //更新账单表
        this.updateBatchById(list);
        //更新滞纳金表
        List<RentLateFee> lateFeeUpdateList = getLateFeeUpdateList(lateFeeMaps, updateByUserId);
        if (CollectionUtils.isNotEmpty(lateFeeUpdateList)) {
            rentLateFeeService.updateBatchById(lateFeeUpdateList);
        }
        //交易明细表新增数据
        rentContractTradeService.saveBatch(getSaveTradeList(reqDTO));
        //更新费用表
        Set<String> feeIds = id2ValueMap(ids, RentContractBill::getRentContractFeeId)
                .values().parallelStream().collect(Collectors.toSet());
        List<RentContractFee> rentContractFeeUpdateList =
                getRentContractFeeUpdateList(feeIds, null, updateByUserId);
        return rentContractFeeService.updateBatchById(rentContractFeeUpdateList);
    }

    /**
     * 收款操作前获取滞纳金明细及实收总金额，非批量
     *
     * @param id
     * @return
     */
    @Override
    public LateFeeDetail findLateFeeByBillId(String id) {
        LateFeeDetail result = new LateFeeDetail();
        BigDecimal dueAmount = id2ValueMap(Sets.newHashSet(id), RentContractBill::getDueAmount).get(id);
        RentLateFee rentLateFee = rentLateFeeService.lambdaQuery().select(
                RentLateFee::getId, RentLateFee::getDueAmount, RentLateFee::getDueDays,
                RentLateFee::getReceivableFine, RentLateFee::getReceiveFine,
                RentLateFee::getOverdueFine, RentLateFee::getChargeName)
                .eq(RentLateFee::getRentBillId, id).one();
        if (Objects.nonNull(rentLateFee)) {
            result = MyBeanUtils.copy(rentLateFee, LateFeeDetail::new);
            result.setTotalAmount(dueAmount.add(rentLateFee.getDueAmount()));
            return result;
        }
        return result.setTotalAmount(dueAmount);
    }

    /**
     * 批量操作：根据账单id获取滞纳金
     *
     * @param ids
     * @return
     */
    @Override
    public LateFeeDetail findLateFeeByBillIds(String[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            throw new ResolinkRuntimeException("10007");
        }
        LateFeeDetail result = new LateFeeDetail();
        Set<String> billIds = Arrays.stream(ids).collect(Collectors.toSet());
        //查询代收金额
        BigDecimal dueAmount = this.lambdaQuery().select(RentContractBill::getId, RentContractBill::getDueAmount)
                .in(RentContractBill::getId, billIds).list()
                .stream().map(RentContractBill::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.setDueAmount(dueAmount);
        //查询滞纳金
        List<RentLateFee> rentLateFeeList = rentLateFeeService.lambdaQuery().select(
                RentLateFee::getId, RentLateFee::getReceiveFine)
                .in(RentLateFee::getRentBillId, billIds).list();
        if (CollectionUtils.isEmpty(rentLateFeeList)) {
            //无滞纳金
            result.setReceiveFine(BigDecimal.ZERO);
        } else {
            BigDecimal receiveFine = rentLateFeeList.stream()
                    .map(RentLateFee::getReceiveFine).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.setReceiveFine(receiveFine);
        }
        result.setTotalAmount(dueAmount.add(result.getReceiveFine()));
        return result;
    }

    @Override
    public BillForScreen getBillForScreen(String parkOrgId) {
        LocalDate thisYear = LocalDate.now();
        String year = thisYear.format(DateTimeFormatter.ofPattern(Constants.YEAR_FORMAT));
        //1、查询出所有的已收租赁账单数据
        List<RentContractBill> rentBillList = getRentBillList(parkOrgId, null, null, RECEIVED.status);
        BigDecimal rentTotalAmount = rentBillList.stream().map(RentContractBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentThisYearAmount = rentBillList.stream()
                .filter(t -> t.getAccountPeriod().startsWith(year))
                .map(RentContractBill::getReceivedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //2、查询出所有的已收水电费用账单数据
        List<MeterConsumptionBill> meterBillList = getMeterBillList(parkOrgId, null, null, RECEIVED.status);
        BigDecimal meterTotalAmount = meterBillList.stream().map(MeterConsumptionBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal meterThisYearAmount = meterBillList.stream()
                .filter(t -> t.getAccountPeriod().startsWith(year))
                .map(MeterConsumptionBill::getReceivedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //3、查询出所有的场地预定账单数据
        List<MeetingReserveDTO> meetingReserveBillList = meetingReserveProvider.getMeetingReserveBillList(parkOrgId, false);
        BigDecimal meetingTotalAmount = meetingReserveBillList.stream().map(MeetingReserveDTO::getReviewAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal meetingThisYearAmount = meetingReserveBillList.stream()
                .filter(t -> Objects.equals(thisYear.getYear(), t.getStateChangeTime().getYear()))
                .map(MeetingReserveDTO::getReviewAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //转换为万元、保留2位小数、四舍五入
        BigDecimal totalAmount = (rentTotalAmount.add(meterTotalAmount).add(meetingTotalAmount))
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal thisYearAmount = (rentThisYearAmount.add(meterThisYearAmount).add(meetingThisYearAmount))
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        BillForScreen result = new BillForScreen(totalAmount, thisYearAmount);
        //柱状图
        result.setBillListForScreen(getBillListForScreen(parkOrgId));
        return result;
    }

    /**
     * 1、累计收费：取值于累计的租赁费、物业费、公寓租金、企业水电费、公寓水电、场地预定的账单的已收金额之和。
     * 2、当年收费：取值于当年租赁费、物业费、公寓租金、企业水电费、公寓水电、场地预定的账单的已收金额之和。
     * 3、按年（近12个月）维度的租赁费、物业费、公寓租金、企业水电费、公寓水电费、场地预定费用情况。
     * 4、场地预定以收费时间（取审核时间）统计，其余收费以账期统计；
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public List<BillListForScreen> getBillListForScreen(String parkOrgId) {
        List<BillListForScreen> result = getInitializationData();
        List<RentContractBill> rentBillList = getRentBillList(parkOrgId, null, getNearTwelveMonthList(), RECEIVED.status);
        Map<String, Map<Integer, List<RentContractBill>>> rentBillMap = rentBillList.stream()
                .collect(Collectors.groupingBy(RentContractBill::getAccountPeriod,
                        Collectors.groupingBy(RentContractBill::getContractType)));
        List<MeterConsumptionBill> meterBillList = getMeterBillList(parkOrgId, null, getNearTwelveMonthList(), RECEIVED.status);
        Map<String, Map<Integer, List<MeterConsumptionBill>>> meterBillMap = meterBillList.stream()
                .collect(Collectors.groupingBy(MeterConsumptionBill::getAccountPeriod,
                        Collectors.groupingBy(MeterConsumptionBill::getContractType)));
        List<MeetingReserveDTO> meetingReserveBillList = meetingReserveProvider.getMeetingReserveBillList(parkOrgId, true);
        //类型 1租赁费、2公寓水电费、3场地预定费、4企业水电费、5物业费、6公寓租金
        result.forEach(t -> {
            if (rentBillMap.containsKey(t.getMonth())) {
                Map<Integer, List<RentContractBill>> rentListMap = rentBillMap.get(t.getMonth());
                if (rentListMap.containsKey(RENTAL_CONTRACT_1.getType())) {
                    //租赁合同账单
                    getAmountDataByRent(t, rentListMap.get(RENTAL_CONTRACT_1.getType()), APARTMENT_CONTRACT_1.getType());
                } else if (rentListMap.containsKey(PROPERTY_CONTRACT.getType())) {
                    //物业合同账单
                    getAmountDataByRent(t, rentListMap.get(PROPERTY_CONTRACT.getType()), PROPERTY_CONTRACT.getType());
                } else if (rentListMap.containsKey(APARTMENT_CONTRACT_1.getType())) {
                    //公寓合同账单
                    getAmountDataByRent(t, rentListMap.get(APARTMENT_CONTRACT_1.getType()), APARTMENT_CONTRACT_1.getType());
                }
            }
            if (meterBillMap.containsKey(t.getMonth())) {
                Map<Integer, List<MeterConsumptionBill>> meterListMap = meterBillMap.get(t.getMonth());
                if (meterListMap.containsKey(RENTAL_CONTRACT_1.getType())) {
                    //企业水电费账单
                    getAmountDataByMeter(t, meterListMap.get(RENTAL_CONTRACT_1.getType()), RENTAL_CONTRACT_1.getType());
                } else if (meterListMap.containsKey(APARTMENT_CONTRACT_1.getType())) {
                    //公寓水电费账单
                    getAmountDataByMeter(t, meterListMap.get(APARTMENT_CONTRACT_1.getType()), APARTMENT_CONTRACT_1.getType());
                }
            }
            if (CollectionUtil.isNotEmpty(meetingReserveBillList)) {
                getAmountDataByMeeting(t, meetingReserveBillList);
            }
        });
        return result;
    }

    /**
     * 房间总数：取值于房屋功能为“公寓—单间、公寓—标间、公寓—套间”且房屋属性为”租赁、销售“的房间总数；
     * 已租数：取值于“待生效、生效中”公寓合同关联的房间总数，需按房间去重。
     * 待租数：房间总数-已租数。
     * 已租数、待租数的时间维度按当前时间统计；
     * -水电费收入:
     * 近三十天：公寓水电费账单“实收总金额”之和。
     * 累计：公寓水电费“实收总金额”累计之和。
     * -租金收入
     * 近三十天：公寓费用账单“实收总金额”之和。
     * 累计：公寓费用账单“实收总金额”累计之和。
     * 收入按近30天的收款日期统计金额
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public ApartmentDataForScreen getApartmentDataForScreen(String parkOrgId) {
        ApartmentDataForScreen result = new ApartmentDataForScreen();
        Map<String, ApartmentBillForScreen> amountData = new HashMap<>(2);
        ApartmentBillForScreen billData = new ApartmentBillForScreen(BigDecimal.ZERO, BigDecimal.ZERO);
        amountData.put("METER", billData);
        amountData.put("RENT", billData);
        Set<String> allHouseIds = getAllHouseIds(parkOrgId);
        Set<String> allRentedHouseIds = getAllRentedHouseIds(parkOrgId);
        result.setTotalHouseCount(allHouseIds.size());
        result.setRentedHouseCount(allRentedHouseIds.size());
        result.setRentingHouseCount(result.getTotalHouseCount() - result.getRentedHouseCount());
        getMeterAndRentBillData(amountData, parkOrgId);
        result.setAmountData(amountData);
        return result;
    }

    /**
     * 收费统计-缴费趋势
     * 月度缴费趋势：应收、已收、待收；默认展示近12个月的全部收费类型的趋势，取值于租赁费、物业费、公寓租金、企业水电费、公寓水电的应收金额之和、已收金额之和、待收金额之和。
     * 支持通过费用类型、时间范围检索，
     * 费用类型包括租赁费、物业费、公寓租金、企业水电费、公寓水电费；
     * 时间范围样式为年-月（2022-05），支持多选，
     * 最多支持12个月数据的展示
     * 统计范围需要增加水电费的离场结算费用。
     *
     * @param reqDTO
     * @return
     */
    @Override
    public List<PayTrendVO> paymentTrend(ChargeStaTrendDTO reqDTO) {
        List<String> monthList = DateUtil.getBetweenMonthStr(reqDTO.getStartMonth(), reqDTO.getEndMonth());
        List<PayTrendVO> result = new LinkedList<>();
        List<MeterConsumptionBill> meterBillList = new LinkedList<>();
        List<RentContractBill> rentBillList = new LinkedList<>();
        List<MeterConsumptionLeave> meterLeaveList = new LinkedList<>();
        if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.COMPANY_METER_BILL.type)) {
            meterBillList = getMeterBillList(reqDTO.getParkOrgId(), RENTAL_CONTRACT_1.getType(), monthList, null);
            meterLeaveList = getMeterLeaveList(reqDTO, RENTAL_CONTRACT_1.getType());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.APARTMENT_METER_BILL.type)) {
            meterBillList = getMeterBillList(reqDTO.getParkOrgId(), APARTMENT_CONTRACT_1.getType(), monthList, null);
            meterLeaveList = getMeterLeaveList(reqDTO, APARTMENT_CONTRACT_1.getType());
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.RENTAL_BILL.type)) {
            rentBillList = getRentBillList(reqDTO.getParkOrgId(), RENTAL_CONTRACT_1.getType(), monthList, null);
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.PROPERTY_BILL.type)) {
            rentBillList = getRentBillList(reqDTO.getParkOrgId(), PROPERTY_CONTRACT.getType(), monthList, null);
        } else if (reqDTO.getChargeType().equals(RentFeeConstants.BillType.APARTMENT_BILL.type)) {
            rentBillList = getRentBillList(reqDTO.getParkOrgId(), APARTMENT_CONTRACT_1.getType(), monthList, null);
        } else {
            meterBillList = getMeterBillList(reqDTO.getParkOrgId(), null, monthList, null);
            rentBillList = getRentBillList(reqDTO.getParkOrgId(), null, monthList, null);
            meterLeaveList = getMeterLeaveList(reqDTO, null);
        }
        List<MeterConsumptionBill> finalMeterBillList = meterBillList;
        List<RentContractBill> finalRentBillList = rentBillList;
        List<MeterConsumptionLeave> finalMeterLeaveList = meterLeaveList;
        monthList.forEach(month -> {
            PayTrendVO vo = new PayTrendVO();
            vo.setMonth(month);
            finalMeterBillList.forEach(meterBill -> {
                if (month.equals(meterBill.getAccountPeriod())) {
                    vo.setReceivableAmount(meterBill.getReceivableAmount());
                    vo.setReceivedAmount(meterBill.getReceivedAmount());
                    vo.setDueAmount(meterBill.getDueAmount());
                }
            });
            finalRentBillList.forEach(rentBill -> {
                if (month.equals(rentBill.getAccountPeriod())) {
                    vo.setReceivableAmount(vo.getReceivableAmount().add(rentBill.getReceivableAmount()));
                    vo.setReceivedAmount(vo.getReceivedAmount().add(rentBill.getReceivedAmount()));
                    vo.setDueAmount(vo.getDueAmount().add(rentBill.getDueAmount()));
                }
            });
            finalMeterLeaveList.forEach(leaveList -> {
                String leaveDateStr = leaveList.getLeaveDate().toString();
                if (leaveDateStr.startsWith(month)) {
                    vo.setReceivedAmount(vo.getReceivedAmount().add(leaveList.getTotalAmount()));
                }
            });
            vo.setReceivedAmount(vo.getReceivedAmount().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN));
            vo.setReceivableAmount(vo.getReceivableAmount().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN));
            vo.setDueAmount(vo.getDueAmount().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN));
            result.add(vo);
        });
        return result;
    }


    /**
     * 收入分布：取值于租赁费、物业费、公寓租金、企业水电费、公寓水电费账单的实收总金额，
     * 默认展示当月各收费项的实收总金额之和以及占所有费用项总收入比例。支持通过时间范围检索，时间范围样式为年-月（2022-05），仅支持单选；
     *
     * @param reqDTO
     * @return
     */
    @Override
    public IncomeDistributionVO incomeDistribution(ChargeStaReqDTO reqDTO) {
        IncomeDistributionVO result = new IncomeDistributionVO();
        ArrayList<String> monthList = Lists.newArrayList(reqDTO.getMonth());
        Map<Integer, BigDecimal> billData = new LinkedHashMap<>();
        int allBillType = 6;
        for (int i = 1; i < allBillType; i++) {
            billData.put(i, BigDecimal.ZERO);
        }
        List<MeterConsumptionBill> meterBillList = getMeterBillList(reqDTO.getParkOrgId(), null, monthList, RECEIVED.status);
        List<RentContractBill> rentBillList = getRentBillList(reqDTO.getParkOrgId(), null, monthList, RECEIVED.status);
        meterBillList.forEach(meterBill -> {
            if (meterBill.getContractType().equals(RENTAL_CONTRACT_1.getType())) {
                //企业水电费
                billData.put(RentFeeConstants.BillType.COMPANY_METER_BILL.type,
                        meterBill.getReceivedAmount().add(billData.get(RentFeeConstants.BillType.COMPANY_METER_BILL.type)));
            } else if (meterBill.getContractType().equals(APARTMENT_CONTRACT_1.getType())) {
                //公寓水电费
                billData.put(RentFeeConstants.BillType.APARTMENT_METER_BILL.type,
                        meterBill.getReceivedAmount().add(billData.get(RentFeeConstants.BillType.APARTMENT_METER_BILL.type)));
            }
        });
        rentBillList.forEach(rentBill -> {
            if (rentBill.getContractType().equals(RENTAL_CONTRACT_1.getType())) {
                //租赁费
                billData.put(RentFeeConstants.BillType.RENTAL_BILL.type,
                        rentBill.getReceivedAmount().add(billData.get(RentFeeConstants.BillType.RENTAL_BILL.type)));
            } else if (rentBill.getContractType().equals(PROPERTY_CONTRACT.getType())) {
                //物业费
                billData.put(RentFeeConstants.BillType.PROPERTY_BILL.type,
                        rentBill.getReceivedAmount().add(billData.get(RentFeeConstants.BillType.PROPERTY_BILL.type)));
            } else if (rentBill.getContractType().equals(APARTMENT_CONTRACT_1.getType())) {
                //公寓租赁费
                billData.put(RentFeeConstants.BillType.APARTMENT_BILL.type,
                        rentBill.getReceivedAmount().add(billData.get(RentFeeConstants.BillType.APARTMENT_BILL.type)));
            }
        });
        BigDecimal totalRentAmount = rentBillList.stream().map(RentContractBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMeterAmount = meterBillList.stream().map(MeterConsumptionBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.setTotalAmount(totalRentAmount.add(totalMeterAmount));
        result.setBillData(billData);
        return result;
    }


    /**
     * 收缴率(本月/累计)：，账单收入/应收金额之和，其中应收金额包括租赁费、物业费、公寓租金、企业水电费、公寓水电的应收金额；
     * 已收金额(本月/累计)：取值于租赁费、物业费、公寓租金、企业水电费、公寓水电的已收金额之和；
     * 待收金额(本月/累计)：取值于租赁费、物业费、公寓租金、企业水电费、公寓水电的待收金额之和；
     * 逾期金额(本月/累计)：取值于租赁费、物业费、公寓租金、企业水电费、公寓水电的逾期金额之和；
     * 账单收入(本月/累计)：取值于租赁费、物业费、公寓租金、企业水电费、公寓水电的账单的实收总金额之和。
     * 默认展示本月值。
     *
     * @param reqDTO
     * @return
     */
    @Override
    public FeeTopStatisticVO topStatistic(ChargeTopStatisticDTO reqDTO) {
        List<String> monthList = new LinkedList<>();
        String currentMonth = null;
        FeeTopStatisticVO result = new FeeTopStatisticVO();
        if (reqDTO.getType().equals(1)) {
            currentMonth = LocalDate.now().format(DEFAULT_YEAR_MONTH_FORMAT);
            monthList.add(currentMonth);
        }
        List<MeterConsumptionBill> meterBillList = getMeterBillList(reqDTO.getParkOrgId(), null, monthList, null);
        List<MeterConsumptionLateFee> meterLateFeeList = getMeterLateFeeList(reqDTO.getParkOrgId(), currentMonth);
        BigDecimal meterReceivedAmount = meterBillList.stream().map(MeterConsumptionBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal meterDueAmount = meterBillList.stream().map(MeterConsumptionBill::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal meterOverDateAmount = meterLateFeeList.stream().map(MeterConsumptionLateFee::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<RentContractBill> rentBillList = getRentBillList(reqDTO.getParkOrgId(), null, monthList, null);
        List<RentLateFee> rentLateFeeList = getRentLateFeeList(reqDTO.getParkOrgId(), currentMonth);
        BigDecimal rentReceivedAmount = rentBillList.stream().map(RentContractBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentDueAmount = rentBillList.stream().map(RentContractBill::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentOverDateAmount = rentLateFeeList.stream().map(RentLateFee::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.setBillReceived(meterReceivedAmount.add(rentReceivedAmount));
        result.setReceivedAmount(meterReceivedAmount.add(rentReceivedAmount));
        result.setDueAmount(meterDueAmount.add(rentDueAmount));
        result.setOverDateAmount(meterOverDateAmount.add(rentOverDateAmount));
        //收缴率
        BigDecimal meterTotalAmount = meterBillList.stream().map(MeterConsumptionBill::getReceivableAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentTotalAmount = rentBillList.stream().map(RentContractBill::getReceivableAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = meterTotalAmount.add(rentTotalAmount);
        if (totalAmount.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal payRate = result.getBillReceived().divide(totalAmount, 4, BigDecimal.ROUND_HALF_DOWN);
            result.setPayRate(payRate);
        }
        return result;
    }

    private List<RentLateFee> getRentLateFeeList(String parkOrgId, String month) {
        LambdaQueryChainWrapper<RentLateFee> wrapper = rentLateFeeService.lambdaQuery();
        wrapper.select(RentLateFee::getDueAmount, RentLateFee::getAccountPeriod)
                .eq(RentLateFee::getParkOrgId, parkOrgId);
        if (StringUtils.isNotBlank(month)) {
            wrapper.eq(RentLateFee::getAccountPeriod, month);
        }
        return wrapper.list();
    }

    private List<MeterConsumptionLateFee> getMeterLateFeeList(String parkOrgId, String month) {
        LambdaQueryChainWrapper<MeterConsumptionLateFee> wrapper = meterConsumptionLateFeeService.lambdaQuery();
        wrapper.select(MeterConsumptionLateFee::getDueAmount, MeterConsumptionLateFee::getSettledMonth)
                .eq(MeterConsumptionLateFee::getParkOrgId, parkOrgId);
        if (StringUtils.isNotBlank(month)) {
            wrapper.eq(MeterConsumptionLateFee::getSettledMonth, month);
        }
        return wrapper.list();
    }

    /**
     * 水电费离场结算已收费用
     *
     * @param reqDTO
     * @param contractType
     * @return
     */
    private List<MeterConsumptionLeave> getMeterLeaveList(ChargeStaTrendDTO reqDTO, Integer contractType) {
        List<MeterConsumptionLeave> result = new LinkedList<>();
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM").parse(reqDTO.getStartMonth());
            Date endDate = new SimpleDateFormat("yyyy-MM").parse(reqDTO.getEndMonth());
            result = meterConsumptionLeaveService.lambdaQuery().select(MeterConsumptionLeave::getTotalAmount, MeterConsumptionLeave::getLeaveDate)
                    .eq(MeterConsumptionLeave::getParkOrgId, reqDTO.getParkOrgId())
                    .eq(Objects.nonNull(contractType), MeterConsumptionLeave::getContractType, contractType)
                    .between(MeterConsumptionLeave::getLeaveDate, cn.hutool.core.date.DateUtil.beginOfMonth(startDate).toLocalDateTime().toLocalDate(),
                            cn.hutool.core.date.DateUtil.endOfMonth(endDate).toLocalDateTime().toLocalDate())
                    .list().parallelStream().collect(Collectors.toList());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 公寓管理获取账单数据
     *
     * @param amountData
     * @param parkOrgId
     */
    private void getMeterAndRentBillData(Map<String, ApartmentBillForScreen> amountData, String parkOrgId) {
        LocalDate now = LocalDate.now();
        LocalDate lastThirtyDays = now.minusDays(30);
        //租金、转换为万元、保留2位小数、四舍五入
        List<RentContractBill> rentList = getRentBillList(parkOrgId, APARTMENT_CONTRACT_1.getType(), null, RECEIVED.status);
        BigDecimal totalRentAmount = rentList.stream().map(RentContractBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal rentNearOneMonthAmount = rentList.stream().filter(t ->
                !t.getCheckoutDate().isBefore(lastThirtyDays) && !t.getCheckoutDate().isAfter(now))
                .map(RentContractBill::getReceivedAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        amountData.put("RENT", new ApartmentBillForScreen(totalRentAmount, rentNearOneMonthAmount));
        //水电费、转换为万元、保留2位小数、四舍五入
        List<MeterConsumptionBill> meterList = getMeterBillList(parkOrgId, APARTMENT_CONTRACT_1.getType(), null, RECEIVED.status);
        BigDecimal totalMeterAmount = meterList.stream().map(MeterConsumptionBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal meterNearOneMonthAmount = meterList.stream().filter(t ->
                !t.getCheckoutDate().isBefore(lastThirtyDays) && !t.getCheckoutDate().isAfter(now))
                .map(MeterConsumptionBill::getReceivedAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        amountData.put("METER", new ApartmentBillForScreen(totalMeterAmount, meterNearOneMonthAmount));
    }

    /**
     * 获取已租房间id集合
     * 已租数：取值于“待生效、生效中”公寓合同关联的房间总数，需按房间去重。
     */
    private Set<String> getAllRentedHouseIds(String parkOrgId) {
        List<Contract> contracts = contractService.lambdaQuery()
                .select(Contract::getId, Contract::getHouseMessages)
                .eq(Contract::getParkOrgId, parkOrgId)
                .eq(Contract::getType, APARTMENT_CONTRACT.getCode())
                .in(Contract::getState, IN_EFFECT.getCode(), TO_BE_EFFECTIVE.getCode()).list();
        return contracts.stream().flatMap(t -> t.getHouseMessages().stream())
                .map(HouseMessage::getHouseId).collect(Collectors.toSet());
    }

    /**
     * 获取房屋功能为“公寓—单间、公寓—标间、公寓—套间”且房屋属性为”租赁、销售“的房间总数
     *
     * @param parkOrgId
     * @return
     */
    private Set<String> getAllHouseIds(String parkOrgId) {
        String[] queryColumn = new String[]{"house_status", "house_function", "house_property"};
        Set<Integer> houseFunctions = Sets.newHashSet(2, 3, 4);
        Set<Integer> houseProperties = Sets.newHashSet(RENT.code, SELL.code);
        HouseQueryDTO houseQueryDTO = HouseQueryDTO.buildingIdsBuildDTO(parkOrgId, null, queryColumn);
        houseQueryDTO.setHouseFunctions(houseFunctions);
        houseQueryDTO.setHouseProperties(houseProperties);
        Map<String, Map<String, Object>> houseColumnsMap = houseProvider.queryHouseColumns(houseQueryDTO);
        Set<String> allHouseIds = Sets.newHashSet();
        houseColumnsMap.forEach((k, v) -> {
            Integer houseFunction = (Integer) v.get("house_function");
            Integer houseProperty = (Integer) v.get("house_property");
            if (houseFunctions.contains(houseFunction) && houseProperties.contains(houseProperty)) {
                allHouseIds.add(k);
            }
        });
        return allHouseIds;
    }


    /**
     * 大屏收费管理获取场地预定账单数据
     *
     * @param dto
     * @param meetingReserveBillList
     */
    private void getAmountDataByMeeting(BillListForScreen dto, List<MeetingReserveDTO> meetingReserveBillList) {
        Map<Integer, BigDecimal> amountData = dto.getAmountData();
        BigDecimal amount = meetingReserveBillList.stream().filter(meetingReserveDTO ->
                meetingReserveDTO.getStateChangeTime().toString().startsWith(dto.getMonth()))
                .map(MeetingReserveDTO::getReviewAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        amountData.put(MEETING_BILL.type, amount);
        dto.setAmountData(amountData);

    }

    /**
     * @param dto
     * @param billList
     * @param contractType
     */
    private void getAmountDataByRent(BillListForScreen dto, List<RentContractBill> billList, Integer contractType) {
        Map<Integer, BigDecimal> amountData = dto.getAmountData();
        BigDecimal amount = billList.stream().map(RentContractBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        if (contractType.equals(RENTAL_CONTRACT_1.getType())) {
            amountData.put(RENTAL_BILL.type, amount);
        } else if (contractType.equals(PROPERTY_CONTRACT.getType())) {
            amountData.put(PROPERTY_BILL.type, amount);
        } else if (contractType.equals(APARTMENT_CONTRACT_1.getType())) {
            amountData.put(APARTMENT_BILL.type, amount);
        }
        dto.setAmountData(amountData);
    }

    private void getAmountDataByMeter(BillListForScreen dto, List<MeterConsumptionBill> billList, Integer contractType) {
        Map<Integer, BigDecimal> amountData = dto.getAmountData();
        BigDecimal amount = billList.stream().map(MeterConsumptionBill::getReceivedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_DOWN);
        if (contractType.equals(RENTAL_CONTRACT_1.getType())) {
            amountData.put(COMPANY_METER_BILL.type, amount);
        } else if (contractType.equals(APARTMENT_CONTRACT_1.getType())) {
            amountData.put(APARTMENT_METER_BILL.type, amount);
        }
        dto.setAmountData(amountData);
    }

    private List<BillListForScreen> getInitializationData() {
        List<BillListForScreen> result = new LinkedList<>();
        BigDecimal amount = BigDecimal.ZERO;
        List<String> nearTwelveMonthList = getNearTwelveMonthList();
        nearTwelveMonthList.forEach(t -> {
            BillListForScreen dto = new BillListForScreen();
            Map<Integer, BigDecimal> feeData = new HashMap<>();
            for (int j = 1; j < 7; j++) {
                feeData.put(j, amount);
            }
            dto.setMonth(t);
            dto.setAmountData(feeData);
            result.add(dto);
        });
        return result;
    }

    private List<RentContractBill> getRentBillList(String parkOrgId, Integer contractType, List<String> monthList, Integer billStatus) {
        LambdaQueryChainWrapper<RentContractBill> wrapper = this.lambdaQuery();
        wrapper.select(RentContractBill::getReceivedAmount, RentContractBill::getAccountPeriod,
                RentContractBill::getContractType, RentContractBill::getCheckoutDate,
                RentContractBill::getDueAmount, RentContractBill::getReceivableAmount)
                .eq(Objects.nonNull(contractType), RentContractBill::getContractType, contractType)
                .eq(RentContractBill::getParkOrgId, parkOrgId)
                .eq(Objects.nonNull(billStatus), RentContractBill::getStatus, billStatus);
        if (CollectionUtil.isNotEmpty(monthList)) {
            wrapper.in(RentContractBill::getAccountPeriod, monthList);
        }
        return wrapper.list();
    }

    private List<MeterConsumptionBill> getMeterBillList(String parkOrgId, Integer contractType, List<String> monthList, Integer billStatus) {
        LambdaQueryChainWrapper<MeterConsumptionBill> wrapper = meterConsumptionBillService.lambdaQuery();
        wrapper.select(MeterConsumptionBill::getReceivedAmount, MeterConsumptionBill::getAccountPeriod,
                MeterConsumptionBill::getContractType, MeterConsumptionBill::getCheckoutDate,
                MeterConsumptionBill::getDueAmount, MeterConsumptionBill::getReceivableAmount)
                .eq(Objects.nonNull(contractType), MeterConsumptionBill::getContractType, contractType)
                .eq(MeterConsumptionBill::getParkOrgId, parkOrgId)
                .eq(Objects.nonNull(billStatus), MeterConsumptionBill::getStatus, billStatus);
        if (CollectionUtil.isNotEmpty(monthList)) {
            wrapper.in(MeterConsumptionBill::getAccountPeriod, monthList);
        }
        return wrapper.list();
    }

    private List<String> getNearTwelveMonthList() {
        List<String> result = new LinkedList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 12; i++) {
            String month = now.minusMonths(i).format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT));
            result.add(month);
        }
        return result;
    }

    private List<DeductionFileDTO> setFiles(String fileKeys) {
        if (StringUtils.isBlank(fileKeys)) {
            return null;
        }
        Set<String> keys = Arrays.stream(fileKeys.split(",")).collect(Collectors.toSet());
        List<DeductionFileDTO> result = Lists.newLinkedList();
        Map<String, FileDTO> fileDetailByKeysSelf = fileProvider.getFileDetailByKeysSelf(keys);
        fileDetailByKeysSelf.forEach((k, v) -> result.add(new DeductionFileDTO(k, v.getFileOriginalName())));
        return result;
    }


    private List<RentLateFee> getLateFeeUpdateList(Map<String, RentLateFee> lateFeeMaps, String updateByUserId) {
        if (CollectionUtil.isEmpty(lateFeeMaps)) {
            return Collections.emptyList();
        }
        List<RentLateFee> result = new LinkedList<>();
        lateFeeMaps.forEach((k, v) -> {
            //状态改为已缴清
            v.setStatus(PAID_UP.status);
            //已收滞纳金=已收滞纳金+待收滞纳金
            v.setOverdueFine(v.getOverdueFine().add(v.getReceiveFine()));
            v.setReceiveFine(BigDecimal.ZERO);
            v.setUpdateBy(updateByUserId);
            result.add(v);
        });
        return result;
    }

    private Map<String, RentLateFee> getLateFeeMapByBillIds(Set<String> billIds) {
        List<RentLateFee> list = rentLateFeeService.lambdaQuery()
                .in(RentLateFee::getRentBillId, billIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.parallelStream().collect(Collectors.toMap(RentLateFee::getRentBillId, Function.identity()));
    }

    /**
     * 获取更新费用表时的对象
     *
     * @param feeIds
     * @param deductionAmount
     * @return
     */
    private List<RentContractFee> getRentContractFeeUpdateList(Set<String> feeIds, BigDecimal deductionAmount, String updateByUserId) {
        Map<String, RentContractFee> feeMap = rentContractFeeService.lambdaQuery().select(
                RentContractFee::getId, RentContractFee::getDueAmount, RentContractFee::getFeeType, RentContractFee::getReceivedAmount,
                RentContractFee::getReceivableAmount, RentContractFee::getDeductionAmount, RentContractFee::getContractType)
                .in(RentContractFee::getId, feeIds).list()
                .parallelStream().collect(Collectors.toMap(RentContractFee::getId, Function.identity()));
        List<RentContractFee> result = Lists.newLinkedList();
        feeIds.forEach(t -> {
            RentContractFee rentContractFee = feeMap.get(t);
            rentContractFee.setId(t);
            if (Objects.nonNull(deductionAmount)) {
                //减免费用时
                //待收金额
                rentContractFee.setDueAmount(rentContractFee.getDueAmount().subtract(deductionAmount));
                //减免费用
                rentContractFee.setDeductionAmount(deductionAmount.add(rentContractFee.getDeductionAmount()));
            } else {
                //收款时
                Integer feeType = rentContractFee.getFeeType();
                Set<Integer> rentTypes = CollectionUtil.unionDistinct(
                        RENTAL_CONTRACT_1.getCalculationValues(),
                        PROPERTY_CONTRACT.getCalculationValues(),
                        APARTMENT_CONTRACT_1.getCalculationValues());
                Set<Integer> depositTypes = CollectionUtil.unionDistinct(
                        RENTAL_CONTRACT_2.getCalculationValues(), APARTMENT_CONTRACT_2.getCalculationValues());
                //如果为公寓合同的费用，其对应的每个房间的账单都收款后才修改费用状态为已收
                if (rentContractFee.getContractType().equals(APARTMENT_CONTRACT_1.getType())) {
                    Map<String, BigDecimal> map = getFeeInfo(t);
                    if (CollectionUtil.isNotEmpty(map)) {
                        rentContractFee.setReceivedAmount(map.get("totalReceivedAmount"));
                        rentContractFee.setDueAmount(map.get("totalDueAmount"));
                        if (map.get("statusSetSize").compareTo(BigDecimal.ONE) == 0) {
                            rentContractFee.setFeeStatus(1);
                        }
                        if (map.get("statusSetSize").compareTo(BigDecimal.ONE) == 0) {
                            if (rentTypes.contains(feeType)) {
                                //费用状态改为1已收
                                rentContractFee.setFeeStatus(1);
                            }
                            if (depositTypes.contains(feeType)) {
                                //押金状态设置为2已缴清
                                rentContractFee.setCashStatus(RentFeeConstants.RentCashStatusEnum.PAID_UP.status);
                            }
                        }
                    }
                } else {
                    if (rentTypes.contains(feeType)) {
                        //费用状态改为1已收
                        rentContractFee.setFeeStatus(1);
                    }
                    if (depositTypes.contains(feeType)) {
                        //押金状态设置为2已缴清
                        rentContractFee.setCashStatus(RentFeeConstants.RentCashStatusEnum.PAID_UP.status);
                    }
                    rentContractFee.setDueAmount(BigDecimal.ZERO);
                    //已收金额=应收金额-减免金额
                    rentContractFee.setReceivedAmount(rentContractFee.getReceivableAmount()
                            .subtract(rentContractFee.getDeductionAmount()));
                }
            }
            rentContractFee.setUpdateBy(updateByUserId);
            result.add(rentContractFee);
        });
        return result;
    }

    private Map<String, BigDecimal> getFeeInfo(String feeId) {
        List<RentContractBill> list = this.lambdaQuery().select(
                RentContractBill::getId, RentContractBill::getReceivedAmount,
                RentContractBill::getStatus, RentContractBill::getDueAmount)
                .eq(RentContractBill::getRentContractFeeId, feeId)
                .list();
        BigDecimal totalReceivedAmount = list.stream()
                .map(RentContractBill::getReceivedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDueAmount = list.stream()
                .map(RentContractBill::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Set<Integer> statusSet = list.stream().map(RentContractBill::getStatus).collect(Collectors.toSet());
        Map<String, BigDecimal> map = Maps.newHashMap();
        map.put("totalReceivedAmount", totalReceivedAmount);
        map.put("totalDueAmount", totalDueAmount);
        map.put("statusSetSize", BigDecimal.valueOf(statusSet.size()));
        return map;
    }

    /**
     * 向交易明细表中插入数据list
     *
     * @param reqDTO
     * @return
     */
    private List<RentContractTrade> getSaveTradeList(RentBillCheckoutReqDTO reqDTO) {
        List<RentContractBill> billList = this.lambdaQuery().in(RentContractBill::getId, reqDTO.getIds()).list();
        List<RentContractTrade> tradeList = billList.stream().map(t -> {
            RentContractFeeCopyTradeDTO copyTradeDTO = MyBeanUtils.copy(t, RentContractFeeCopyTradeDTO::new);
            RentContractTrade trade = MyBeanUtils.copy(copyTradeDTO, RentContractTrade::new);
            trade.setBillId(t.getId());
            trade.setCanReturn(CAN_RETURN.type);
            trade.setType(PAY_ORDER.type);
            trade.setRemark(reqDTO.getRemark());
            trade.setPayType(reqDTO.getPayType());
            trade.setCheckoutDate(reqDTO.getCheckoutDate());
            trade.setContractType(t.getContractType());
            EntityUserUtil.setAll(trade);
            trade.setTradeNo(rentContractTradeService.getTradeNo());
            return trade;
        }).collect(Collectors.toList());
        return tradeList;
    }


    private List<FeeDetail> getFeeDetailList(RentContractBill entity) {
        List<FeeDetail> feeDetailList = Lists.newArrayListWithCapacity(1);
        FeeDetail feeDetail = MyBeanUtils.copy(entity, FeeDetail::new);
        feeDetailList.add(feeDetail);
        return feeDetailList;
    }

    private List<RentContractBillRecordDTO> getDeductionRecordList(RentContractBill entity) {
        List<RentContractBillRecord> list = rentContractBillRecordService.lambdaQuery()
                .eq(RentContractBillRecord::getBillId, entity.getId()).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Set<String> userIds = list.parallelStream()
                .map(RentContractBillRecord::getCreateBy).collect(Collectors.toSet());
        Map<String, String> userId2NameMap = commonProvider
                .getNamesByIdsAndType(userIds, SimpleNode.TypeEnum.USER.getCode());
        return MyBeanUtils.copyList(list, RentContractBillRecordDTO::new,
                (s, t) -> t.setCreateByName(userId2NameMap.get(t.getCreateBy())));
    }

    private List<LateFeeDetail> getLateFeeDetailList(RentContractBill entity) {
        List<RentLateFee> list = rentLateFeeService.lambdaQuery()
                .eq(RentLateFee::getRentBillId, entity.getId()).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return MyBeanUtils.copyList(list, LateFeeDetail::new);
    }

    private List<CheckoutRecord> getCheckoutRecordList(RentContractBill entity) {
        List<RentContractTrade> list = rentContractTradeService.lambdaQuery()
                .eq(RentContractTrade::getBillId, entity.getId()).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Set<String> userIds = list.parallelStream().map(RentContractTrade::getCreateBy).collect(Collectors.toSet());
        Map<String, String> userId2NameMap = commonProvider.getNamesByIdsAndType(userIds, SimpleNode.TypeEnum.USER.getCode());
        return MyBeanUtils.copyList(list, CheckoutRecord::new, (s, t) -> {
            t.setAmount(s.getReceivedAmount());
            if (t.getType().equals(RETURN_ORDER.type)) {
                t.setAmount(t.getAmount().multiply(new BigDecimal("-1")));
            }
            t.setCreateByName(userId2NameMap.get(t.getCreateBy()));
        });
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<RentContractBill> getConditionWrapper(RentContractBillQuery query) {
        String parkOrgId = query.getParkOrgId();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(parkOrgId);
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<RentContractBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentContractBill::getContractType, query.getContractType())
                .like(StringUtils.isNotBlank(query.getCompanyName()), RentContractBill::getCompanyName, query.getCompanyName())
                .like(StringUtils.isNotBlank(query.getBillNo()), RentContractBill::getBillNo, query.getBillNo())
                .eq(Objects.nonNull(query.getStatus()), RentContractBill::getStatus, query.getStatus())
                .like(StringUtils.isNotBlank(query.getContractNo()), RentContractBill::getContractNo, query.getContractNo())
                .in(RentContractBill::getParkOrgId, parkOrgIds)
                .orderByDesc(RentContractBill::getCreateTime);
        //按应收日期查询
        LocalDate dueDateStart = query.getDueDateStart();
        LocalDate dueDateEnd = query.getDueDateEnd();
        if (Objects.nonNull(dueDateStart) && Objects.nonNull(dueDateEnd)) {
            queryWrapper.ge(RentContractBill::getDueDate, dueDateStart)
                    .le(RentContractBill::getDueDate, dueDateEnd);
        }
        //按最晚缴纳日期查询
        LocalDate latestPayDateStart = query.getLatestPayDateStart();
        LocalDate latestPayDateEnd = query.getLatestPayDateEnd();
        if (Objects.nonNull(latestPayDateStart) && Objects.nonNull(latestPayDateEnd)) {
            queryWrapper.ge(RentContractBill::getLatestPayDate, latestPayDateStart)
                    .le(RentContractBill::getLatestPayDate, latestPayDateEnd);
        }
        return queryWrapper;
    }

    private LambdaQueryWrapper<RentContractBill> getConditionWrapperForCompanyPort(CompanyPortBillQuery query) {
        Set<String> companyIds = companyProvider.findIdsByBusinessUserId(SystemUtil.getInstance().getCurrentUserId());
        MyPageInfoUtils.isEmptyPage(companyIds);
        LambdaQueryWrapper<RentContractBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(query.getType()), RentContractBill::getContractType, query.getType())
                .eq(StringUtils.isNotBlank(query.getAccountPeriod()), RentContractBill::getAccountPeriod, query.getAccountPeriod())
                .like(StringUtils.isNotBlank(query.getBillNo()), RentContractBill::getBillNo, query.getBillNo())
                .like(StringUtils.isNotBlank(query.getChargeName()), RentContractBill::getChargeName, query.getChargeName())
                .in(RentContractBill::getCompanyId, companyIds)
                .orderByDesc(RentContractBill::getCreateTime);
        return queryWrapper;
    }

    private List<RentContractBillListVO> convert(List<RentContractBill> list, Integer contractType) {
        List<RentContractBillListVO> result = MyBeanUtils.copyList(list, RentContractBillListVO::new);
        if (contractType.equals(APARTMENT_CONTRACT_1.getType())) {
            //公寓费用时返回入驻人员名
            Set<String> employeeIds = Sets.newHashSet();
            list.forEach(t -> employeeIds.addAll(CollectionUtil.unionDistinct(employeeIds, t.getEmployeeIds())));
            Map<String, String> employeeNames = commonProvider.getNamesByIdsAndType(
                    employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
            result.forEach(t -> {
                List<String> currentEmployeeIds = t.getEmployeeIds();
                if (CollectionUtil.isNotEmpty(employeeNames) && CollectionUtil.isNotEmpty(currentEmployeeIds)) {
                    List<String> names = Lists.newArrayListWithCapacity(currentEmployeeIds.size());
                    currentEmployeeIds.forEach(id -> names.add(employeeNames.get(id)));
                    t.setEmployeeIdNames(names);
                }
            });
        }
        return result;
    }

    /**
     * 获取账单编号
     */
    @Override
    public String getBillNo() {
        //账单编码
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(RENT_BILL_CODE_PREFIX);
        LocalDateTime dateTime = numberSeq.getDateTime();
        String billNo = new StringBuilder(RENT_BILL_CODE_PREFIX).append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHH")))
                .append(numberSeq.getValue()).toString();
        return billNo;
    }
}
