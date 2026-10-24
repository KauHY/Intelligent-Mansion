package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.feign.ContractProvider;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.Consumption;
import com.crlink.smartpark.device.dto.GetConsumptionByContract;
import com.crlink.smartpark.device.dto.MeterConsumptionDTO;
import com.crlink.smartpark.device.dto.MeterConsumptionDetailDTO;
import com.crlink.smartpark.device.entity.MeterConsumption;
import com.crlink.smartpark.device.entity.MeterConsumptionRecord;
import com.crlink.smartpark.device.mapper.MeterConsumptionMapper;
import com.crlink.smartpark.device.model.ConsumptionModifyReqDTO;
import com.crlink.smartpark.device.model.excel.MeterConsumptionExportDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionQuery;
import com.crlink.smartpark.device.service.MeterConsumptionRecordService;
import com.crlink.smartpark.device.service.MeterConsumptionService;
import com.crlink.smartpark.device.service.MeterContractHouseRelationService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.ELECTRICITY_METER;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.WATER_METER;
import static com.crlink.smartpark.device.common.MeterConstant.COMPANY_CONSUMPTION_EXPORT_FILENAME;
import static com.crlink.smartpark.device.common.MeterConstant.HOUSE_CONSUMPTION_EXPORT_FILENAME;


/**
 * 企业或房屋水电用量 服务实现类
 * */
@Service
public class MeterConsumptionServiceImpl extends ServiceImpl<MeterConsumptionMapper, MeterConsumption> implements MeterConsumptionService {


    @Autowired
    private MeterConsumptionRecordService meterConsumptionRecordService;

    @Autowired
    private MeterContractHouseRelationService meterContractHouseRelationService;

    @Autowired
    private ContractProvider contractProvider;


    @Override
    @Id2objHandle
    public MeterConsumptionDTO getMeterConsumptionById(String id) {
        MeterConsumption entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeterConsumptionDTO detail = getConsumptionDetail(entity);
        return detail;
    }

    @Override
    @Id2objHandle
    public List<MeterConsumptionDTO> listMeterConsumption(String nowMonth) {
        List<MeterConsumption> meterConsumptionList = lambdaQuery().eq(MeterConsumption::getType, Constants.ONE).eq(MeterConsumption::getSettledMonth, nowMonth).list();
        List<MeterConsumptionDTO> result = MyBeanUtils.copyList(meterConsumptionList, MeterConsumptionDTO::new);
        return result;
    }

    @Override
    @Id2objHandle
    public PageInfo<MeterConsumptionDTO> listMeterConsumptionPage(MeterConsumptionQuery query) {
        query.startPage();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.setParkOrgIds(parkOrgIds);
        List<MeterConsumption> entities = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(entities);
        List<MeterConsumptionDTO> result = MyBeanUtils.copyList(entities, MeterConsumptionDTO::new);
        return query.createPageInfo(result);
    }

    @Override
    public MeterConsumptionDTO getConsumptionByContract(GetConsumptionByContract dto) {
        String settledMonth = LocalDate.now().minusMonths(1).format(Constants.DEFAULT_YEAR_MONTH_FORMAT);
        LambdaQueryChainWrapper<MeterConsumption> wrapper = this.lambdaQuery()
                .select(MeterConsumption::getId)
                .eq(MeterConsumption::getContractId, dto.getContractId())
                .eq(MeterConsumption::getType, dto.getType())
                .eq(MeterConsumption::getSettledMonth, settledMonth);
        if (StringUtils.isNotBlank(dto.getHouseId())) {
            String sql = new StringBuilder("JSON_CONTAINS(").append("house_ids,").append("JSON_QUOTE(").append(dto.getHouseId()).append("))").toString();
            wrapper.apply(sql);
        }
        MeterConsumption one = wrapper.last("limit 1").one();
        if (Objects.isNull(one)) {
            return null;
        }
        return getMeterConsumptionById(one.getId());
    }

    @Override
    public void downloadMeterConsumption(MeterConsumptionQuery query) {
        String fileName = query.getType() == 0 ? HOUSE_CONSUMPTION_EXPORT_FILENAME : COMPANY_CONSUMPTION_EXPORT_FILENAME;
        query.startPage();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        query.setParkOrgIds(parkOrgIds);
        ExcelExportUtils.export(() -> this.list(getConditionWrapper(query)),
                entities -> convertToMeterConsumptionExportDTO(entities),
                fileName, MeterConsumptionExportDTO.class);
    }

    private List<MeterConsumptionExportDTO> convertToMeterConsumptionExportDTO(List<MeterConsumption> entities) {
        List<MeterConsumptionExportDTO> result = new ArrayList<>(entities.size() * 2);
        for (int i = 0; i < entities.size(); i++) {
            MeterConsumptionExportDTO exportDTO = MyBeanUtils.copy(entities.get(i), MeterConsumptionExportDTO::new);
            MeterConsumptionExportDTO clone = (MeterConsumptionExportDTO) exportDTO.clone();
            List<Consumption> consumption = entities.get(i).getConsumption();
            exportDTO.setDeviceType(WATER_METER.type);
            clone.setDeviceType(ELECTRICITY_METER.type);
            if (CollUtil.isNotEmpty(consumption)) {
                exportDTO.setTotalAmount(consumption.get(0).getTotalAmount());
                clone.setTotalAmount(consumption.get(1).getTotalAmount());
            }
            result.add(exportDTO);
            result.add(clone);
        }
        return result;
    }


    private LambdaQueryWrapper<MeterConsumption> getConditionWrapper(MeterConsumptionQuery query) {
        LambdaQueryWrapper<MeterConsumption> wrapper = new LambdaQueryWrapper<MeterConsumption>();
        wrapper.eq(MeterConsumption::getType, query.getType());
        wrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeterConsumption::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StringUtils.isNotBlank(query.getSettledMonth()), MeterConsumption::getSettledMonth, query.getSettledMonth());
        wrapper.like(StringUtils.isNotBlank(query.getCompanyName()), MeterConsumption::getCompanyName, query.getCompanyName());
        wrapper.eq(StringUtils.isNotBlank(query.getBuildingId()), MeterConsumption::getBuildingId, query.getBuildingId());
        if (StringUtils.isNotBlank(query.getHouseId())) {
            String sql = new StringBuilder("JSON_CONTAINS(").append("house_ids,").append("JSON_QUOTE('").append(query.getHouseId()).append("'))").toString();
            wrapper.apply(sql);
        }
        wrapper.in(MeterConsumption::getParkOrgId, query.getParkOrgIds());
        return wrapper;
    }


    @Override
    public List<MeterConsumptionDetailDTO> getHouseConsumptionDetail(Collection<String> houseIds, String settledMonth) {
        List<MeterConsumptionDetailDTO> result = new ArrayList<>();
        Map<String, Set<String>> houseId2MeterIds = meterContractHouseRelationService.houseId2MeterIds(houseIds, settledMonth);
        if (CollUtil.isEmpty(houseId2MeterIds.values())) {
            return result;
        }
        Set<String> allMeterIds = houseId2MeterIds.values().stream().flatMap(t -> t.stream()).collect(Collectors.toSet());
        Map<String, MeterConsumptionRecord> meterMap = meterConsumptionRecordService.listByMeterIds(allMeterIds, settledMonth);
        Map<String, Set<String>> meterId2houseIds = meterContractHouseRelationService.meterId2houseIds(allMeterIds, settledMonth);
        for (String houseId : houseIds) {
            Set<String> meterIds = houseId2MeterIds.getOrDefault(houseId, Collections.emptySet());
            for (String meterId : meterIds) {
                MeterConsumptionRecord record = meterMap.get(meterId);
                MeterConsumptionDetailDTO detailDTO = new MeterConsumptionDetailDTO();
                detailDTO.setCategoryType(record.getEffectProperty());
                detailDTO.setDeviceType(record.getDeviceType());
                if (record.getEffectProperty() == 2) {
                    detailDTO.setShareCount(meterId2houseIds.get(meterId).size());
                }
                detailDTO.setDeviceName(record.getDeviceName());
                detailDTO.setLastReading(record.getLastReading());
                detailDTO.setThisReading(record.getThisReading());
                detailDTO.setConsumption(record.getConsumption());
                result.add(detailDTO);
            }
        }
        return result;
    }

    @Override
    public Boolean modifyShareConsumption(ConsumptionModifyReqDTO dto) {
        MeterConsumption meterConsumption = this.lambdaQuery().select(MeterConsumption::getId, MeterConsumption::getConsumption)
                .eq(MeterConsumption::getId, dto.getId()).one();
        meterConsumption.getConsumption().forEach(c -> {
            if (Objects.equals(c.getDeviceType(), dto.getDeviceType())) {
                c.setShareAmount(dto.getShareAmount());
                c.setTotalAmount(c.getRoomAmount().add(dto.getShareAmount()));
            }
        });
        //List<Consumption> consumptions = Lists.newArrayList();
        //for (Consumption consumptionMap : meterConsumption.getConsumption()) {
        //    Consumption consumption = new Consumption();
        //    BeanUtil.copyProperties(consumptionMap, consumption);
        //    if (Objects.equals(consumption.getDeviceType(), dto.getDeviceType())) {
        //        consumption.setTotalAmount(consumption.getRoomAmount().add(dto.getShareAmount()));
        //        consumption.setShareAmount(dto.getShareAmount());
        //    }
        //    consumptions.add(consumption);
        //}
        //meterConsumption.setConsumption(consumptions);
        EntityUserUtil.setUpdateBy(meterConsumption);
        return this.updateById(meterConsumption);
    }


    private List<MeterConsumptionDTO> entitiesToDTOs(List<MeterConsumption> entities) {
        return MyBeanUtils.copyList(entities, MeterConsumptionDTO::new, (s, t) -> {
        });
    }

    private MeterConsumptionDTO getConsumptionDetail(MeterConsumption entity) {
        MeterConsumptionDTO dto = MyBeanUtils.copy(entity, MeterConsumptionDTO::new);
        String buildingId = dto.getBuildingId();
        List<String> houseIds = dto.getHouseIds();
        List<MeterConsumptionDetailDTO> detail = getHouseConsumptionDetail(houseIds, entity.getSettledMonth());
        dto.setConsumptionDetail(detail);
        return dto;
    }


//    public static void main(String[] args) {
//        ImmutableMap<Integer, Consumption> map = ImmutableMap.of(5, new Consumption(5), 6, new Consumption(6));
//        Consumption consumption = map.get(5);
//        consumption.setPublicAmount(BigDecimal.TEN);
//        System.out.println(map);
//    }
}
