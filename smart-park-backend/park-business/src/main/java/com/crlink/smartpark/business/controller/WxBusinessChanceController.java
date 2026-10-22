package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.controller.request.BusinessChanceOperationDTO;
import com.crlink.smartpark.business.dto.chance.BusinessChanceReqDTO;
import com.crlink.smartpark.business.dto.chance.BusinessChanceVO;
import com.crlink.smartpark.business.dto.chance.FollowBusinessChanceDTO;
import com.crlink.smartpark.business.service.BusinessChanceService;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.crlink.smartpark.business.common.BusinessChanceRecordTypeEnum.RECEIVE_CHANCE_3;

/**
 * <p>
 *
 * </p>
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-商机管理-小程序")
@RequestMapping("/business/business-chance/wx")
public class WxBusinessChanceController {

    @Autowired
    private BusinessChanceService businessChanceService;


    @ApiOperation(value = "商机-新增", tags = "商机管理-小程序")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid BusinessChanceReqDTO dto) {
        //商机来源：1 商机录入
        dto.setBusinessSource(1);
        dto.setId(null);
        Boolean result = businessChanceService.saveOrUpdateBusinessChance(dto);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "商机-根据ID查询商机信息", tags = "商机管理-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<BusinessChanceVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        BusinessChanceVO result = businessChanceService.getBusinessChanceById(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "分页查询招商信息:type,0全部商机，1公共商机，2我的商机", tags = "商机管理-小程序")
    @PostMapping("/listBusinessInfoPage/{type}")
    public ResponseResult<PageInfo<BusinessChanceVO>> listBusinessChancePage(@PathVariable("type") @NotNull(message = "type不能为空！") Integer type, @RequestBody BaseQuery<String> query) {
        PageInfo<BusinessChanceVO> result = businessChanceService.listBusinessChanceWxPage(query, type);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "操作商机:操作类型：2分配商机、3领取商机", tags = "商机管理-小程序")
    @PostMapping("/operate")
    public ResponseResult<Boolean> operateBusinessChance(@RequestBody @Valid BusinessChanceOperationDTO dto) {
        if (dto.getRecordType().equals(RECEIVE_CHANCE_3.getCode())) {
            dto.setFollowEmployeeId(SystemUtil.getInstance().getEmployeeId());
        }
        Boolean result = businessChanceService.operateBusinessChance(dto, false);
        return ResponseResult.success(result);
    }

    /**
     * 【商机】跟进
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "商机-跟进", tags = "商机管理-小程序")
    @PostMapping("/follow")
    public ResponseResult<Boolean> follow(@RequestBody @Valid FollowBusinessChanceDTO dto) {
        Boolean result = businessChanceService.followBusinessChance(dto);
        return ResponseResult.success(result);
    }

//    /**
//     * 【商机】跟进
//     *
//     * @return
//     */
//    @ApiOperation(value = "商机-跟进是阶段为赢单关闭", tags = "商机管理-小程序")
//    @PostMapping("/followChanceIsWinClose")
//    public ResponseResult<ConvertToCustomerReqDTO> followChanceIsWinClose(@RequestParam Integer stage, @RequestParam String businessChanceId) {
//        if (CHANCE_WIN_CLOSE != stage) {
//            throw new ResolinkRuntimeException("10004");
//        }
//        ConvertToCustomerReqDTO result = businessChanceService.followChanceIsWinClose(stage, businessChanceId);
//        return ResponseResult.success(result);
//    }

}
