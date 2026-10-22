package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.contract.common.ContractConstants;
import com.crlink.smartpark.contract.dto.*;
import com.crlink.smartpark.contract.entity.Contract;
import com.crlink.smartpark.contract.entity.ContractBillingPlan;
import com.crlink.smartpark.contract.entity.HouseRelation;
import com.crlink.smartpark.contract.mapper.ContractMapper;
import com.crlink.smartpark.contract.model.excel.*;
import com.crlink.smartpark.contract.model.query.ContractListQuery;
import com.crlink.smartpark.contract.model.query.ContractRentedHouseQuery;
import com.crlink.smartpark.contract.model.request.AuditReqDto;
import com.crlink.smartpark.contract.model.request.ContractProcessRecordReqDTO;
import com.crlink.smartpark.contract.model.request.ContractReqDTO;
import com.crlink.smartpark.contract.model.request.WithdrawReqDto;
import com.crlink.smartpark.contract.service.*;
import com.crlink.smartpark.contract.task.ContractStateTask;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.dto.BillingSchemeQuery;
import com.crlink.smartpark.fee.feign.FeeProvider;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.ContractState.*;
import static com.crlink.smartpark.common.constants.DictConstant.ContractType.APARTMENT_CONTRACT;
import static com.crlink.smartpark.common.constants.DictConstant.ContractType.RENTAL_CONTRACT;
import static com.crlink.smartpark.contract.common.ContractConstants.HouseRelationType.CONTRACT;
import static com.crlink.smartpark.contract.common.ContractConstants.HouseRelationType.FEE_BILLING;

/**
 * <p>
 * 合同 服务实现类
 * </p>
 * */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Autowired
    private ContractBillingPlanService contractBillingPlanService;
    @Autowired
    private HouseRelationService houseRelationService;
    @Autowired
    private ContractProcessRecordService contractProcessRecordService;
    @Autowired
    private FeeProvider feeProvider;
    @Autowired
    private EmployeeProvider employeeProvider;
    @Autowired
    private CommonProvider commonProvider;
    @Autowired
    private RentContractFeeService rentContractFeeService;

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Contract, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(Contract::getId, sValue)
                .in(Contract::getId, ids).list().stream()
                .collect(HashMap::new, (map, entity) -> map.put(entity.getId(), sValue.apply(entity)), HashMap::putAll);
        return result;
    }

    @Override
    public Boolean checkNoConflict(String no, String id) {
        return this.count(Wrappers.<Contract>query().lambda().eq(Contract::getNo, no).ne(StringUtils.isNotBlank(id),
                Contract::getId, id)) > 0;
    }

    @Override
    @Transactional
    public Boolean saveOrUpdateContract(ContractReqDTO reqDTO) {
        AssertUtil.requireTrue(!checkNoConflict(reqDTO.getNo(), reqDTO.getId()), "300001");
        AssertUtil.requireTrue(!checkHouseRelationConflict(reqDTO), "300002");

        Contract entity = MyBeanUtils.copy(reqDTO, Contract::new);
        EntityUserUtil.setAllDynamic(entity);
        if (StringUtils.isBlank(reqDTO.getId())) {
            entity.setProcessNo(ContractConstants.PROCESS_NO_START);
            entity.setState(DictConstant.ContractState.TO_BE_SUBMITTED.getCode());
            this.save(entity);
            reqDTO.setId(entity.getId());
        } else {
            this.updateById(entity);
            // 先删房屋关联相关再删计费信息
            Map<String, Set<String>> contractId2PlanIdMap = (Map<String, Set<String>>) contractBillingPlanService
                    .contractId2ValueMap(Sets.newHashSet(reqDTO.getId()), ContractBillingPlan::getId);
            Set<String> pkIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(contractId2PlanIdMap.get(reqDTO.getId()))) {
                pkIds = contractId2PlanIdMap.get(reqDTO.getId());
            }

            pkIds.add(reqDTO.getId());
            houseRelationService.remove(Wrappers.<HouseRelation>lambdaQuery().in(HouseRelation::getPkId, pkIds));
            contractBillingPlanService.remove(
                    Wrappers.<ContractBillingPlan>lambdaQuery().eq(ContractBillingPlan::getContractId, reqDTO.getId()));
        }

        List<HouseRelation> houseRelations = Lists.newArrayList();
        Set<String> houseIds =
                reqDTO.getHouseMessages().stream().map(HouseMessage::getHouseId).collect(Collectors.toSet());
        houseIds.forEach(houseId -> houseRelations.add(new HouseRelation(reqDTO.getId(), houseId, CONTRACT.value)));
        if (CollectionUtil.isNotEmpty(reqDTO.getContractBillingPlanReqDTOS())) {
            Map<String, Set<String>> billingIdHouseIdMap = Maps.newHashMap();
            reqDTO.getContractBillingPlanReqDTOS().forEach(billingPlan -> {
                ContractBillingPlan contractBillingPlan = MyBeanUtils.copy(billingPlan, ContractBillingPlan::new);
                contractBillingPlan.setContractId(reqDTO.getId());
                contractBillingPlanService.save(contractBillingPlan);
                billingIdHouseIdMap.put(contractBillingPlan.getId(), billingPlan.getHouseIds());
            });
            billingIdHouseIdMap.forEach(
                    (key, value) -> value.forEach(e -> houseRelations.add(new HouseRelation(key, e, FEE_BILLING.value))));
        }

        return houseRelationService.saveBatch(houseRelations);
    }

    private Boolean checkHouseRelationConflict(ContractReqDTO contractReqDTO) {
        ContractRentedHouseQuery contractRentedHouseQuery =
                MyBeanUtils.copy(contractReqDTO, ContractRentedHouseQuery::new);
        contractRentedHouseQuery
                .setNoIds(StringUtils.isNotBlank(contractReqDTO.getId()) ? Sets.newHashSet(contractReqDTO.getId()) : null);
        Set<String> rentedHouseIds = this.getUnavailableHouse(contractRentedHouseQuery);
        Set<String> houseIds =
                contractReqDTO.getHouseMessages().stream().map(HouseMessage::getHouseId).collect(Collectors.toSet());
        return CollectionUtil.containsAny(rentedHouseIds, houseIds);
    }

    @Override
    @Transactional
    public Boolean changeContract(ContractReqDTO dto) {
        AssertUtil.requireTrue(TO_BE_EFFECTIVE.getCode() == dto.getState()
                || IN_EFFECT.getCode() == dto.getState(), "300003");
        String processNo = getProcessNo(dto.getType());
        dto.setState(DictConstant.ContractState.TO_BE_SUBMITTED.getCode());
        dto.setAttributes(dto.getState() == DictConstant.ContractType.ADMISSION_AGREEMENT.getCode()
                ? DictConstant.ProtocolAttributes.CHANGE_PROTOCOL.getCode()
                : DictConstant.ContractAttributes.CHANGE_CONTRACT.getCode());
        dto.setProcessNo(processNo);
        saveOrUpdateContract(dto);
        return operateRecord(dto.getId(), DictConstant.ContractOperate.CHANGE.getCode(), dto.getProcessNo(), processNo,
                dto.getState(), DictConstant.ContractState.TO_BE_SUBMITTED.getCode(), "", dto.getExtension());
    }

    @Override
    @Transactional
    public Boolean extendContract(ContractReqDTO dto) {
        AssertUtil.requireTrue(DictConstant.ContractState.EXPIRED.getCode() == dto.getState()
                || IN_EFFECT.getCode() == dto.getState(), "300007");
        dto.setState(DictConstant.ContractState.TO_BE_SUBMITTED.getCode());
        dto.setAttributes(dto.getState() == DictConstant.ContractType.ADMISSION_AGREEMENT.getCode()
                ? DictConstant.ProtocolAttributes.RENEW_PROTOCOL.getCode()
                : DictConstant.ContractAttributes.RENEW_CONTRACT.getCode());
        String oldProcessNo = dto.getProcessNo();
        String processNo = getProcessNo(dto.getType());
        dto.setProcessNo(processNo);
        saveOrUpdateContract(dto);
        return operateRecord(dto.getId(), DictConstant.ContractOperate.CHANGE.getCode(), oldProcessNo,
                processNo, dto.getState(), DictConstant.ContractState.TO_BE_SUBMITTED.getCode(), "",
                dto.getExtension());
    }

    @Override
    @Transactional
    public boolean removeById(Serializable id) {
        Contract contract = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(DictConstant.ContractState.ABOLISHED.getCode() == contract.getState(), "300006");
        operateRecord(contract.getId(), DictConstant.ContractOperate.DELETE.getCode(), contract.getProcessNo(),
                getProcessNo(contract.getType()), contract.getState(), contract.getState(), "", null);
        contract.setUpdateTime(LocalDateTime.now());
        EntityUserUtil.setUpdateBy(contract);
        return super.removeById(id);
    }

    private Boolean operate(int operate, String contractId, int afterState, Extension extension) {
        Contract contract =
                Optional.ofNullable(getById(contractId)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return operate(operate, contract, afterState, extension);
    }

    private Boolean operate(int operate, Contract contract, int afterState, Extension extension) {
        String processNo = getProcessNo(contract.getType());
        operateRecord(contract.getId(), operate, contract.getProcessNo(), processNo, contract.getState(), afterState,
                "", extension);

        contract.setState(afterState);
        contract.setUpdateTime(LocalDateTime.now());
        contract.setProcessNo(processNo);
        EntityUserUtil.setUpdateBy(contract);
        return updateById(contract);
    }

    @Override
    @Transactional
    public Boolean nullifyById(String id) {
        Contract contract = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(DictConstant.ContractState.TO_BE_SUBMITTED.getCode() == contract.getState(), "300005");
        return operate(DictConstant.ContractOperate.NULLIFY.getCode(), id,
                DictConstant.ContractState.ABOLISHED.getCode(), null);
    }

    @Override
    @Transactional
    public Boolean terminationById(String id) {
        Contract contract = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(DictConstant.ContractState.EXPIRED.getCode() == contract.getState(), "300005");
        contract.setAttributes(contract.getState() == DictConstant.ContractType.ADMISSION_AGREEMENT.getCode()
                ? DictConstant.ProtocolAttributes.HISTORICAL_PROTOCOL.getCode()
                : DictConstant.ContractAttributes.HISTORICAL_CONTRACT.getCode());
        return operate(DictConstant.ContractOperate.TERMINATION.getCode(), contract,
                DictConstant.ContractState.INVALIDATION.getCode(), null);
    }

    @Override
    @Transactional
    public Boolean withdraw(WithdrawReqDto withdrawReqDto) {
        Contract contract = Optional.ofNullable(getById(withdrawReqDto.getId()))
                .orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(TO_BE_EFFECTIVE.getCode() == contract.getState()
                || IN_EFFECT.getCode() == contract.getState(), "300003");
        contract.setWithdrawDate(withdrawReqDto.getWithdrawDate());
        contract.setWithdrawReason(withdrawReqDto.getWithdrawReason());
        return operate(DictConstant.ContractOperate.WITHDRAW.getCode(), contract,
                DictConstant.ContractState.UNDER_REVIEW_WITHDRAW.getCode(), null);
    }

    @Override
    @Transactional
    public Boolean submitAudit(String id) {
        Contract contract = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(DictConstant.ContractState.TO_BE_SUBMITTED.getCode() == contract.getState(), "300005");
        return operate(DictConstant.ContractOperate.INITIATE_REVIEW.getCode(), contract,
                DictConstant.ContractState.UNDER_REVIEW.getCode(), null);
    }

    @Override
    @Transactional
    public Boolean audit(AuditReqDto auditReqDto) {
        Contract contract =
                Optional.ofNullable(getById(auditReqDto.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(DictConstant.ContractState.UNDER_REVIEW.getCode() == contract.getState()
                || DictConstant.ContractState.UNDER_REVIEW_WITHDRAW.getCode() == contract.getState(), "300004");
        Extension extension = MyBeanUtils.copy(auditReqDto, Extension::new);
        Integer afterState = contract.getState();

        if (DictConstant.ContractState.UNDER_REVIEW.getCode() == contract.getState()) {
            if (DictConstant.AuditResult.PASS.getCode() == auditReqDto.getAuditResult()) {
                afterState = ContractStateTask.betweenDateStartAndEnd(contract.getStartDate(), contract.getEndDate());
                if (contract.getType().equals(ContractConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT.getType())
                        || contract.getType().equals(ContractConstants.ContractTypeToSchemeFeeType.PROPERTY_CONTRACT.getType())) {
                    rentContractFeeService.createRentContractFee(contract);
                }
                if (contract.getType().equals(ContractConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT.getType())) {
                    rentContractFeeService.createApartContractFee(contract);
                }
            } else {
                afterState = DictConstant.ContractState.TO_BE_SUBMITTED.getCode();
            }
        } else if (DictConstant.ContractState.UNDER_REVIEW_WITHDRAW.getCode() == contract.getState()) {
            if (DictConstant.AuditResult.PASS.getCode() == auditReqDto.getAuditResult()) {
                if (LocalDate.now().isAfter(contract.getWithdrawDate()) || LocalDate.now().equals(contract.getWithdrawDate())) {
                    afterState = DictConstant.ContractState.WITHDRAWN.getCode();
                } else {
                    afterState = DictConstant.ContractState.IN_EFFECT.getCode();
                }
                contract.setAttributes(contract.getState() == DictConstant.ContractType.ADMISSION_AGREEMENT.getCode()
                        ? DictConstant.ProtocolAttributes.WITHDRAWAL_PROTOCOL.getCode()
                        : DictConstant.ContractAttributes.WITHDRAWAL_CONTRACT.getCode());
            } else {
                afterState = ContractStateTask.betweenDateStartAndEnd(contract.getStartDate(), contract.getEndDate());
            }
        }

        return operate(DictConstant.ContractOperate.AUDIT.getCode(), contract, afterState, extension);
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public ContractDTO getContractById(String id) {
        Contract entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        ContractDTO dto = MyBeanUtils.copy(entity, ContractDTO::new);
        Map<String, List<ContractBillingPlan>> contactId2PlanIdMap = (Map<String,
                List<ContractBillingPlan>>) contractBillingPlanService.contractId2ValueMap(Sets.newHashSet(id), null);
        if (CollectionUtil.isNotEmpty(contactId2PlanIdMap)) {
            Collection<String> pkIds = contactId2PlanIdMap.get(id).parallelStream().map(ContractBillingPlan::getId)
                    .collect(Collectors.toSet());
            Set<String> schemeIds = contactId2PlanIdMap.get(id).parallelStream()
                    .map(ContractBillingPlan::getFeeBillingSchemeId).collect(Collectors.toSet());
            // Map<String, SchemeItemDTO> schemeAndItemNamesBySchemeIds =
            // feeProvider.getSchemeNameAndItemBySchemeIds(schemeIds);
            Map<String, List<String>> plan2HouseIdsMap =
                    houseRelationService.pkId2HouseRelationsMap(pkIds, FEE_BILLING.value);
            List<ContractBillingPlanDTO> contractBillingPlanDTOS =
                    contactId2PlanIdMap.get(id).stream().map(contractBillingPlan -> {
                        ContractBillingPlanDTO contractBillingPlanDTO =
                                MyBeanUtils.copy(contractBillingPlan, ContractBillingPlanDTO::new);
                        contractBillingPlanDTO.setHouseIds(plan2HouseIdsMap.get(contractBillingPlanDTO.getId()));
                        // SchemeItemDTO schemeItemDTO =
                        // schemeAndItemNamesBySchemeIds.get(contractBillingPlan.getFeeBillingSchemeId());
                        // contractBillingPlanDTO.setFeeBillingSchemeName(schemeItemDTO.getSchemeName());
                        // contractBillingPlanDTO.setChargeItemName(schemeItemDTO.getItemName());
                        // contractBillingPlanDTO.setTaxRate(schemeItemDTO.getTaxRate());
                        return contractBillingPlanDTO;
                    }).collect(Collectors.toList());
            dto.setContractBillingPlanDTOS(contractBillingPlanDTOS);
        }
        // Map<String, List<String>> contractId2HouseIdsMap =
        // houseRelationService.pkId2HouseRelationsMap(Sets.newHashSet(id), CONTRACT.value);
        // dto.setHouseVOS(CollectionUtil.newArrayList(houseProvider.getHouseDetailByHouseId(Sets.newHashSet(contractId2HouseIdsMap.get(id))).values()));
        if (StringUtils.isNotBlank(dto.getEmployeeId())) {
            String[] employeeIds = dto.getEmployeeId().split(",");
            Map<String, String> namesByIdsAndType = commonProvider.getNamesByIdsAndType(Sets.newHashSet(employeeIds),
                    SimpleNode.TypeEnum.EMPLOYEE.getCode());
            List<DictVO> collect = namesByIdsAndType.entrySet().stream().map(e -> new DictVO(e.getValue(), e.getKey()))
                    .collect(Collectors.toList());
            dto.setEmployeeDicts(collect);
        }
        return dto;
    }

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<ContractDTO> listPage(ContractListQuery condition) {
        List<Contract> list = getWrapper(condition).list();
        MyPageInfoUtils.isEmptyPage(list);
        List<ContractDTO> dtoList = convert(list);
        return new PageInfo<>(dtoList);
    }

    private List<ContractDTO> convert(List<Contract> contracts) {
        List<ContractDTO> contractDTOS = MyBeanUtils.copyList(contracts, ContractDTO::new, (s, t) -> {
        });
        return contractDTOS;
    }

    private List<ContractExportDTO> convertToExcel(List<Contract> contracts) {

        Set<String> employeeIdStrings = new HashSet<>();
        contracts.forEach(e -> {
            if (StringUtils.isNotBlank(e.getEmployeeId())) {
                List<String> employeList = Arrays.asList(e.getEmployeeId().split(","));
                employeeIdStrings.addAll(employeList);
            }
        });
        Map<String, EmployeeDTO> employeeId2EmployeeDTOsMap =
                employeeProvider.orderRecordQueryEmployeeColumns(employeeIdStrings);

        List<ContractExportDTO> contractDTOS = MyBeanUtils.copyList(contracts, ContractExportDTO::new, (s, t) -> {
            if (s.getExtension() != null) {
                BeanUtils.copyProperties(s.getExtension(), t);
            }

            // 处理其他数据
            if (s.getHouseMessages() != null) {
                t.setHouseMessagesStr(getHouseStr(s.getHouseMessages()));
            }
            // 经办人
            if (StringUtils.isNotBlank(s.getEmployeeId())) {
                List<String> employeList = Arrays.asList(s.getEmployeeId().split(","));
                Set<String> list = new HashSet<>();
                employeList.stream().forEach(e -> {
                    EmployeeDTO employeeDTO = employeeId2EmployeeDTOsMap.get(e);
                    if (Objects.nonNull(employeeDTO)) {
                        list.add(employeeDTO.getName());
                    }
                });
                if (list.size() > 0) {
                    t.setEmployeeId(StringUtils.join(list.toArray(), ";"));
                }
            }
        });
        return contractDTOS;
    }

    @Override
    public void export(ContractListQuery condition) {
        LambdaQueryChainWrapper<Contract> wrapper = getWrapper(condition);

        // ExcelExportUtils.export(wrapper::list, this::convertToExcel, ContractConstants.FILE_NAME_CONTRACT,
        // ContractExcelDTO.class);

        List<ContractExportDTO> list = convertToExcel(wrapper.list());
        DictConstant.ContractType contractTypeByCode =
                DictConstant.ContractType.getContractTypeByCode(condition.getType());
        switch (contractTypeByCode) {
            case ADMISSION_AGREEMENT:
                ExcelExportUtils.export(MyBeanUtils.copyList(list, AdmissionAgreementExcelExportDTO::new),
                        DictConstant.ContractType.ADMISSION_AGREEMENT.getLabel(), AdmissionAgreementExcelExportDTO.class,
                        null);
            case INVITATION_BID:
                ExcelExportUtils.export(MyBeanUtils.copyList(list, InvitationBidExcelExportDTO::new),
                        DictConstant.ContractType.INVITATION_BID.getLabel(), InvitationBidExcelExportDTO.class, null);
            case RENTAL_CONTRACT:
                ExcelExportUtils.export(MyBeanUtils.copyList(list, RentalContractExcelExportDTO::new),
                        RENTAL_CONTRACT.getLabel(), RentalContractExcelExportDTO.class, null);
            case PROPERTY_CONTRACT:
                ExcelExportUtils.export(MyBeanUtils.copyList(list, PropertyContractExcelExportDTO::new),
                        DictConstant.ContractType.PROPERTY_CONTRACT.getLabel(), PropertyContractExcelExportDTO.class, null);
            case APARTMENT_CONTRACT:
                ExcelExportUtils.export(MyBeanUtils.copyList(list, ApartmentContractExcelExportDTO::new),
                        APARTMENT_CONTRACT.getLabel(), ApartmentContractExcelExportDTO.class,
                        null);
            default:
                return;
        }
    }

    private LambdaQueryChainWrapper<Contract> getWrapper(ContractListQuery condition) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        return this.lambdaQuery().in(Contract::getParkOrgId, parkOrgIds)
                .eq(StringUtils.isNotBlank(condition.getParkOrgId()), Contract::getParkOrgId, condition.getParkOrgId())
                .eq(Objects.nonNull(condition.getState()), Contract::getState, condition.getState())
                .eq(Objects.nonNull(condition.getType()), Contract::getType, condition.getType())
                .eq(Objects.nonNull(condition.getNo()), Contract::getNo, condition.getNo())
                .like(StringUtils.isNotBlank(condition.getName()), Contract::getName, condition.getName())
                .like(StringUtils.isNotBlank(condition.getNameB()), Contract::getNameB, condition.getNameB())
                .and(Objects.nonNull(condition.getStartDate()) && Objects.nonNull(condition.getEndDate()),
                        condition1 -> condition1.le(Contract::getStartDate, condition.getEndDate())
                                .and(condition2 -> condition2.ge(Contract::getEndDate, condition.getStartDate())))
                .apply(StringUtils.isNotBlank(condition.getHouseId()),
                        "JSON_CONTAINS(house_messages, JSON_OBJECT('houseId',\"" + condition.getHouseId() + "\"))")
                .apply(StringUtils.isNotBlank(condition.getBuildingName()),
                        "JSON_CONTAINS(house_messages, JSON_OBJECT('buildingName',\"" + condition.getBuildingName() + "\"))")
                .orderByDesc(Contract::getUpdateTime);
    }

    /***
     * entity转DTO
     *
     * @param list entity集合
     * @return DTO集合
     */
    private List<ContractDTO> listEntityToDto(List<Contract> list) {
        return list.stream().map(entity -> {
            ContractDTO temp = new ContractDTO();
            BeanUtils.copyProperties(entity, temp);
            return temp;
        }).collect(Collectors.toList());
    }

    /**
     * 符合条件 1、合同状态为待提交、审核中、待生效、生效中 2、合同日期与前端传来的时间区间有交集
     *
     * @param contractRentedHouseQuery
     * @return
     */
    @Override
    public Set<String> getUnavailableHouse(ContractRentedHouseQuery contractRentedHouseQuery) {
        Set<String> contractIds = this.lambdaQuery().select(Contract::getId)
                .eq(Contract::getType, contractRentedHouseQuery.getType())
                .in(Contract::getState, DictConstant.ContractState.TO_BE_SUBMITTED.getCode(),
                        DictConstant.ContractState.UNDER_REVIEW.getCode(), TO_BE_EFFECTIVE.getCode(),
                        IN_EFFECT.getCode(), UNDER_REVIEW_WITHDRAW.getCode())
                .in(Contract::getParkOrgId, contractRentedHouseQuery.getParkOrgId())
                .and(condition1 -> condition1.le(Contract::getStartDate, contractRentedHouseQuery.getStartDate())
                        .ge(Contract::getEndDate, contractRentedHouseQuery.getStartDate())
                        .or(condition2 -> condition2.le(Contract::getStartDate, contractRentedHouseQuery.getEndDate())
                                .ge(Contract::getEndDate, contractRentedHouseQuery.getEndDate()))
                        .or(condition3 -> condition3.le(Contract::getStartDate, contractRentedHouseQuery.getStartDate())
                                .ge(Contract::getEndDate, contractRentedHouseQuery.getEndDate()))
                        .or(condition4 -> condition4.ge(Contract::getStartDate, contractRentedHouseQuery.getStartDate())
                                .le(Contract::getEndDate, contractRentedHouseQuery.getEndDate())))
                .notIn(CollectionUtils.isNotEmpty(contractRentedHouseQuery.getNoIds()), Contract::getId,
                        contractRentedHouseQuery.getNoIds())
                .list().stream().map(Contract::getId).collect(Collectors.toSet());
        Set<String> rentedHouseIds = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(contractIds)) {
            rentedHouseIds.addAll(houseRelationService.lambdaQuery().select(HouseRelation::getHouseId)
                    .in(HouseRelation::getPkId, contractIds).eq(HouseRelation::getType, CONTRACT.value).list().stream()
                    .map(HouseRelation::getHouseId).collect(Collectors.toSet()));
        }
        return rentedHouseIds;
    }

    @Override
    public String getNoByType(int type) {
        ContractConstants.ContractNoType contractNoTypeByType =
                ContractConstants.ContractNoType.getContractNoTypeByType(type);
        int count = contractNoTypeByType.getAtomicInteger().addAndGet(1);
        return String.format(contractNoTypeByType.getNoFormat(), LocalDate.now().getYear(), count);
    }

    private String getProcessNo(int type) {
        ContractConstants.ContractNoType contractNoTypeByType =
                ContractConstants.ContractNoType.getContractNoTypeByType(type);
        return contractNoTypeByType.getProcessNoPrefix() + System.currentTimeMillis();
    }

    private boolean operateRecord(String contractId, Integer operate, String preProcessNo, String processNo,
                                  Integer preState, Integer state, String remark, Extension extension) {
        ContractProcessRecordReqDTO record = new ContractProcessRecordReqDTO(contractId, operate, preProcessNo,
                processNo, preState, state, remark, extension);
        return contractProcessRecordService.saveOrUpdateContractProcessRecord(record);
    }

    private String getHouseStr(List<HouseMessage> houseMessages) {

        Map<String, List<HouseMessage>> map =
                houseMessages.stream().collect(Collectors.groupingBy(HouseMessage::getBuildingName));
        List<String> listReturns = new ArrayList<>();
        map.forEach((k, v) -> {
            Set<String> list = v.parallelStream().map(HouseMessage::getHouseName).collect(Collectors.toSet());
            listReturns.add(k + " " + StringUtils.join(list.toArray(), "、"));
        });

        return StringUtils.join(listReturns.toArray(), ";");
    }

    @Override
    public List<DictVO> getCalculationListByType(int type) {
        if (APARTMENT_CONTRACT.getCode() == type) {
            List<DictVO> data1 = feeProvider.getCalculationListByFeeType(DictUtil.getValue(DictConstant.FeeType.APARTMENT_RENT_SINGLE)).getData();
            List<DictVO> data2 = feeProvider.getCalculationListByFeeType(DictUtil.getValue(DictConstant.FeeType.DEPOSIT_SINGLE)).getData();
            data1.addAll(data2);
            return data1.stream().distinct().collect(Collectors.toList());
        }
        int feeType = 0;
        if (RENTAL_CONTRACT.getCode() == type) {
            feeType = 1;
        } else if (DictConstant.ContractType.PROPERTY_CONTRACT.getCode() == type) {
            feeType = 4;
        }
        return feeProvider.getCalculationListByFeeType(feeType).getData();
    }

    /**
     * 费用模块离场结算时根据类型、园区、公司获取合合同下拉列表
     * 合同状态为“待生效、生效中”中
     *
     * @param type      1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同
     * @param parkOrgId 园区id
     * @param companyId 乙方公司id
     * @return
     */
    @Override
    public List<DictVO> getDictListByType(Integer type, String parkOrgId, String companyId) {
        List<DictVO> result = this.lambdaQuery().select(Contract::getId, Contract::getNo)
                .eq(Contract::getType, type)
                .eq(StringUtils.isNotBlank(parkOrgId), Contract::getParkOrgId, parkOrgId)
                .eq(StringUtils.isNotBlank(companyId), Contract::getCustomerIdB, companyId)
                .and(wrapper -> wrapper.eq(Contract::getState, TO_BE_EFFECTIVE.getCode()).or().eq(Contract::getState, IN_EFFECT.getCode()))
                .list().parallelStream()
                .map(t -> new DictVO(t.getNo(), t.getId())).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<DictVO> getHouseListByContract(String contractIds) {
        Map<String, List<HouseMessage>> map = id2ValueMap(Sets.newHashSet(contractIds), Contract::getHouseMessages);
        if (CollectionUtil.isEmpty(map)) {
            return Collections.emptyList();
        }
        List<DictVO> result = new LinkedList<>();
        map.forEach((k, v) -> v.forEach(houseMessage -> {
            String houseName = houseMessage.getBuildingName() + houseMessage.getHouseName();
            DictVO dict = new DictVO(houseName, houseMessage.getHouseId());
            result.add(dict);
        }));
        return result;
    }

    @Override
    public PageInfo<BillingSchemeDTO> listBillingSchemePage(ContractListQuery contractListQuery) {
        BillingSchemeQuery query = MyBeanUtils.copy(contractListQuery, BillingSchemeQuery::new);
        Set<Integer> feeTypes = ContractConstants.ContractTypeToSchemeFeeType.getFeeTypeByType(contractListQuery.getType());
        query.setFeeTypes(feeTypes);
        return feeProvider.listBillingSchemePage(query).getData();
    }

    @Override
    public Set<String> getEffectiveHouseIds() {
        //return baseMapper.queryEffectiveContractHouseIds();
        List<Contract> contracts = this.lambdaQuery().select(Contract::getId, Contract::getHouseMessages).
                eq(Contract::getState, IN_EFFECT.getCode()).list();
        Set<String> houseIds = contracts.parallelStream().flatMap(t -> t.getHouseMessages().stream()).map(HouseMessage::getHouseId).
                collect(Collectors.toSet());
        return houseIds;
    }

    @Override
    public List<ContractDTO> getEffectiveContracts() {
        List<Contract> contracts = this.lambdaQuery().select(Contract::getId, Contract::getParkOrgId, Contract::getParkOrgName,
                Contract::getCustomerIdB, Contract::getNameB, Contract::getHouseMessages, Contract::getType)
                .in(Contract::getType, RENTAL_CONTRACT.getCode(), APARTMENT_CONTRACT.getCode())
                .eq(Contract::getState, IN_EFFECT.getCode()).list();
        List<ContractDTO> result = MyBeanUtils.copyList(contracts, ContractDTO::new);
        return result;
    }

    @Override
    public Map<String, ContractHouseStatusDTO> getContractHouseRelationFor3D(Collection<String> houseIds) {
        Map<String, ContractHouseStatusDTO> result = Maps.newHashMap();
        if (CollUtil.isEmpty(houseIds)) {
            return result;
        }
        List<HouseRelation> houseRelations = houseRelationService.lambdaQuery().select(HouseRelation::getPkId, HouseRelation::getHouseId)
                .eq(HouseRelation::getType, CONTRACT.value).in(HouseRelation::getHouseId, houseIds).list();
        Map<String, Set<String>> houseId2ContractIdsMap = houseRelations.parallelStream().collect(Collectors.groupingBy(HouseRelation::getHouseId,
                Collectors.mapping(HouseRelation::getPkId, Collectors.toSet())));
        for (String houseId : houseIds) {
            Set<String> contractIds = houseId2ContractIdsMap.get(houseId);
            if (CollUtil.isNotEmpty(contractIds)) {
                List<Contract> contracts = this.lambdaQuery().select(Contract::getId, Contract::getNameB, Contract::getState).in(Contract::getId, contractIds).list();
                for (Contract contract : contracts) {
                    //存在一个说明房屋就是已租状态
                    if (TO_BE_EFFECTIVE.getCode() == contract.getState() || IN_EFFECT.getCode() == contract.getState()) {
                        result.put(houseId, new ContractHouseStatusDTO(contract.getNameB(), 2));
                        break;
                    }
                }
                if (CollUtil.isEmpty(contracts)) {
                    result.put(houseId, new ContractHouseStatusDTO(null, 1));
                }
            }
        }
        return result;
    }


}
