package com.crlink.smartpark.visitor.feign;

import com.crlink.smartpark.visitor.vo.VisitorRecordVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/** * @desc
 */
@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/visitorRecord", contextId = "VisitorRecordProvider")
public interface VisitorRecordProvider {

    @ApiOperation(value = "通过人员设备ID查询访客记录", tags = "访客记录")
    @GetMapping("/getByPersonId/{devicePersonId}")
    VisitorRecordVO getByPersonId(@PathVariable("devicePersonId") Long devicePersonId);

    @ApiOperation(value = "更新门禁核验状态", tags = "访客记录")
    @PutMapping("/{devicePersonId}/updateDoorAccessStatus")
    void updateDoorAccessStatus(@PathVariable("devicePersonId") Long devicePersonId);

}
