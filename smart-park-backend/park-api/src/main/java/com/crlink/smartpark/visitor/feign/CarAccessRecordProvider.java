package com.crlink.smartpark.visitor.feign;

import com.crlink.smartpark.visitor.dto.CarAccessRecordDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 车行管理
 *
 */
@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/carAccessRecord", contextId = "CarAccessRecordProvider")
public interface CarAccessRecordProvider {

    @ApiOperation(value = "车辆通行-记录保存", tags = "车行管理")
    @PostMapping("/saveEventRecord")
    Boolean saveEventRecord(@RequestBody CarAccessRecordDTO recordDTO);

}
