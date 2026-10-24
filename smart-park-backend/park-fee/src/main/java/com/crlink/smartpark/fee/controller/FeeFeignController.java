package com.crlink.smartpark.fee.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.dto.BillingSchemeQuery;
import com.crlink.smartpark.fee.dto.SchemeItemDTO;
import com.crlink.smartpark.fee.entity.BillingScheme;
import com.crlink.smartpark.fee.service.BillingSchemeService;
import com.crlink.smartpark.fee.service.ChargeItemService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/fee_feign")
public class FeeFeignController {

    @Autowired
    private BillingSchemeService billingSchemeService;
    @Autowired
    private ChargeItemService chargeItemService;

    /**
     * 通过ids获取名字
     *
     * @description
     */
    @RequestMapping(value = "/getSchemeNameAndItemBySchemeIds", method = RequestMethod.POST)
    public Map<String, SchemeItemDTO> getSchemeNameAndItemBySchemeIds(@RequestBody Set<String> set) {
        return billingSchemeService.getSchemeNameAndItemBySchemeIds(set);
    }

    @ApiOperation(value = "计费方案-根据收费类型获取计算标准下拉", tags = "计费方案", notes =
            "1:房屋租金-建筑面积*月单价、套内面积*月单价<br>" +
                    "4:房屋租金-建筑面积*月单价<br>" +
                    "17、18、19:公寓租金-单间、标间、套间-天单价<br>" +
                    "20、21、22:公寓押金-单间、标间、套间-房间数*单价<br>" +
                    "9、10、11、12:电表、水表、公摊电费、公摊水费-用量*单价<br>" +
                    "其他返回全部")
    @GetMapping("/getCalculationListByFeeType")
    public ResponseResult<List<DictVO>> getCalculationListByFeeType(@RequestParam("type") @NotNull(message = "type不能为空") int type) {
        return ResponseResult.success(billingSchemeService.getCalculationListByFeeType(type));
    }

    @ApiOperation(value = "计费方案-分页查询", tags = "计费方案")
    @PostMapping("/listBillingSchemePage")
    public ResponseResult<PageInfo<BillingSchemeDTO>> listBillingSchemePage(@RequestBody BillingSchemeQuery query) {
        PageInfo<BillingSchemeDTO> result = billingSchemeService.listBillingSchemePage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "计费方案-根据园区id获取水电计费方案", tags = "计费方案")
    @PostMapping("/listBillingScheme")
    public Map<String, List<BillingSchemeDTO>> listBillingScheme(@RequestBody Set<String> parkOrgIds) {
        return billingSchemeService.listBillingScheme(parkOrgIds);
    }

    /**
     * 通过ids
     *
     * @description
     */
    @RequestMapping(value = "/getBillingSchemeDTOByIds", method = RequestMethod.POST)
    public Map<String, BillingSchemeDTO> getBillingSchemeDTOByIds(@RequestBody Set<String> set) {
        return billingSchemeService.getBillingSchemeDTOByIds(set);
    }

}
