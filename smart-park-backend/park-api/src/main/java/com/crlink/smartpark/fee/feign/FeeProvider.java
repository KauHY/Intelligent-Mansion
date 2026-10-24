package com.crlink.smartpark.fee.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.dto.BillingSchemeQuery;
import com.crlink.smartpark.fee.dto.SchemeItemDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "park-fee", path = "/smartPark/park-fee/fee_feign", contextId = "FeeProvider")
public interface FeeProvider {

    /**
     * 通过ids获取名字
     *
     * @param list
     * @return
     */
    @RequestMapping(value = "/getSchemeNameAndItemBySchemeIds", method = RequestMethod.POST)
    Map<String, SchemeItemDTO> getSchemeNameAndItemBySchemeIds(@RequestBody Set<String> list);

    @RequestMapping(value = "/getBillingSchemeDTOByIds", method = RequestMethod.POST)
    Map<String, BillingSchemeDTO> getBillingSchemeDTOByIds(@RequestBody Set<String> list);

    @GetMapping("/getCalculationListByFeeType")
    ResponseResult<List<DictVO>> getCalculationListByFeeType(@RequestParam("type") @NotNull(message = "type不能为空") int type);

    @PostMapping("/listBillingSchemePage")
    ResponseResult<PageInfo<BillingSchemeDTO>> listBillingSchemePage(@RequestBody BillingSchemeQuery query);

    @PostMapping("/listBillingScheme")
    Map<String, List<BillingSchemeDTO>> listBillingScheme(@RequestBody Set<String> parkOrgIds);
}
