package com.crlink.smartpark.contract.feign;

import com.crlink.smartpark.contract.dto.ContractDTO;
import com.crlink.smartpark.contract.dto.ContractHouseStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 群组关联 服务类
 * </p>
 * */
@FeignClient(name = "park-contract", path = "/smartPark/park-contract", contextId = "ContractProvider")
public interface ContractProvider {

    /**
     * @return
     */
    @RequestMapping(value = "/contract/getEffectiveHouseIds", method = RequestMethod.GET)
    Set<String> getEffectiveHouseIds();

    /**
     * 合同-获取生效中的合同
     *
     * @return
     */
    @RequestMapping(value = "/contract/getEffectiveContracts", method = RequestMethod.GET)
    List<ContractDTO> getEffectiveContracts();

    /**
     * value = "合同房屋状态", tags = "内部接口,用于运营大屏"
     *
     * @param houseIds
     * @return
     */
    @RequestMapping(value = "/contract/getContractHouseRelationFor3D", method = RequestMethod.GET)
    Map<String, ContractHouseStatusDTO> getContractHouseRelationFor3D(@RequestBody Collection<String> houseIds);


    /**
     * 根据园区获取已租的房屋id集合,用于运营大屏
     *
     * @param parkOrgId
     * @return
     */
    @GetMapping("/contract/vo/getRentedHouseId")
    Map<Integer, Set<String>> getRentedHouseId(@RequestParam("parkOrgId") String parkOrgId);
}
