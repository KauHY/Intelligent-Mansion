package com.crlink.smartpark.visitor.feign;

import com.crlink.smartpark.visitor.dto.AccessRecordDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** * @desc
 */
@FeignClient(name = "park-visitor", path = "/smartPark/park-visitor/access-record", contextId = "AccessRecordProvider")
public interface AccessRecordProvider {

    @ApiOperation(value = "保存访客记录", tags = "访客记录")
    @PostMapping("/saveAccessRecord")
    Boolean saveAccessRecord(@RequestBody AccessRecordDTO accessRecordDTO);

}
