package com.crlink.smartpark.common.feign;

import com.crlink.smartpark.common.dto.OperateLogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 * */
@FeignClient(name = "park-common", path = "/smartPark/park-common", contextId = "OperateLogProvider")
public interface OperateLogProvider {

    /**
     * 操作日志保存
     */
    @PostMapping("/operate-log/create")
    String saveOperateLog(@RequestBody OperateLogDTO reqDTO);


}
