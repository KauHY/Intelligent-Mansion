package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.dto.GetConsumptionByContract;
import com.crlink.smartpark.device.dto.MeterConsumptionDTO;
import com.crlink.smartpark.device.dto.MeterConsumptionDetailDTO;
import com.crlink.smartpark.device.entity.MeterConsumption;
import com.crlink.smartpark.device.model.ConsumptionModifyReqDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionQuery;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 企业或房屋水电用量 服务类
 * </p>
 * */
public interface MeterConsumptionService extends IService<MeterConsumption> {

    MeterConsumptionDTO getMeterConsumptionById(String id);

    PageInfo<MeterConsumptionDTO> listMeterConsumptionPage(MeterConsumptionQuery query);

    List<MeterConsumptionDetailDTO> getHouseConsumptionDetail(Collection<String> houseIds, String settledMonth);

    Boolean modifyShareConsumption(ConsumptionModifyReqDTO dto);

    List<MeterConsumptionDTO> listMeterConsumption(String nowMonth);

    MeterConsumptionDTO getConsumptionByContract(GetConsumptionByContract dto);

    void downloadMeterConsumption(MeterConsumptionQuery query);
}
