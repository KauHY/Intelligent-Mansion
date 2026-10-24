package com.crlink.smartpark.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.fee.common.FeeConstant;
import com.crlink.smartpark.fee.controller.request.BillingSchemeReqDTO;
import com.crlink.smartpark.fee.controller.request.CalculationReqDTO;
import com.crlink.smartpark.fee.dto.BillingSchemeQuery;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.dto.ChargeItemDTO;
import com.crlink.smartpark.fee.dto.SchemeItemDTO;
import com.crlink.smartpark.fee.entity.BillingScheme;
import com.crlink.smartpark.fee.entity.ChargeItem;
import com.crlink.smartpark.fee.mapper.BillingSchemeMapper;
import com.crlink.smartpark.fee.service.BillingSchemeService;
import com.crlink.smartpark.fee.service.ChargeItemService;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 计费方案 服务实现类
 * */
@Service
public class BillingSchemeServiceImpl extends ServiceImpl<BillingSchemeMapper, BillingScheme> implements BillingSchemeService {

    @Autowired
    private ChargeItemService chargeItemService;
    @Autowired
    private CommonProvider commonProvider;

    public static final int NEGATIVE_BIGDECIMAL = -1;

    public static final BigDecimal THIRTY = new BigDecimal(30);

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BillingScheme, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<BillingScheme> lambdaQuery = this.lambdaQuery().in(BillingScheme::getId, ids);
        if (flag) {
            lambdaQuery.select(BillingScheme::getId, sValue);
        }
        Stream<BillingScheme> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(BillingScheme::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(BillingScheme::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateBillingScheme(BillingSchemeReqDTO reqDTO) {
        AssertUtil.requireTrue(this.count(Wrappers.<BillingScheme>query().lambda()
                .eq(BillingScheme::getSchemeName, reqDTO.getSchemeName())
                .eq(BillingScheme::getParkOrgId, reqDTO.getParkOrgId())
                .ne(StringUtils.isNotBlank(reqDTO.getId()), BillingScheme::getId, reqDTO.getId())) <= 0, "310001");
        ChargeItem chargeItem = chargeItemService.lambdaQuery().eq(ChargeItem::getId, reqDTO.getChargeItemId()).one();
        if (chargeItem != null) {
            if (chargeItem.getFeeType() == FeeConstant.FeeTypeToCalculation.ELECTRICITY_METER.getType()
                    || chargeItem.getFeeType() == FeeConstant.FeeTypeToCalculation.WATER_METER.getType()
                    || chargeItem.getFeeType() == FeeConstant.FeeTypeToCalculation.SHARED_ELECTRICITY_CHARGE.getType()
                    || chargeItem.getFeeType() == FeeConstant.FeeTypeToCalculation.SHARED_WATER_CHARGE.getType()) {
                int count = this.lambdaQuery().eq(BillingScheme::getParkOrgId, chargeItem.getParkOrgId()).eq(BillingScheme::getFeeType, chargeItem.getFeeType()).count();
                if (StringUtils.isBlank(reqDTO.getId())) {
                    if (count > 0) {
                        throw new ResolinkRuntimeException("310003");
                    }
                } else {
                    BillingScheme billingScheme = this.getById(reqDTO.getId());
                    if (!billingScheme.getChargeItemId().equals(reqDTO.getChargeItemId())) {
                        if (count > 0) {
                            throw new ResolinkRuntimeException("310003");
                        }
                    }
                }
            }
        }
        BillingScheme entity = MyBeanUtils.copy(reqDTO, BillingScheme::new);
        entity.setFeeType(chargeItem.getFeeType());
        entity.setParkOrgId(getParkOrgIdByChargeItemId(reqDTO.getChargeItemId()));
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }


    /**
     * 通过id查询实体
     */
    @Override
    public BillingSchemeDTO getBillingSchemeById(String id) {
        if (StringUtils.isNotBlank(id)) {
            BillingScheme entity = getById(id);
            if (entity != null) {
                BillingSchemeDTO billingSchemeDTO = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
                return billingSchemeDTO;
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<BillingSchemeDTO> listBillingSchemePage(BillingSchemeQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        query.startPage();
        query.setParkOrgIds(parkOrgIds);
        List<BillingScheme> list = baseMapper.listBillingSchemePage(query);
        MyPageInfoUtils.isEmptyPage(list);
        List<BillingSchemeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<BillingScheme> getConditionWrapper(BillingSchemeQuery query) {
        LambdaQueryWrapper<BillingScheme> queryWrapper = new LambdaQueryWrapper<BillingScheme>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<BillingSchemeDTO> entitiesToDTOs(List<BillingScheme> entities) {
        Set<String> parkOrgIds = Sets.newHashSet();
        Set<String> chargeItemIds = entities.parallelStream().map(billingScheme -> {
                    parkOrgIds.add(billingScheme.getParkOrgId());
                    return billingScheme.getChargeItemId();
                }
        ).collect(Collectors.toSet());
        Map<String, ChargeItem> id2ValueMap = (Map<String, ChargeItem>) chargeItemService.id2ValueMap(chargeItemIds, null);

        Map<String, String> parkOrgNameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        return MyBeanUtils.copyList(entities, BillingSchemeDTO::new, (s, t) -> {
            t.setChargeItemDTO(MyBeanUtils.copy(id2ValueMap.get(s.getChargeItemId()), ChargeItemDTO::new));
            t.setParkOrgName(parkOrgNameMap.get(s.getParkOrgId()));
            t.setUpdateTime(s.getUpdateTime().toLocalDate());
        });
    }

    private String getParkOrgIdByChargeItemId(String chargeItemId) {
        Map<String, String> parkOrgIdMap = (Map<String, String>) chargeItemService.id2ValueMap(Sets.newHashSet(chargeItemId), ChargeItem::getParkOrgId);
        return parkOrgIdMap.get(chargeItemId);
    }

    @Override
    public BigDecimal calculation(CalculationReqDTO calculationReqDTO) {
        DictConstant.FeeCalculationStandard standardByCode = DictConstant.FeeCalculationStandard.getStandardByCode(calculationReqDTO.getCalculationStandard());
        BigDecimal days = new BigDecimal(calculationReqDTO.getBillingEndDate().toEpochDay() - calculationReqDTO.getBillingStartDate().toEpochDay() + 1);
        Integer year = Period.between(calculationReqDTO.getBillingStartDate(), calculationReqDTO.getBillingEndDate().plusDays(1)).getYears();
        BigDecimal monthOfYear = new BigDecimal(year * 12);
        if (Objects.nonNull(calculationReqDTO.getFreeRentDay()) && Constants.ZERO != calculationReqDTO.getFreeRentDay()) {
            BigDecimal freeRentDays = new BigDecimal(calculationReqDTO.getFreeRentDay());
            days = days.subtract(freeRentDays);
            monthOfYear = monthOfYear.subtract(freeRentDays);
        }
        BigDecimal months = days.divide(THIRTY, 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal result;
        switch (Objects.requireNonNull(standardByCode)) {
            case BUILDING_AREA_MULTIPLY_MONTHLY_UNIT_PRICE: {
                BigDecimal totalArea = calculationReqDTO.getBuildingAreas().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                result = totalArea.multiply(calculationReqDTO.getUnitPrice()).multiply(monthOfYear);
                break;
            }
            case COVER_AREA_MULTIPLY_MONTHLY_UNIT_PRICE: {
                BigDecimal totalArea = calculationReqDTO.getCoverAreas().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                result = totalArea.multiply(calculationReqDTO.getUnitPrice()).multiply(monthOfYear);
                break;
            }
            case DOSAGE_MULTIPLY_UNIT_PRICE: {
                result = calculationReqDTO.getDosage().multiply(calculationReqDTO.getUnitPrice());
                break;
            }
            case PRICE_PER_DAY: {
                if (FeeConstant.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_2.getCalculationValues().contains(calculationReqDTO.getFeeType())) {
                    result = calculationReqDTO.getUnitPrice().multiply(calculationReqDTO.getNumberOfRooms());
                } else {
                    result = days.multiply(calculationReqDTO.getUnitPrice()).multiply(calculationReqDTO.getNumberOfRooms());
                }

                break;
            }
            case NUMBER_OF_ROOMS_MULTIPLY_UNIT_PRICE: {
                if (FeeConstant.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_2.getCalculationValues().contains(calculationReqDTO.getFeeType())) {
                    result = calculationReqDTO.getUnitPrice().multiply(calculationReqDTO.getNumberOfRooms());
                } else {
                    result = days.multiply(calculationReqDTO.getUnitPrice()).multiply(calculationReqDTO.getNumberOfRooms());
                }
                break;
            }
            default:
                result = BigDecimal.ZERO;
                break;
        }
        if (NEGATIVE_BIGDECIMAL == result.signum()) {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    @Override
    public Map<String, SchemeItemDTO> getSchemeNameAndItemBySchemeIds(Set<String> set) {
        List<BillingScheme> list = this.lambdaQuery().select(BillingScheme::getId, BillingScheme::getChargeItemId, BillingScheme::getSchemeName)
                .in(BillingScheme::getId, set)
                .list();
        Set<String> itemIds = list.stream().map(BillingScheme::getChargeItemId).collect(Collectors.toSet());
        Map<String, ChargeItem> stringMap = (Map<String, ChargeItem>) chargeItemService.id2ValueMap(itemIds, null);
        return list.stream().collect(Collectors
                .toMap(BillingScheme::getId, e -> new SchemeItemDTO(e.getSchemeName(), stringMap.get(e.getChargeItemId()).getChargeName(), stringMap.get(e.getChargeItemId()).getTaxRate())));
    }

    @Override
    public Map<String, BillingSchemeDTO> getBillingSchemeDTOByIds(Set<String> set) {
        List<BillingScheme> list = this.lambdaQuery().in(BillingScheme::getId, set).list();
        List<BillingSchemeDTO> result = entitiesToDTOs(list);
        return result.stream().collect(Collectors.toMap(BillingSchemeDTO::getId, Function.identity()));
    }

    @Override
    public List<DictVO> getCalculationListByFeeType(int type) {
        FeeConstant.FeeTypeToCalculation feeTypeToCalculationByFeeType = FeeConstant.FeeTypeToCalculation.getFeeTypeToCalculationByFeeType(type);
        int[] calculationValues = Objects.isNull(feeTypeToCalculationByFeeType) ?
                DictConstant.FeeCalculationStandard.getAllCode()
                : feeTypeToCalculationByFeeType.getCalculationValues();
        List<DictConstant.FeeCalculationStandard> standardByCode = DictConstant.FeeCalculationStandard.getStandardByCode(calculationValues);
        return standardByCode.stream().map(e -> new DictVO(e.getLabel(), e.getCode())).collect(Collectors.toList());
    }

    @Override
    public List<DictVO> getCalculationListByFeeId(String id) {
        ChargeItem chargeItem = Optional.ofNullable(chargeItemService.getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return getCalculationListByFeeType(chargeItem.getFeeType());
    }

    @Override
    public Map<String, List<BillingSchemeDTO>> listBillingScheme(Set<String> parkOrgIds) {
        Map<String, List<BillingScheme>> schemeMap = lambdaQuery().in(BillingScheme::getFeeType, FeeConstant.ContractTypeToSchemeFeeType.Meter_CONTRACT.getCalculationValues())
                .in(BillingScheme::getParkOrgId, parkOrgIds).list().parallelStream().collect(Collectors.groupingBy(BillingScheme::getParkOrgId));
        Map<String, List<BillingSchemeDTO>> listMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(schemeMap)) {
            for (String key : schemeMap.keySet()) {
                List<BillingScheme> value = schemeMap.get(key);
                List<BillingSchemeDTO> result = entitiesToDTOs(value);
                listMap.put(key, result);
            }
        }
        return listMap;
    }
}
