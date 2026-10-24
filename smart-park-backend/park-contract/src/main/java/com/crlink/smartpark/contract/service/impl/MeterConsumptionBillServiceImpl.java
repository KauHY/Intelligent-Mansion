package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.dto.CheckoutRecord;
import com.crlink.smartpark.contract.dto.DeductionFileDTO;
import com.crlink.smartpark.contract.dto.LateFeeDetail;
import com.crlink.smartpark.contract.dto.RentContractBillRecordDTO;
import com.crlink.smartpark.contract.entity.*;
import com.crlink.smartpark.contract.mapper.MeterConsumptionBillMapper;
import com.crlink.smartpark.contract.model.excel.MeterConsumptionBillExcelExportDTO;
import com.crlink.smartpark.contract.model.query.CompanyPortBillQuery;
import com.crlink.smartpark.contract.model.query.RentContractBillQuery;
import com.crlink.smartpark.contract.model.request.*;
import com.crlink.smartpark.contract.model.vo.CompanyPortBillListVO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionBillListVO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionBillVO;
import com.crlink.smartpark.contract.service.*;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_BILL_EXPORT_FILENAME;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeBillEnum.RECEIVED;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeBillEnum.UNRECEIVED;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeReturnOrderEnum.CAN_RETURN;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.PAY_ORDER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.RETURN_ORDER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentLateStatusEnum.PAID_UP;


/**
 * 租赁合同账单 服务实现类
 * */
@Service
public class MeterConsumptionBillServiceImpl extends ServiceImpl<MeterConsumptionBillMapper, MeterConsumptionBill> implements MeterConsumptionBillService {


    @Autowired
    private MeterConsumptionBillRecordService meterConsumptionBillRecordService;

    @Autowired
    private MeterConsumptionFeeService meterConsumptionFeeService;

    @Autowired
    private MeterConsumptionTradeService meterConsumptionTradeService;

    @Autowired
    private RentContractTradeService rentContractTradeService;

    @Autowired
    private MeterConsumptionLateFeeService meterConsumptionLateFeeService;

    @Autowired
    NumberProvider numberProvider;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private FileProvider fileProvider;

    @Autowired
    private CompanyProvider companyProvider;


    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionBill, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(MeterConsumptionBill::getId, sValue)
                .in(MeterConsumptionBill::getId, ids).list().stream()
                .collect(HashMap::new, (map, meter) -> map.put(meter.getId(), sValue.apply(meter)), HashMap::putAll);
        return result;
    }

    /**
     * 修改账单的所在账期
     */
    @Override
    public Boolean updateMeterConsumptionBill(RentBillUpdateReqDTO reqDTO) {
        Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return this.lambdaUpdate()
                .eq(MeterConsumptionBill::getId, reqDTO.getId())
                .set(MeterConsumptionBill::getAccountPeriod, reqDTO.getAccountPeriod())
                .set(MeterConsumptionBill::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
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
        MeterConsumptionBill bill = Optional.ofNullable(getById(dto.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
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
        LambdaUpdateChainWrapper<MeterConsumptionBill> updateWrapper = this.lambdaUpdate();
        //当减免金额=待收金额，且无滞纳金，账单状态由“未收”更新为“已收”。
        //当减免金额=待收金额，有滞纳金，账单状态不更新，为“未收”。
        BigDecimal dueAmount = bill.getDueAmount().subtract(deductionAmount);
        if (dueAmount.compareTo(BigDecimal.ZERO) == 0) {
            MeterConsumptionLateFee lateFee = meterConsumptionLateFeeService.lambdaQuery()
                    .eq(MeterConsumptionLateFee::getRentBillId, dto.getId()).one();
            if (Objects.isNull(lateFee)) {
                updateWrapper.set(MeterConsumptionBill::getStatus, RECEIVED.status)
                        .set(MeterConsumptionBill::getCheckoutDate, LocalDate.now());
            }
        }
        //更新账单表
        String updateByUserId = SystemUtil.getInstance().getCurrentUserId();
        updateWrapper.eq(MeterConsumptionBill::getId, dto.getId())
                //待收金额
                .set(MeterConsumptionBill::getDueAmount, bill.getDueAmount().subtract(deductionAmount))
                //减免费用
                .set(MeterConsumptionBill::getDeductionAmount, deductionAmount.add(bill.getDeductionAmount()))
                .set(MeterConsumptionBill::getUpdateBy, updateByUserId)
                .update();
        //更新费用表
        List<MeterConsumptionFee> meterConsumptionFeeUpdateList =
                getMeterConsumptionFeeUpdateList(Sets.newHashSet(bill.getMeterConsumptionFeeId()), deductionAmount, updateByUserId);
        meterConsumptionFeeService.updateBatchById(meterConsumptionFeeUpdateList);
        //更新账单减免记录表
        MeterConsumptionBillRecord record = new MeterConsumptionBillRecord();
        record.setBillId(dto.getId());
        record.setContractType(bill.getContractType());
        record.setDeductionAmount(deductionAmount);
        record.setFileList(setFiles(dto.getFile()));
        record.setRemark(dto.getRemark());
        EntityUserUtil.setAll(record);
        return meterConsumptionBillRecordService.save(record);
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeterConsumptionBillVO getMeterConsumptionBillById(String id) {
        MeterConsumptionBill entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeterConsumptionBillVO result = MyBeanUtils.copy(entity, MeterConsumptionBillVO::new);
//        String feeId = entity.getMeterConsumptionFeeId();
//        List<ChargeDetail> chargeDetails = meterConsumptionFeeService.id2ValueMap(
//                Sets.newHashSet(feeId), MeterConsumptionFee::getChargeDetail).get(feeId);
//        result.setChargeDetail(chargeDetails);
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
    public PageInfo<MeterConsumptionBillListVO> listMeterConsumptionBillPage(RentContractBillQuery query) {
        LambdaQueryWrapper<MeterConsumptionBill> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<MeterConsumptionBill> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
//        List<MeterConsumptionBillListVO> result = convert(list, query.getContractType());
        List<MeterConsumptionBillListVO> result = MyBeanUtils.copyList(list, MeterConsumptionBillListVO::new);
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
        LambdaQueryWrapper<MeterConsumptionBill> wrapper = getConditionWrapperForCompanyPort(query);
        query.startPage();
        List<MeterConsumptionBill> list = list(wrapper);
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
                .eq(MeterConsumptionBill::getId, reqDTO.getId())
                .set(MeterConsumptionBill::getPayType, reqDTO.getPayType())
                .set(MeterConsumptionBill::getCompanyFiles, reqDTO.getCompanyFiles())
                .set(MeterConsumptionBill::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .update();
    }

    @Override
    public void export(RentContractBillQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)), this::entitiesToExportDTOs,
                RENT_BILL_EXPORT_FILENAME, MeterConsumptionBillExcelExportDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkout(RentBillCheckoutReqDTO reqDTO) {
        Set<String> ids = reqDTO.getIds();
        Map<String, BigDecimal> dueAmountMap = id2ValueMap(ids, MeterConsumptionBill::getDueAmount);
        List<MeterConsumptionBill> list = new LinkedList<>();
        String updateByUserId = SystemUtil.getInstance().getCurrentUserId();
        Map<String, MeterConsumptionLateFee> lateFeeMaps = getLateFeeMapByBillIds(ids);
        ids.forEach(t -> {
            MeterConsumptionBill bill = new MeterConsumptionBill();
            bill.setId(t);
            bill.setCheckoutDate(reqDTO.getCheckoutDate());
            bill.setPayType(reqDTO.getPayType());
            bill.setRemark(reqDTO.getRemark());
            //状态改为已收
            bill.setStatus(RECEIVED.status);
            bill.setDueAmount(BigDecimal.ZERO);
            bill.setReceivedAmount(dueAmountMap.get(t));
            if (CollectionUtil.isNotEmpty(lateFeeMaps)) {
                MeterConsumptionLateFee lateFee = lateFeeMaps.get(t);
                if (Objects.nonNull(lateFee)) {
                    bill.setReceivedAmount(bill.getReceivedAmount().add(lateFee.getReceiveFine()));
                }
            }
            bill.setUpdateBy(updateByUserId);
            list.add(bill);
        });
        //更新账单表
        this.updateBatchById(list);
        //更新滞纳金表
        List<MeterConsumptionLateFee> lateFeeUpdateList = getLateFeeUpdateList(lateFeeMaps, updateByUserId);
        if (CollectionUtils.isNotEmpty(lateFeeUpdateList)) {
            meterConsumptionLateFeeService.updateBatchById(lateFeeUpdateList);
        }
        //交易明细表新增数据
        meterConsumptionTradeService.saveBatch(getSaveTradeList(reqDTO));
        //更新费用表
        Set<String> feeIds = id2ValueMap(ids, MeterConsumptionBill::getMeterConsumptionFeeId)
                .values().parallelStream().collect(Collectors.toSet());
        List<MeterConsumptionFee> meterConsumptionFeeUpdateList =
                getMeterConsumptionFeeUpdateList(feeIds, null, updateByUserId);
        return meterConsumptionFeeService.updateBatchById(meterConsumptionFeeUpdateList);
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
        BigDecimal dueAmount = id2ValueMap(Sets.newHashSet(id), MeterConsumptionBill::getDueAmount).get(id);
        MeterConsumptionLateFee meterConsumptionLateFee = meterConsumptionLateFeeService.lambdaQuery().select(
                MeterConsumptionLateFee::getId, MeterConsumptionLateFee::getDueAmount, MeterConsumptionLateFee::getDueDays,
                MeterConsumptionLateFee::getReceivableFine, MeterConsumptionLateFee::getReceiveFine,
                MeterConsumptionLateFee::getOverdueFine, MeterConsumptionLateFee::getChargeName)
                .eq(MeterConsumptionLateFee::getRentBillId, id).one();
        if (Objects.nonNull(meterConsumptionLateFee)) {
            result = MyBeanUtils.copy(meterConsumptionLateFee, LateFeeDetail::new);
            result.setTotalAmount(dueAmount.add(meterConsumptionLateFee.getDueAmount()));
            return result;
        }
        return result.setTotalAmount(dueAmount);
    }

    /**
     * 批量操作：根据账单id获取滞纳金
     *
     * @param billIds
     * @return
     */
    @Override
    public LateFeeDetail findLateFeeByBillIds(Set<String> billIds) {
        if (CollectionUtils.isEmpty(billIds)) {
            throw new ResolinkRuntimeException("10007");
        }
        LateFeeDetail result = new LateFeeDetail();
        //查询代收金额
        BigDecimal dueAmount = this.lambdaQuery().select(MeterConsumptionBill::getId, MeterConsumptionBill::getDueAmount)
                .in(MeterConsumptionBill::getId, billIds).list()
                .stream().map(MeterConsumptionBill::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.setDueAmount(dueAmount);
        //查询滞纳金
        List<MeterConsumptionLateFee> meterConsumptionLateFeeList = meterConsumptionLateFeeService
                .lambdaQuery().select(MeterConsumptionLateFee::getId, MeterConsumptionLateFee::getReceiveFine)
                .in(MeterConsumptionLateFee::getRentBillId, billIds).list();
        if (CollectionUtils.isEmpty(meterConsumptionLateFeeList)) {
            //无滞纳金
            result.setReceiveFine(BigDecimal.ZERO);
        } else {
            BigDecimal receiveFine = meterConsumptionLateFeeList.stream()
                    .map(MeterConsumptionLateFee::getReceiveFine).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.setReceiveFine(receiveFine);
        }
        result.setTotalAmount(dueAmount.add(result.getReceiveFine()));
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


    private List<MeterConsumptionLateFee> getLateFeeUpdateList(Map<String, MeterConsumptionLateFee> lateFeeMaps, String updateByUserId) {
        if (CollectionUtil.isEmpty(lateFeeMaps)) {
            return Collections.emptyList();
        }
        List<MeterConsumptionLateFee> result = new LinkedList<>();
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

    private Map<String, MeterConsumptionLateFee> getLateFeeMapByBillIds(Set<String> billIds) {
        List<MeterConsumptionLateFee> list = meterConsumptionLateFeeService.lambdaQuery()
                .in(MeterConsumptionLateFee::getRentBillId, billIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.parallelStream().collect(Collectors.toMap(MeterConsumptionLateFee::getRentBillId, Function.identity()));
    }

    /**
     * 获取更新费用表时的对象
     *
     * @param feeIds
     * @param deductionAmount
     * @return
     */
    private List<MeterConsumptionFee> getMeterConsumptionFeeUpdateList(Set<String> feeIds, BigDecimal deductionAmount, String updateByUserId) {
        Map<String, MeterConsumptionFee> feeMap = meterConsumptionFeeService.lambdaQuery()
                .select(MeterConsumptionFee::getId, MeterConsumptionFee::getDueAmount, MeterConsumptionFee::getContractType,
                        MeterConsumptionFee::getReceivableAmount, MeterConsumptionFee::getDeductionAmount)
                .in(MeterConsumptionFee::getId, feeIds).list()
                .parallelStream().collect(Collectors.toMap(MeterConsumptionFee::getId, Function.identity()));
        List<MeterConsumptionFee> result = Lists.newLinkedList();
        feeIds.forEach(t -> {
            MeterConsumptionFee meterConsumptionFee = feeMap.get(t);
            meterConsumptionFee.setId(t);
            if (Objects.nonNull(deductionAmount)) {
                //减免费用时
                //待收金额
                meterConsumptionFee.setDueAmount(meterConsumptionFee.getDueAmount().subtract(deductionAmount));
                //减免费用
                meterConsumptionFee.setDeductionAmount(deductionAmount.add(meterConsumptionFee.getDeductionAmount()));
            } else {
                //收款时
                //如果为公寓的费用，其对应的每个房间的账单都收款后才修改费用状态为已收
                if (meterConsumptionFee.getContractType().equals(APARTMENT_CONTRACT_1.getType())) {
                    Map<String, BigDecimal> map = getFeeInfo(t);
                    if (CollectionUtil.isNotEmpty(map)) {
                        meterConsumptionFee.setReceivedAmount(map.get("totalReceivedAmount"));
                        meterConsumptionFee.setDueAmount(map.get("totalDueAmount"));
                        if (map.get("statusSetSize").compareTo(BigDecimal.ONE) == 0) {
                            meterConsumptionFee.setFeeStatus(1);
                        }
                        if (map.get("statusSetSize").compareTo(BigDecimal.ONE) == 0) {
                            //费用状态改为1已收
                            meterConsumptionFee.setFeeStatus(1);
                        }
                    }
                } else {
                    meterConsumptionFee.setFeeStatus(1);
                    meterConsumptionFee.setDueAmount(BigDecimal.ZERO);
                    //已收金额=应收金额-减免金额
                    meterConsumptionFee.setReceivedAmount(meterConsumptionFee.getReceivableAmount()
                            .subtract(meterConsumptionFee.getDeductionAmount()));
                }
            }
            meterConsumptionFee.setUpdateBy(updateByUserId);
            result.add(meterConsumptionFee);
        });
        return result;
    }

    private Map<String, BigDecimal> getFeeInfo(String feeId) {
        List<MeterConsumptionBill> list = this.lambdaQuery().select(
                MeterConsumptionBill::getId, MeterConsumptionBill::getReceivedAmount,
                MeterConsumptionBill::getStatus, MeterConsumptionBill::getDueAmount)
                .eq(MeterConsumptionBill::getMeterConsumptionFeeId, feeId)
                .list();
        BigDecimal totalReceivedAmount = list.stream()
                .map(MeterConsumptionBill::getReceivedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDueAmount = list.stream()
                .map(MeterConsumptionBill::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Set<Integer> statusSet = list.stream().map(MeterConsumptionBill::getStatus).collect(Collectors.toSet());
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
    private List<MeterConsumptionTrade> getSaveTradeList(RentBillCheckoutReqDTO reqDTO) {
        List<MeterConsumptionBill> billList = this.lambdaQuery().in(MeterConsumptionBill::getId, reqDTO.getIds()).list();
        List<MeterConsumptionTrade> tradeList = billList.parallelStream().map(t -> {
            RentContractFeeCopyTradeDTO copyTradeDTO = MyBeanUtils.copy(t, RentContractFeeCopyTradeDTO::new);
            MeterConsumptionTrade trade = MyBeanUtils.copy(copyTradeDTO, MeterConsumptionTrade::new);
//            trade.setPayTime(reqDTO.getCheckoutDate());
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


    private List<RentContractBillRecordDTO> getDeductionRecordList(MeterConsumptionBill entity) {
        List<MeterConsumptionBillRecord> list = meterConsumptionBillRecordService.lambdaQuery()
                .eq(MeterConsumptionBillRecord::getBillId, entity.getId()).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Set<String> userIds = list.parallelStream()
                .map(MeterConsumptionBillRecord::getCreateBy).collect(Collectors.toSet());
        Map<String, String> userId2NameMap = commonProvider
                .getNamesByIdsAndType(userIds, SimpleNode.TypeEnum.USER.getCode());
        return MyBeanUtils.copyList(list, RentContractBillRecordDTO::new,
                (s, t) -> t.setCreateByName(userId2NameMap.get(t.getCreateBy())));
    }

    private List<LateFeeDetail> getLateFeeDetailList(MeterConsumptionBill entity) {
        List<MeterConsumptionLateFee> list = meterConsumptionLateFeeService.lambdaQuery()
                .eq(MeterConsumptionLateFee::getRentBillId, entity.getId()).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return MyBeanUtils.copyList(list, LateFeeDetail::new);
    }

    private List<CheckoutRecord> getCheckoutRecordList(MeterConsumptionBill entity) {
        List<MeterConsumptionTrade> list = meterConsumptionTradeService.lambdaQuery()
                .eq(MeterConsumptionTrade::getBillId, entity.getId()).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Set<String> userIds = list.parallelStream().map(MeterConsumptionTrade::getCreateBy).collect(Collectors.toSet());
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
    private LambdaQueryWrapper<MeterConsumptionBill> getConditionWrapper(RentContractBillQuery query) {
        String parkOrgId = query.getParkOrgId();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(parkOrgId);
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<MeterConsumptionBill> queryWrapper = new LambdaQueryWrapper<MeterConsumptionBill>();
        queryWrapper.eq(MeterConsumptionBill::getContractType, query.getContractType())
                .like(StringUtils.isNotBlank(query.getCompanyName()), MeterConsumptionBill::getCompanyName, query.getCompanyName())
                .like(StringUtils.isNotBlank(query.getBillNo()), MeterConsumptionBill::getBillNo, query.getBillNo())
                .eq(Objects.nonNull(query.getStatus()), MeterConsumptionBill::getStatus, query.getStatus())
                .in(MeterConsumptionBill::getParkOrgId, parkOrgIds)
                .orderByDesc(MeterConsumptionBill::getCreateTime);
        //按应收日期查询
        LocalDate dueDateStart = query.getDueDateStart();
        LocalDate dueDateEnd = query.getDueDateEnd();
        if (Objects.nonNull(dueDateStart) && Objects.nonNull(dueDateEnd)) {
            queryWrapper.ge(MeterConsumptionBill::getDueDate, dueDateStart)
                    .le(MeterConsumptionBill::getDueDate, dueDateEnd);
        }
        //按最晚缴纳日期查询
        LocalDate latestPayDateStart = query.getLatestPayDateStart();
        LocalDate latestPayDateEnd = query.getLatestPayDateEnd();
        if (Objects.nonNull(latestPayDateStart) && Objects.nonNull(latestPayDateEnd)) {
            queryWrapper.ge(MeterConsumptionBill::getLatestPayDate, latestPayDateStart)
                    .le(MeterConsumptionBill::getLatestPayDate, latestPayDateEnd);
        }
        return queryWrapper;
    }

    private LambdaQueryWrapper<MeterConsumptionBill> getConditionWrapperForCompanyPort(CompanyPortBillQuery query) {
        Set<String> companyIds = companyProvider.findIdsByBusinessUserId(SystemUtil.getInstance().getCurrentUserId());
        MyPageInfoUtils.isEmptyPage(companyIds);
        LambdaQueryWrapper<MeterConsumptionBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(query.getType()), MeterConsumptionBill::getContractType, query.getType())
                .eq(StringUtils.isNotBlank(query.getAccountPeriod()), MeterConsumptionBill::getAccountPeriod, query.getAccountPeriod())
                .like(StringUtils.isNotBlank(query.getBillNo()), MeterConsumptionBill::getBillNo, query.getBillNo())
                .like(StringUtils.isNotBlank(query.getChargeName()), MeterConsumptionBill::getChargeName, query.getChargeName())
                .in(MeterConsumptionBill::getCompanyId, companyIds)
                .orderByDesc(MeterConsumptionBill::getCreateTime);
        return queryWrapper;
    }

    private List<MeterConsumptionBillExcelExportDTO> entitiesToExportDTOs(List<MeterConsumptionBill> list) {
        Set<String> houseIds = list.parallelStream().map(MeterConsumptionBill::getHouseIds).flatMap(Collection::stream).collect(Collectors.toSet());
        Map<String, String> houseNameMaps = commonProvider.getBuildingHouseNamesByIds(houseIds);
        if (CollectionUtil.isEmpty(houseNameMaps)) {
            return MyBeanUtils.copyList(list, MeterConsumptionBillExcelExportDTO::new);
        }
        List<MeterConsumptionBillExcelExportDTO> result = MyBeanUtils.copyList(list, MeterConsumptionBillExcelExportDTO::new, (s, t) -> {
            List<String> name = new LinkedList<>();
            s.getHouseIds().forEach(houseId -> name.add(houseNameMaps.get(houseId)));
            t.setHouseName(name);
        });
        return result;
    }
}
