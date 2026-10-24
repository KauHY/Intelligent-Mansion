package com.crlink.smartpark.device.service.task;

import cn.hutool.core.util.NumberUtil;
import com.crlink.smartpark.contract.dto.ContractDTO;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.contract.feign.ContractProvider;
import com.crlink.smartpark.device.dto.Consumption;
import com.crlink.smartpark.device.dto.MeterConsumptionDetailDTO;
import com.crlink.smartpark.device.entity.MeterConsumption;
import com.crlink.smartpark.device.service.MeterConsumptionService;
import com.crlink.smartpark.system.dto.house.HouseDTO;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.YEAR_MONTH_FORMAT;

@Service
public class MeterConsumptionTask {
    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private MeterConsumptionService meterConsumptionService;

    @Autowired
    private HouseProvider houseProvider;
    public static ThreadLocal<LocalDate> NOW_TIME_TL = new ThreadLocal<>();

    /**
     * 计算企业或房屋的水电用量，先计算水电表的水电用量
     * 生成企业或房屋水电用量，每月1日计算上个月的的，
     */
    public void generateMeterConsumption() {
        //用于手动触发
        LocalDate nowDay = NOW_TIME_TL.get();
        if (Objects.nonNull(nowDay)) {
            NOW_TIME_TL.remove();
        } else {
            nowDay = LocalDate.now();
        }

        List<ContractDTO> effectiveContracts = contractProvider.getEffectiveContracts();
        List<MeterConsumption> result = Lists.newArrayList();
        //todo 结算月份
        String settledMonth = nowDay.minusDays(5).format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT));
        Set<String> allHouseIds = Sets.newHashSet();
        for (ContractDTO contract : effectiveContracts) {
            MeterConsumption companyConsumption = new MeterConsumption();
            companyConsumption.setContractId(contract.getId());
            companyConsumption.setType(1);
            companyConsumption.setParkOrgId(contract.getParkOrgId());
            companyConsumption.setSettledMonth(settledMonth);
            companyConsumption.setCompanyId(contract.getCustomerIdB());
            companyConsumption.setCompanyName(contract.getNameB());
            companyConsumption.setContractType(contract.getType());
            List<String> houseIds = contract.getHouseMessages().parallelStream().map(HouseMessage::getHouseId).collect(Collectors.toList());
            companyConsumption.setHouseIds(houseIds);
            companyConsumption.setConsumption(getConsumption(houseIds, settledMonth));
            allHouseIds.addAll(houseIds);
            result.add(companyConsumption);
            for (String houseId : houseIds) {
                MeterConsumption houseConsumption = new MeterConsumption();
                houseConsumption.setContractId(contract.getId());
                houseConsumption.setType(0);
                houseConsumption.setParkOrgId(contract.getParkOrgId());
                houseConsumption.setSettledMonth(settledMonth);
                houseConsumption.setCompanyId(contract.getCustomerIdB());
                houseConsumption.setCompanyName(contract.getNameB());
                houseConsumption.setContractType(contract.getType());
                houseConsumption.setHouseIds(Lists.newArrayList(houseId));
                houseConsumption.setConsumption(getConsumption(Lists.newArrayList(houseId), settledMonth));
                result.add(houseConsumption);
            }
        }
        Map<String, String> house2BuildingIdMap = queryHouseBuildingId(allHouseIds);
        result.forEach(t -> {
            if (t.getType() == 0) {
                t.setBuildingId(house2BuildingIdMap.get(t.getHouseIds().get(0)));
            }
        });
        meterConsumptionService.saveBatch(result);
    }

    private Map<String, String> queryHouseBuildingId(Set<String> houseIds) {
        Map<String, HouseDTO> byHouseId = houseProvider.getHouseByHouseId(houseIds, Sets.newHashSet("building_id"));
        Map<String, String> map = new HashMap<>(houseIds.size());
        byHouseId.forEach((houseId, v) -> {
            map.put(houseId, v.getBuildingId());
        });
        return map;
    }

    private List<Consumption> getConsumption(Collection<String> houseIds, String settledMonth) {
        ImmutableMap<Integer, Consumption> map = ImmutableMap.of(5, new Consumption(5), 6, new Consumption(6));
        List<MeterConsumptionDetailDTO> detail = meterConsumptionService.getHouseConsumptionDetail(houseIds, settledMonth);
        for (MeterConsumptionDetailDTO detailDTO : detail) {
            Consumption consumption = map.get(detailDTO.getDeviceType());
            BigDecimal count = detailDTO.getConsumption();
            if (detailDTO.getCategoryType() == 1) {
                consumption.setRoomAmount(NumberUtil.add(consumption.getRoomAmount(), count));
                consumption.setTotalAmount(NumberUtil.add(consumption.getShareAmount(), consumption.getRoomAmount()));
            } else {
                int shareCount = detailDTO.getShareCount();
                consumption.setPublicAmount(NumberUtil.add(consumption.getPublicAmount(), count));
                consumption.setShareAmount(NumberUtil.div(consumption.getPublicAmount(), shareCount, 2));
                consumption.setTotalAmount(NumberUtil.add(consumption.getRoomAmount(), consumption.getShareAmount()));
            }
        }
        return Lists.newArrayList(map.values());
    }
}
