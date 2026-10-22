package com.crlink.smartpark.device.feign;

import com.crlink.smartpark.device.dto.GetConsumptionByContract;
import com.crlink.smartpark.device.dto.MeterConsumptionDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 水电用量 服务类
 * </p>
 * */
@FeignClient(name = "park-device", path = "/smartPark/park-device/meterConsumption", contextId = "MeterConsumptionProvider")
public interface MeterConsumptionProvider {

    @ApiOperation(value = "卡片管理-根据员工ID查询所有可用卡号", tags = "卡片管理")
    @PostMapping("/listMeterConsumption")
    List<MeterConsumptionDTO> listMeterConsumption(@RequestParam("nowMonth") String nowMonth);

    @ApiOperation(value = "企业或房屋水电用量-根据合同Id或房屋Id获取水电上月用量", tags = "企业或房屋水电用量")
    @PostMapping("/getConsumptionByContract")
    MeterConsumptionDTO getConsumptionByContract(@RequestBody GetConsumptionByContract dto);

}
