package com.crlink.smartpark.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.dto.MeterConsumptionTradeDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionBill;
import com.crlink.smartpark.contract.entity.MeterConsumptionTrade;
import com.crlink.smartpark.contract.mapper.MeterConsumptionTradeMapper;
import com.crlink.smartpark.contract.model.excel.MeterConsumptionTradeExcelExportDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionTradeQuery;
import com.crlink.smartpark.contract.model.request.RentContractFeeCopyTradeDTO;
import com.crlink.smartpark.contract.model.request.ReturnMoneyReqDTO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionTradeListVO;
import com.crlink.smartpark.contract.service.MeterConsumptionBillService;
import com.crlink.smartpark.contract.service.MeterConsumptionTradeService;
import com.crlink.smartpark.contract.service.RentContractTradeService;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.TIME_FORMATTER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_TRADE_EXPORT_FILENAME;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeReturnOrderEnum.CAN_NOT_RETURN;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.PAY_ORDER;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeTradeTypeEnum.RETURN_ORDER;


/**
 * 租赁合同交易明细 服务实现类
 * */
@Service
public class MeterConsumptionTradeServiceImpl extends ServiceImpl<MeterConsumptionTradeMapper, MeterConsumptionTrade> implements MeterConsumptionTradeService {

    @Autowired
    private MeterConsumptionBillService meterConsumptionBillService;

    @Autowired
    NumberProvider numberProvider;

    @Autowired
    CommonProvider commonProvider;

    @Autowired
    private RentContractTradeService rentContractTradeService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionTrade, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeterConsumptionTrade> lambdaQuery = this.lambdaQuery().in(MeterConsumptionTrade::getId, ids);
        if (flag) {
            lambdaQuery.select(MeterConsumptionTrade::getId, sValue);
        }
        Stream<MeterConsumptionTrade> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeterConsumptionTrade::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeterConsumptionTrade::getId, Function.identity()));
        }
        return result;
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeterConsumptionTradeDTO getMeterConsumptionTradeById(String id) {
        MeterConsumptionTrade entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeterConsumptionTradeDTO result = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
//        MeterConsumptionTradeDTO result = MyBeanUtils.copy(listVO, MeterConsumptionTradeDTO::new);
        //有对应的退款记录
        if (entity.getCanReturn().equals(CAN_NOT_RETURN.type)) {
            Integer type = entity.getType();
            MeterConsumptionTrade otherTrade = getReturnTrade(entity.getBillId(), type);
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


    private MeterConsumptionTrade getReturnTrade(String billId, Integer type) {
        LambdaQueryChainWrapper<MeterConsumptionTrade> wrapper = this.lambdaQuery();
        wrapper.select(MeterConsumptionTrade::getId, MeterConsumptionTrade::getTradeNo, MeterConsumptionTrade::getCreateTime,
                MeterConsumptionTrade::getCreateBy, MeterConsumptionTrade::getReturnAmount)
                .eq(MeterConsumptionTrade::getBillId, billId).last("limit 1");
        if (PAY_ORDER.type.equals(type)) {
            wrapper.eq(MeterConsumptionTrade::getType, 2);
        }
        if (RETURN_ORDER.type.equals(type)) {
            wrapper.eq(MeterConsumptionTrade::getType, 1);
        }
        return wrapper.one();
    }


    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<MeterConsumptionTradeListVO> listMeterConsumptionTradePage(MeterConsumptionTradeQuery query) {
        LambdaQueryWrapper<MeterConsumptionTrade> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<MeterConsumptionTrade> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<MeterConsumptionTradeListVO> result = MyBeanUtils.copyList(entitiesToDTOs(list), MeterConsumptionTradeListVO::new);
        return query.createPageInfo(result);
    }

    @Override
    public void export(MeterConsumptionTradeQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)),
                sourceList -> MyBeanUtils.copyList(entitiesToDTOs(sourceList), MeterConsumptionTradeExcelExportDTO::new),
                RENT_TRADE_EXPORT_FILENAME, MeterConsumptionTradeExcelExportDTO.class);
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
        MeterConsumptionTrade entity = Optional.ofNullable(getById(dto.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        rentContractTradeService.judgeReturnAmount(entity.getReceivedAmount(), dto.getReturnAmount());
        //原纪录设置为不可退款
        this.lambdaUpdate()
                .eq(MeterConsumptionTrade::getId, dto.getId())
                .set(MeterConsumptionTrade::getCanReturn, CAN_NOT_RETURN.type)
                .set(MeterConsumptionTrade::getReturnAmount, dto.getReturnAmount())
                .set(MeterConsumptionTrade::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .update();
        RentContractFeeCopyTradeDTO copyTradeDTO = MyBeanUtils.copy(entity, RentContractFeeCopyTradeDTO::new);
        MeterConsumptionTrade trade = MyBeanUtils.copy(copyTradeDTO, MeterConsumptionTrade::new);
        BeanUtils.copyProperties(dto, trade);
        trade.setId(null);
        trade.setReturnAmount(dto.getReturnAmount());
        trade.setType(RETURN_ORDER.type);
        trade.setTradeNo(rentContractTradeService.getTradeNo());
        trade.setCanReturn(CAN_NOT_RETURN.type);
        trade.setReceivedAmount(dto.getReturnAmount());
        EntityUserUtil.setAll(trade);
        return this.save(trade);
    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeterConsumptionTrade> getConditionWrapper(MeterConsumptionTradeQuery query) {
        String parkOrgId = query.getParkOrgId();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(parkOrgId);
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<MeterConsumptionTrade> queryWrapper = new LambdaQueryWrapper<MeterConsumptionTrade>();
        String companyName = query.getCompanyName();
        LocalDateTime payTimeStart = query.getPayTimeStart();
        LocalDateTime payTimeEnd = query.getPayTimeEnd();
        queryWrapper.eq(MeterConsumptionTrade::getContractType, query.getContractType())
                .like(StringUtils.isNotBlank(companyName), MeterConsumptionTrade::getCompanyName, companyName)
                .like(StringUtils.isNotBlank(query.getBillNo()), MeterConsumptionTrade::getBillNo, query.getBillNo())
                .eq(Objects.nonNull(query.getType()), MeterConsumptionTrade::getType, query.getType())
                .in(MeterConsumptionTrade::getParkOrgId, parkOrgIds)
                .orderByDesc(MeterConsumptionTrade::getCreateTime);
        if (Objects.nonNull(payTimeStart) && Objects.nonNull(payTimeEnd)) {
            queryWrapper.ge(MeterConsumptionTrade::getCreateTime, payTimeStart)
                    .le(MeterConsumptionTrade::getCreateTime, payTimeEnd);
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeterConsumptionTradeDTO> entitiesToDTOs(List<MeterConsumptionTrade> list) {
        Set<String> billIds = list.parallelStream().map(MeterConsumptionTrade::getBillId).collect(Collectors.toSet());
        Map<String, MeterConsumptionBill> billData = getBillData(billIds);
        return MyBeanUtils.copyList(list, MeterConsumptionTradeDTO::new, (s, t) -> {
            MeterConsumptionBill meterConsumptionBill = billData.get(s.getBillId());
//            t.setReceivableAmount(meterConsumptionBill.getReceivableAmount());
//            t.setReceivedAmount(meterConsumptionBill.getReceivedAmount());
            t.setHouseIds(meterConsumptionBill.getHouseIds());
            t.setAccountPeriod(meterConsumptionBill.getAccountPeriod());
            t.setChargeName(meterConsumptionBill.getChargeName());
//            t.setEmployeeIds(meterConsumptionBill.getEmployeeIds());
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
    private Map<String, MeterConsumptionBill> getBillData(Set<String> billIds) {
        List<MeterConsumptionBill> list = meterConsumptionBillService.lambdaQuery()
                .select(MeterConsumptionBill::getId, MeterConsumptionBill::getReceivableAmount,
                        MeterConsumptionBill::getReceivedAmount, MeterConsumptionBill::getAccountPeriod,
                        MeterConsumptionBill::getChargeName, MeterConsumptionBill::getEmployeeIds,
                        MeterConsumptionBill::getHouseIds)
                .in(MeterConsumptionBill::getId, billIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.parallelStream().collect(Collectors.toMap(MeterConsumptionBill::getId, Function.identity()));
    }
}
