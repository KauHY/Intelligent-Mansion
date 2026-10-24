package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.dto.RentContractTradeDTO;
import com.crlink.smartpark.contract.entity.RentContractBill;
import com.crlink.smartpark.contract.entity.RentContractFee;
import com.crlink.smartpark.contract.entity.RentContractTrade;
import com.crlink.smartpark.contract.mapper.RentContractTradeMapper;
import com.crlink.smartpark.contract.model.excel.RentContractTradeExcelExportDTO;
import com.crlink.smartpark.contract.model.query.RentContractTradeQuery;
import com.crlink.smartpark.contract.model.request.RentContractFeeCopyTradeDTO;
import com.crlink.smartpark.contract.model.request.ReturnMoneyReqDTO;
import com.crlink.smartpark.contract.service.RentContractBillService;
import com.crlink.smartpark.contract.service.RentContractFeeService;
import com.crlink.smartpark.contract.service.RentContractTradeService;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.TIME_FORMATTER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.*;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_TRADE_CODE_PREFIX;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_TRADE_EXPORT_FILENAME;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentCashStatusEnum.CLEARED;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeReturnOrderEnum.CAN_NOT_RETURN;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.PAY_ORDER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.RETURN_ORDER;


/**
 * 租赁合同交易明细 服务实现类
 * */
@Service
public class RentContractTradeServiceImpl extends ServiceImpl<RentContractTradeMapper, RentContractTrade> implements RentContractTradeService {

    @Autowired
    private RentContractBillService rentContractBillService;

    @Autowired
    NumberProvider numberProvider;

    @Autowired
    CommonProvider commonProvider;

    @Autowired
    private RentContractFeeService rentContractFeeService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<RentContractTrade, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<RentContractTrade> lambdaQuery = this.lambdaQuery().in(RentContractTrade::getId, ids);
        if (flag) {
            lambdaQuery.select(RentContractTrade::getId, sValue);
        }
        Stream<RentContractTrade> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(RentContractTrade::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(RentContractTrade::getId, Function.identity()));
        }
        return result;
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public RentContractTradeDTO getRentContractTradeById(String id) {
        RentContractTrade entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        RentContractTradeDTO result = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
        //有对应的退款记录
        if (entity.getCanReturn().equals(CAN_NOT_RETURN.type)) {
            Integer type = entity.getType();
            RentContractTrade otherTrade = getReturnTrade(entity.getBillId(), type);
            result.setExtraTradeId(otherTrade.getId());
            result.setExtraTradeNo(otherTrade.getTradeNo());
            result.setApplicantId(otherTrade.getCreateBy());
            if (PAY_ORDER.type.equals(type)) {
                result.setReturnTime(otherTrade.getCreateTime().format(TIME_FORMATTER));
            } else {
                result.setReturnTime(entity.getCreateTime().toLocalDate().toString());
            }
            result.setReturnAmount(entity.getReturnAmount());
            if (Objects.equals(APARTMENT_CONTRACT_1.getType(), entity.getContractType())) {
                result.setEmployeeIdNames(getEmployeeNames(result.getEmployeeIds()));
            }
        }
        return result;
    }

    private RentContractTrade getReturnTrade(String billId, Integer type) {
        LambdaQueryChainWrapper<RentContractTrade> wrapper = this.lambdaQuery();
        wrapper.select(RentContractTrade::getId, RentContractTrade::getTradeNo, RentContractTrade::getCreateTime,
                RentContractTrade::getCreateBy, RentContractTrade::getReturnAmount)
                .eq(RentContractTrade::getBillId, billId).last("limit 1");
        if (PAY_ORDER.type.equals(type)) {
            wrapper.eq(RentContractTrade::getType, 2);
        }
        if (RETURN_ORDER.type.equals(type)) {
            wrapper.eq(RentContractTrade::getType, 1);
        }
        return wrapper.one();
    }


    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<RentContractTradeDTO> listRentContractTradePage(RentContractTradeQuery query) {
        LambdaQueryWrapper<RentContractTrade> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<RentContractTrade> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<RentContractTradeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void export(RentContractTradeQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)),
                sourceList -> MyBeanUtils.copyList(entitiesToDTOs(sourceList), RentContractTradeExcelExportDTO::new
//                        , (s, t) -> t.setFeeDate(s.getStartDate().toString() + s.getEndDate().toString())
                ),
                RENT_TRADE_EXPORT_FILENAME, RentContractTradeExcelExportDTO.class);
    }

    /**
     * 退款操作
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean returnMoney(ReturnMoneyReqDTO dto) {
        RentContractTrade entity = Optional.ofNullable(getById(dto.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        judgeReturnAmount(entity.getReceivedAmount(), dto.getReturnAmount());
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        if (dto.getFromTrade()) {
            String billId = entity.getBillId();
            String feeId = rentContractBillService.id2ValueMap(Sets.newHashSet(billId), RentContractBill::getRentContractFeeId).get(billId);
            Integer feeType = rentContractFeeService.id2ValueMap(Sets.newHashSet(feeId), RentContractFee::getFeeType).get(feeId);
            Set<Integer> depositTypes = CollectionUtil.unionDistinct(
                    RENTAL_CONTRACT_2.getCalculationValues(), APARTMENT_CONTRACT_2.getCalculationValues());
            if (depositTypes.contains(feeType)) {
                rentContractFeeService.lambdaUpdate().eq(RentContractFee::getId, feeId)
                        .set(RentContractFee::getRefundedAmount, dto.getReturnAmount())
                        .set(RentContractFee::getCashStatus, CLEARED.status)
                        .set(RentContractFee::getUpdateBy, currentUserId)
                        .update();
            }
        }
        //原记录设置为不可退款
        this.lambdaUpdate()
                .eq(RentContractTrade::getId, dto.getId())
                .set(RentContractTrade::getCanReturn, CAN_NOT_RETURN.type)
                .set(RentContractTrade::getReturnAmount, dto.getReturnAmount())
                .set(RentContractTrade::getUpdateBy, currentUserId)
                .update();
        RentContractFeeCopyTradeDTO copyTradeDTO = MyBeanUtils.copy(entity, RentContractFeeCopyTradeDTO::new);
        RentContractTrade trade = MyBeanUtils.copy(copyTradeDTO, RentContractTrade::new);
        BeanUtils.copyProperties(dto, trade);
        trade.setId(null);
        trade.setReturnAmount(dto.getReturnAmount());
        trade.setType(RETURN_ORDER.type);
        trade.setTradeNo(getTradeNo());
        trade.setCanReturn(CAN_NOT_RETURN.type);
        trade.setReceivedAmount(dto.getReturnAmount());
        EntityUserUtil.setAll(trade);
        return this.save(trade);
    }

    /**
     * 退款金额:大于0且小于等于“可退金额”的数字
     *
     * @param receivedAmount 可退金额即实付金额
     * @param returnAmount   退款金额
     */
    @Override
    public void judgeReturnAmount(BigDecimal receivedAmount, BigDecimal returnAmount) {
        if (returnAmount.compareTo(BigDecimal.ZERO) < 1) {
            throw new ResolinkRuntimeException("300015");
        } else if (returnAmount.compareTo(receivedAmount) > 0) {
            throw new ResolinkRuntimeException("300016");
        }
    }


    /**
     * 获取交易明细编号
     *
     * @return
     */
    @Override
    public String getTradeNo() {
        //交易明细编码
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(RENT_TRADE_CODE_PREFIX);
        String tradeNo = new StringBuilder(RENT_TRADE_CODE_PREFIX).append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern("yyMMddHH")))
                .append(numberSeq.getValue()).toString();
        return tradeNo;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<RentContractTrade> getConditionWrapper(RentContractTradeQuery query) {
        String parkOrgId = query.getParkOrgId();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(parkOrgId);
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<RentContractTrade> queryWrapper = new LambdaQueryWrapper<RentContractTrade>();
        String companyName = query.getCompanyName();
        LocalDateTime payTimeStart = query.getPayTimeStart();
        LocalDateTime payTimeEnd = query.getPayTimeEnd();
        queryWrapper.eq(RentContractTrade::getContractType, query.getContractType())
                .like(StringUtils.isNotBlank(companyName), RentContractTrade::getCompanyName, companyName)
                .like(StringUtils.isNotBlank(query.getBillNo()), RentContractTrade::getBillNo, query.getBillNo())
                .eq(Objects.nonNull(query.getType()), RentContractTrade::getType, query.getType())
                .in(RentContractTrade::getParkOrgId, parkOrgIds)
                .orderByDesc(RentContractTrade::getCreateTime);
        if (Objects.nonNull(payTimeStart) && Objects.nonNull(payTimeEnd)) {
            queryWrapper.ge(RentContractTrade::getCreateTime, payTimeStart)
                    .le(RentContractTrade::getCreateTime, payTimeEnd);
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<RentContractTradeDTO> entitiesToDTOs(List<RentContractTrade> list) {
        Set<String> billIds = list.parallelStream().map(RentContractTrade::getBillId).collect(Collectors.toSet());
        Map<String, RentContractBill> billData = getBillData(billIds);
        return MyBeanUtils.copyList(list, RentContractTradeDTO::new, (s, t) -> {
            RentContractBill rentContractBill = billData.get(s.getBillId());
//            t.setReceivableAmount(rentContractBill.getReceivableAmount());
//            t.setReceivedAmount(rentContractBill.getReceivedAmount());
            t.setHouseName(rentContractBill.getHouseName());
            t.setAccountPeriod(rentContractBill.getAccountPeriod());
            t.setChargeName(rentContractBill.getChargeName());
            t.setEmployeeIds(rentContractBill.getEmployeeIds());
        });
    }


    /**
     * 公寓费用返回员工名称
     *
     * @param employeeIds
     * @return
     */
    private List<String> getEmployeeNames(List<String> employeeIds) {
        if (CollectionUtils.isEmpty(employeeIds)) {
            return Collections.emptyList();
        }
        Set<String> ids = employeeIds.parallelStream().collect(Collectors.toSet());
        Map<String, String> employeeNames = commonProvider.getNamesByIdsAndType(
                ids, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        return employeeNames.values().parallelStream().collect(Collectors.toList());
    }

    /**
     * 根据账单id获取应收金额、实付金额、房源名称、所在账期、所含收费项
     *
     * @param billIds
     * @return
     */
    private Map<String, RentContractBill> getBillData(Set<String> billIds) {
        List<RentContractBill> list = rentContractBillService.lambdaQuery()
                .select(RentContractBill::getId, RentContractBill::getReceivableAmount, RentContractBill::getReceivedAmount,
                        RentContractBill::getHouseName, RentContractBill::getAccountPeriod, RentContractBill::getChargeName,
                        RentContractBill::getEmployeeIds)
                .in(RentContractBill::getId, billIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.parallelStream().collect(Collectors.toMap(RentContractBill::getId, Function.identity()));
    }
}
