package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.dto.DetailListDTO;
import com.crlink.smartpark.contract.dto.RentContractLeaveDTO;
import com.crlink.smartpark.contract.entity.RentContractBill;
import com.crlink.smartpark.contract.entity.RentContractFee;
import com.crlink.smartpark.contract.entity.RentContractLeave;
import com.crlink.smartpark.contract.entity.RentLateFee;
import com.crlink.smartpark.contract.mapper.RentContractLeaveMapper;
import com.crlink.smartpark.contract.model.excel.RentContractLeaveExcelExportDTO;
import com.crlink.smartpark.contract.model.query.RentContractLeaveQuery;
import com.crlink.smartpark.contract.model.request.RentBillCheckoutReqDTO;
import com.crlink.smartpark.contract.model.request.RentContractLeaveReqDTO;
import com.crlink.smartpark.contract.model.request.SettlementPayReqDTO;
import com.crlink.smartpark.contract.model.vo.RentContractLeaveListVO;
import com.crlink.smartpark.contract.service.RentContractBillService;
import com.crlink.smartpark.contract.service.RentContractFeeService;
import com.crlink.smartpark.contract.service.RentContractLeaveService;
import com.crlink.smartpark.contract.service.RentLateFeeService;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import java.util.stream.Stream;

import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.*;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_LEAVE_EXPORT_FILENAME;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentCashStatusEnum.CLEARED;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentCashStatusEnum.PAID_UP;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeBillEnum.UNRECEIVED;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeOutEnum.BINDING;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RentFeeOutEnum.CANCELED;


/**
 * 租赁合同交易明细 服务实现类
 * */
@Service
public class RentContractLeaveServiceImpl extends ServiceImpl<RentContractLeaveMapper, RentContractLeave> implements RentContractLeaveService {

    @Autowired
    private RentContractBillService rentContractBillService;

    @Autowired
    private RentLateFeeService rentLateFeeService;

    @Autowired
    private RentContractFeeService rentContractFeeService;

    @Autowired
    private CommonProvider commonProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<RentContractLeave, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<RentContractLeave> lambdaQuery = this.lambdaQuery().in(RentContractLeave::getId, ids);
        if (flag) {
            lambdaQuery.select(RentContractLeave::getId, sValue);
        }
        Stream<RentContractLeave> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(RentContractLeave::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(RentContractLeave::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 离场结算-计算费用
     */
    @Override
    public RentContractLeaveDTO leaveSettlement(RentContractLeaveReqDTO reqDTO) {
        RentContractLeaveDTO result = MyBeanUtils.copy(reqDTO, RentContractLeaveDTO::new);
        String contractId = reqDTO.getContractId();
        List<DetailListDTO> detailList = getDetailList(null, contractId, true);
        result.setList(detailList);
        result.setDeposit(getDeposit(contractId, reqDTO.getContractType()));
        BigDecimal totalAmount = getTotalAmount(detailList);
        result.setTotalAmount(totalAmount);
        return result;
    }


    /**
     * 结算缴费
     *
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean settlementPay(SettlementPayReqDTO reqDTO) {
        RentContractLeaveDTO rentContractLeaveDTO = reqDTO.getRentContractLeaveDTO();
        if (rentContractLeaveDTO.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new ResolinkRuntimeException("300017");
        }
        LocalDate checkoutDate = reqDTO.getCheckoutDate();
        if (checkoutDate.isAfter(LocalDate.now())) {
            throw new ResolinkRuntimeException("300013");
        }
        RentContractLeave entity = MyBeanUtils.copy(rentContractLeaveDTO, RentContractLeave::new);
        String remark = reqDTO.getRemark();
        Set<String> billIds = rentContractLeaveDTO.getList().parallelStream()
                .map(DetailListDTO::getId).collect(Collectors.toSet());
        entity.setBillIds(billIds);
        entity.setRemark(remark);
        entity.setCheckoutDate(checkoutDate);
        entity.setId(null);
        EntityUserUtil.setAll(entity);
        //1、结算表插入数据
        this.save(entity);
        //2、未出费用改为已废除
        String contractId = rentContractLeaveDTO.getContractId();
        rentContractFeeService.lambdaUpdate()
                .set(RentContractFee::getBillingStatus, CANCELED.status)
                .eq(RentContractFee::getContractId, contractId)
                .eq(RentContractFee::getBillingStatus, BINDING.status)
                .update();
        //2、账单数据修改、类似与批量收款操作
//        Set<String> billIds = rentContractLeaveDTO.getList()
//                .parallelStream().map(DetailListDTO::getId).collect(Collectors.toSet());
        RentBillCheckoutReqDTO checkoutReqDTO = new RentBillCheckoutReqDTO();
        checkoutReqDTO.setIds(billIds);
        checkoutReqDTO.setPayType(reqDTO.getPayType());
        checkoutReqDTO.setRemark(remark);
        checkoutReqDTO.setCheckoutDate(checkoutDate);
        checkoutReqDTO.setFromLeave(true);
        return rentContractBillService.checkout(checkoutReqDTO);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public RentContractLeaveDTO getRentContractLeaveById(String id) {
        RentContractLeave entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        RentContractLeaveDTO result = MyBeanUtils.copy(entity, RentContractLeaveDTO::new);
        List<DetailListDTO> detailList = getDetailList(entity.getBillIds(), entity.getContractId(), false);
        result.setList(detailList);
        result.setTotalAmount(getTotalAmount(detailList));
        return result;
    }


    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<RentContractLeaveListVO> listRentContractLeavePage(RentContractLeaveQuery query) {
        LambdaQueryWrapper<RentContractLeave> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<RentContractLeave> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<RentContractLeaveListVO> result = convert(list);
        return query.createPageInfo(result);
    }

    @Override
    public void export(RentContractLeaveQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)), sourceList ->
                        MyBeanUtils.copyList(convert(sourceList), RentContractLeaveExcelExportDTO::new),
                RENT_LEAVE_EXPORT_FILENAME, RentContractLeaveExcelExportDTO.class);
    }

    private List<RentContractLeaveListVO> convert(List<RentContractLeave> list) {
        Set<String> userIds = list.parallelStream().map(RentContractLeave::getCreateBy).collect(Collectors.toSet());
        Map<String, String> names = commonProvider.getNamesByIdsAndType(userIds, SimpleNode.TypeEnum.USER.getCode());
        return MyBeanUtils.copyList(list, RentContractLeaveListVO::new, (s, t) -> t.setCreateByName(names.get(s.getCreateBy())));
    }

    /**
     * 获取待缴纳账单明细
     *
     * @param contractId 合同id
     * @return
     */
    private List<DetailListDTO> getDetailList(Set<String> billIds, String contractId, Boolean fromLeaveSettlement) {
        LambdaQueryChainWrapper<RentContractBill> wrapper = rentContractBillService.lambdaQuery()
                .select(RentContractBill::getId, RentContractBill::getBillNo, RentContractBill::getChargeName, RentContractBill::getReceivedAmount,
                        RentContractBill::getHouseName, RentContractBill::getEmployeeIds, RentContractBill::getStartDate,
                        RentContractBill::getEndDate, RentContractBill::getDueAmount, RentContractBill::getContractType)
                .eq(RentContractBill::getContractId, contractId)
                .orderByDesc(RentContractBill::getCreateTime);
        if (CollectionUtil.isEmpty(billIds)) {
            //1、查询未收的账单
            wrapper.eq(RentContractBill::getStatus, UNRECEIVED.status);
        } else {
            wrapper.in(RentContractBill::getId, billIds);
        }
        List<RentContractBill> billList = wrapper.list();
        if (CollectionUtil.isNotEmpty(billList)) {
            Map<String, RentContractBill> billMap = billList.parallelStream().collect(Collectors.toMap(RentContractBill::getId, Function.identity()));
            Set<String> employeeIds = Sets.newHashSet();
            billList.forEach(t -> {
                billMap.put(t.getId(), t);
//            if (t.getContractType().equals(APARTMENT_CONTRACT_1.getType())) {
                if (Objects.equals(APARTMENT_CONTRACT_1.getType(), t.getContractType())) {
                    //公寓合同时查询入驻人员名称
                    employeeIds.addAll(CollectionUtil.unionDistinct(employeeIds, t.getEmployeeIds()));
                }
            });
            List<DetailListDTO> detailList = new LinkedList<>();
            Map<String, String> employeeNames = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
            billMap.forEach((k, v) -> {
                DetailListDTO dto = new DetailListDTO();
                dto.setId(k);
                dto.setChargeName(v.getChargeName());
                dto.setHouseName(v.getHouseName());
                dto.setStartDate(v.getStartDate());
                dto.setEndDate(v.getEndDate());
                if (fromLeaveSettlement) {
                    dto.setDueAmount(v.getDueAmount());
                } else {
                    dto.setDueAmount(v.getReceivedAmount());
                }

                List<String> currentEmployeeIds = v.getEmployeeIds();
//                if (APARTMENT_CONTRACT_1.getType() == v.getContractType() && CollectionUtil.isNotEmpty(currentEmployeeIds)) {
                if (Objects.equals(APARTMENT_CONTRACT_1.getType(), v.getContractType()) && CollectionUtil.isNotEmpty(currentEmployeeIds)) {
                    //公寓合同时返回入驻人员名称
                    dto.setEmployeeIds(currentEmployeeIds);
                    List<String> names = Lists.newArrayListWithCapacity(currentEmployeeIds.size());
                    currentEmployeeIds.forEach(id -> names.add(employeeNames.get(id)));
                    dto.setEmployeeIdNames(names);
                }
                //2、查询待收滞纳金
                getReceiveFine(billMap.keySet(), dto, k, fromLeaveSettlement);
                detailList.add(dto);
            });
            return detailList;
        }
        return Collections.emptyList();
    }

    /**
     * 获取待收滞纳金
     */
    private void getReceiveFine(Set<String> billIds, DetailListDTO dto, String currentBillId, Boolean fromLeaveSettlement) {
        Map<String, RentLateFee> lateFeeMap = rentLateFeeService.lambdaQuery()
                .select(RentLateFee::getRentBillId, RentLateFee::getReceiveFine, RentLateFee::getOverdueFine)
                .in(RentLateFee::getRentBillId, billIds).list()
                .parallelStream().collect(Collectors.toMap(
                        RentLateFee::getRentBillId, Function.identity()));
        if (CollectionUtil.isNotEmpty(lateFeeMap)) {
            RentLateFee rentLateFee = lateFeeMap.get(currentBillId);
            if (fromLeaveSettlement) {
                dto.setReceiveFine(rentLateFee == null ? BigDecimal.ZERO : rentLateFee.getReceiveFine());
            } else {
                dto.setReceiveFine(rentLateFee == null ? BigDecimal.ZERO : rentLateFee.getOverdueFine());
            }
        } else {
            dto.setReceiveFine(BigDecimal.ZERO);
        }
    }

    /**
     * 获取押金
     *
     * @param contractId 合同id
     * @return
     */
    private BigDecimal getDeposit(String contractId, Integer contractType) {
        if (contractType.equals(PROPERTY_CONTRACT.getType())) {
            return BigDecimal.ZERO;
        }
        final LambdaQueryChainWrapper<RentContractFee> wrapper = rentContractFeeService.lambdaQuery();
        wrapper.select(RentContractFee::getReceivedAmount, RentContractFee::getRefundedAmount)
                .eq(RentContractFee::getContractId, contractId)
                .eq(RentContractFee::getContractType, contractType)
                .in(RentContractFee::getCashStatus, PAID_UP.status, CLEARED.status);
        if (contractType.equals(RENTAL_CONTRACT_2.getType())) {
            wrapper.in(RentContractFee::getFeeType, RENTAL_CONTRACT_2.getCalculationValues());
        }
        if (contractType.equals(APARTMENT_CONTRACT_2.getType())) {
            wrapper.in(RentContractFee::getFeeType, APARTMENT_CONTRACT_2.getCalculationValues());
        }
        List<RentContractFee> list = wrapper.list();
        if (CollectionUtil.isNotEmpty(list)) {
//            list.stream().map(RentContractFee::getReceivedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            //v17 押金=已收押金-已退款押金
            return list.stream().map(t -> t.getReceivedAmount().subtract(t.getRefundedAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getTotalAmount(List<DetailListDTO> list) {
        if (CollectionUtil.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        return list.stream()
                .map(t -> t.getDueAmount().add(t.getReceiveFine()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<RentContractLeave> getConditionWrapper(RentContractLeaveQuery query) {
        String parkOrgId = query.getParkOrgId();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(parkOrgId);
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        String companyName = query.getCompanyName();
        LambdaQueryWrapper<RentContractLeave> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentContractLeave::getContractType, query.getContractType())
                .like(StringUtils.isNotBlank(companyName), RentContractLeave::getCompanyName, companyName)
                .in(RentContractLeave::getParkOrgId, parkOrgIds)
                .orderByDesc(RentContractLeave::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<RentContractLeaveListVO> entitiesToDTOs(List<RentContractLeave> entities) {
        return MyBeanUtils.copyList(entities, RentContractLeaveListVO::new, (s, t) -> {
        });
    }
}
