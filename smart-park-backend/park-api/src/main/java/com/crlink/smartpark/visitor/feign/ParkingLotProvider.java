package com.crlink.smartpark.visitor.feign;

import com.crlink.smartpark.visitor.dto.ParkingLotSaveDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 停车场管理
 *
 */
//@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/parkingLot", contextId = "ParkingLotProvider")
public interface ParkingLotProvider {

    //@ApiOperation(value = "批量新增停车场", tags = "停车场")
    //@PostMapping
    //boolean saveBatch(@RequestBody List<ParkingLotSaveDTO> parkingLotSaveDTOS);

}
