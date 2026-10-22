package com.crlink.smartpark.customer.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 群组关联 服务类
 * </p>
 * */
@FeignClient(name = "park-customer", path = "/smartPark/park-customer/parkManagement/equipment-ledger", contextId="EquipmentLedgerProvider")
public interface EquipmentLedgerProvider {

    /**
     * @return
     */
    @RequestMapping(value = "/hasAssociate", method = RequestMethod.GET)
    ResponseResult<Boolean> hasAssociate(@RequestParam("categoryId") String categoryId);


}
