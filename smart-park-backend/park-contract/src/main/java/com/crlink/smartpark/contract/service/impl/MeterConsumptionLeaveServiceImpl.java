package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.ChargeDetail;
import com.crlink.smartpark.contract.dto.ComputeAmountDTO;
import com.crlink.smartpark.contract.dto.DetailListDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionLeaveDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionLeave;
import com.crlink.smartpark.contract.mapper.MeterConsumptionLeaveMapper;
import com.crlink.smartpark.contract.model.excel.RentContractLeaveExcelExportDTO;
import com.crlink.smartpark.contract.model.query.RentContractLeaveQuery;
import com.crlink.smartpark.contract.model.request.MeterConsumptionLeaveReqDTO;
import com.crlink.smartpark.contract.model.request.SettlementPayReqDTO;
import com.crlink.smartpark.contract.model.vo.RentContractLeaveListVO;
import com.crlink.smartpark.contract.service.MeterConsumptionLeaveService;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.GetConsumptionByContract;
import com.crlink.smartpark.device.dto.MeterConsumptionDTO;
import com.crlink.smartpark.device.dto.MeterConsumptionDetailDTO;
import com.crlink.smartpark.device.feign.MeterConsumptionProvider;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.feign.FeeProvider;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
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

import static com.crlink.smartpark.contract.common.RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1;
import static com.crlink.smartpark.contract.common.RentFeeConstants.RENT_LEAVE_EXPORT_FILENAME;


/**
 * 租赁合同交易明细 服务实现类
 * */
@Service
public class MeterConsumptionLeaveServiceImpl extends ServiceImpl<MeterConsumptionLeaveMapper, MeterConsumptionLeave> implements MeterConsumptionLeaveService {

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private MeterConsumptionProvider meterConsumptionProvider;

    @Autowired
    private FeeProvider feeProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionLeave, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeterConsumptionLeave> lambdaQuery = this.lambdaQuery().in(MeterConsumptionLeave::getId, ids);
        if (flag) {
            lambdaQuery.select(MeterConsumptionLeave::getId, sValue);
        }
        Stream<MeterConsumptionLeave> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeterConsumptionLeave::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeterConsumptionLeave::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 离场结算-计算费用
     */
    @Override
    public MeterConsumptionLeaveDTO leaveSettlement(MeterConsumptionLeaveReqDTO reqDTO) {
        MeterConsumptionLeaveDTO result = MyBeanUtils.copy(reqDTO, MeterConsumptionLeaveDTO::new);
        result.setChargeDetail(setChargeDetailList(reqDTO));
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
        MeterConsumptionLeaveDTO meterConsumptionLeaveDTO = reqDTO.getMeterConsumptionLeaveDTO();
        LocalDate checkoutDate = reqDTO.getCheckoutDate();
        if (checkoutDate.isAfter(LocalDate.now())) {
            throw new ResolinkRuntimeException("300013");
        }
        MeterConsumptionLeave entity = MyBeanUtils.copy(meterConsumptionLeaveDTO, MeterConsumptionLeave::new);
        List<ChargeDetail> chargeDetailList = new LinkedList<>();
        meterConsumptionLeaveDTO.getChargeDetail().forEach(chargeDetail -> {
            ComputeAmountDTO dto = MyBeanUtils.copy(chargeDetail, ComputeAmountDTO::new);
            dto.setParkOrgId(meterConsumptionLeaveDTO.getParkOrgId());
            ChargeDetail result = MyBeanUtils.copy(chargeDetail, ChargeDetail::new);
            ChargeDetail detail = computeAmount(dto);
            result.setRoomAmount(detail.getRoomAmount());
            result.setPublicAmount(detail.getPublicAmount());
            result.setShareAmount(detail.getShareAmount());
            result.setAmountCount(detail.getAmountCount());
            chargeDetailList.add(result);
        });
        BigDecimal total = chargeDetailList.stream().map(ChargeDetail::getAmountCount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            throw new ResolinkRuntimeException("300017");
        }
        entity.setChargeDetail(chargeDetailList);
        entity.setTotalAmount(total);
        String remark = reqDTO.getRemark();
        entity.setRemark(remark);
        entity.setCheckoutDate(checkoutDate);
        entity.setId(null);
        entity.setContractType(reqDTO.getContractType());
        EntityUserUtil.setAll(entity);
        //1、结算表插入数据
        return this.save(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeterConsumptionLeaveDTO getRentContractLeaveById(String id) {
        MeterConsumptionLeave entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeterConsumptionLeaveDTO result = MyBeanUtils.copy(entity, MeterConsumptionLeaveDTO::new);
        return result;
    }


    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<RentContractLeaveListVO> listRentContractLeavePage(RentContractLeaveQuery query) {
        LambdaQueryWrapper<MeterConsumptionLeave> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<MeterConsumptionLeave> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<RentContractLeaveListVO> result = convert(list);
        return query.createPageInfo(result);
    }

    @Override
    public void export(RentContractLeaveQuery query) {
        if (query.getContractType().equals(APARTMENT_CONTRACT_1.getType())) {
            ExcelExportUtils.export(() -> list(getConditionWrapper(query)), sourceList ->
                            MyBeanUtils.copyList(convert(sourceList), RentContractLeaveExcelExportDTO::new),
                    RENT_LEAVE_EXPORT_FILENAME, RentContractLeaveExcelExportDTO.class);
        } else {
            ExcelExportUtils.export(() -> list(getConditionWrapper(query)), sourceList ->
                            MyBeanUtils.copyList(convert(sourceList), RentContractLeaveExcelExportDTO::new),
                    RENT_LEAVE_EXPORT_FILENAME, RentContractLeaveExcelExportDTO.class, Sets.newHashSet("houseName"));
        }
    }

    private List<RentContractLeaveListVO> convert(List<MeterConsumptionLeave> list) {
        Set<String> userIds = new HashSet<>();
        Set<String> houseIds = new HashSet<>();
        list.forEach(t -> {
            userIds.add(t.getCreateBy());
            if (StringUtils.isNotBlank(t.getHouseId())) {
                houseIds.add(t.getHouseId());
            }
        });
        Map<String, String> names = commonProvider.getNamesByIdsAndType(userIds, SimpleNode.TypeEnum.USER.getCode());
        Map<String, String> houseNameMaps = commonProvider.getBuildingHouseNamesByIds(houseIds);
        return MyBeanUtils.copyList(list, RentContractLeaveListVO::new, (s, t) -> {
            t.setCreateByName(names.get(s.getCreateBy()));
            t.setHouseName(houseNameMaps.get(t.getHouseId()));
        });
    }

    /**
     * 获取待缴纳明细
     *
     * @param reqDTO
     * @return
     */
    private List<ChargeDetail> setChargeDetailList(MeterConsumptionLeaveReqDTO reqDTO) {
        GetConsumptionByContract dto = new GetConsumptionByContract();
        dto.setContractId(reqDTO.getContractId());
        dto.setHouseId(reqDTO.getHouseId());
        dto.setType(reqDTO.getType());
        //跨服务调用、获取用量记录
        MeterConsumptionDTO consumptionByContract = meterConsumptionProvider.getConsumptionByContract(dto);
        if (Objects.isNull(consumptionByContract)) {
            return Collections.emptyList();
        }
        Set<String> houseIds = consumptionByContract.getHouseIds().parallelStream().collect(Collectors.toSet());
        List<String> houseNames = commonProvider.getNamesByIdsAndType(houseIds, SimpleNode.TypeEnum.HOUSE.code)
                .values().parallelStream().collect(Collectors.toList());
        List<MeterConsumptionDetailDTO> consumptionDetailList = consumptionByContract.getConsumptionDetail();
        List<ChargeDetail> chargeDetailList = new ArrayList<>();
        for (MeterConsumptionDetailDTO t : consumptionDetailList) {
            ChargeDetail chargeDetail = new ChargeDetail();
            if (RentFeeConstants.DeviceType.WATER_METER.type.equals(t.getDeviceType())) {
                if (t.getCategoryType().equals(1)) {
                    chargeDetail.setChargeName("水费");
                    chargeDetail.setShareCount(1);
                    chargeDetail.setNeedShare(false);
                } else {
                    chargeDetail.setChargeName("公摊水费");
                    chargeDetail.setShareCount(t.getShareCount());
                    chargeDetail.setNeedShare(true);
                }
                chargeDetail.setType(10);
            } else if (RentFeeConstants.DeviceType.ELECTRICITY_METER.type.equals(t.getDeviceType())) {
                if (t.getCategoryType().equals(1)) {
                    chargeDetail.setChargeName("电费");
                    chargeDetail.setShareCount(1);
                    chargeDetail.setNeedShare(false);
                } else {
                    chargeDetail.setChargeName("公摊电费");
                    chargeDetail.setShareCount(t.getShareCount());
                    chargeDetail.setNeedShare(true);
                }
                chargeDetail.setType(9);
            }
            chargeDetail.setLastReading(t.getThisReading());
            chargeDetail.setHouseName(houseNames);
            chargeDetailList.add(chargeDetail);
        }
        return chargeDetailList;
    }

    @Override
    public ChargeDetail computeAmount(ComputeAmountDTO dto) {
        String parkOrgId = dto.getParkOrgId();
        List<BillingSchemeDTO> schemeDTOS = feeProvider.listBillingScheme(Sets.newHashSet(parkOrgId)).get(parkOrgId);
        ChargeDetail detail = new ChargeDetail();
        schemeDTOS.forEach(t -> {
            Integer feeType = t.getFeeType();
            if (dto.getType().equals(feeType)) {
                BigDecimal roomAmount = dto.getThisReading().subtract(dto.getLastReading());
                if (dto.getNeedShare()) {
                    detail.setPublicAmount(roomAmount);
                    //应摊用量=公共用量/分摊房间数   保留2位小数、四舍五入
                    detail.setShareAmount(roomAmount.divide(new BigDecimal(dto.getShareCount()), 2, BigDecimal.ROUND_HALF_DOWN));
                    detail.setAmountCount(detail.getShareAmount().multiply(t.getUnitPriceWithoutTax()));
                } else {
                    detail.setRoomAmount(roomAmount);
                    detail.setAmountCount(roomAmount.multiply(t.getUnitPriceWithoutTax()));
                }
                detail.setType(dto.getType());
                detail.setNeedShare(dto.getNeedShare());
            }
        });
        return detail;
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
    private LambdaQueryWrapper<MeterConsumptionLeave> getConditionWrapper(RentContractLeaveQuery query) {
        String parkOrgId = query.getParkOrgId();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(parkOrgId);
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        String companyName = query.getCompanyName();
        LambdaQueryWrapper<MeterConsumptionLeave> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MeterConsumptionLeave::getContractType, query.getContractType())
                .like(StringUtils.isNotBlank(companyName), MeterConsumptionLeave::getCompanyName, companyName)
                .in(MeterConsumptionLeave::getParkOrgId, parkOrgIds)
                .orderByDesc(MeterConsumptionLeave::getCreateTime);
        return queryWrapper;
    }
}
