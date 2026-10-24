package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.CardDTO;
import com.crlink.smartpark.device.model.*;
import com.crlink.smartpark.device.model.query.CardQuery;
import com.crlink.smartpark.device.service.CardService;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * park-device
 * 卡片管理
 * */
@Slf4j
@RestController
@RequestMapping("/card")
@Api(tags = "卡片管理")
@Validated
public class CardController {

    @Autowired
    private CardService cardService;


    @ApiOperation(value = "卡片管理-新增", tags = "卡片管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid CardReqDTO reqDTO) throws Exception {
        reqDTO.setId(null);
        return ResponseResult.success(cardService.saveCard(reqDTO));
    }

    @ApiOperation(value = "卡片管理-修改", tags = "卡片管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid CardUpdateReqDTO reqDTO) throws Exception {
        return ResponseResult.success(true);
    }


    @ApiOperation(value = "卡片管理-删除", tags = "卡片管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) throws Exception {
        return ResponseResult.success(cardService.removeCardById(id));
    }


    @ApiOperation(value = "卡片管理-根据ID查询", tags = "卡片管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<CardDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(cardService.getCardById(id));
    }


    @ApiOperation(value = "卡片管理-分页查询", tags = "卡片管理")
    @PostMapping("/listCardPage")
    public ResponseResult<PageInfo<CardDTO>> listCardPage(@RequestBody @Valid CardQuery query) {
        return ResponseResult.success(cardService.listCardPage(query));
    }

    @ApiOperation(value = "卡片管理-挂失/取消挂失/解绑", tags = "卡片管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@RequestBody @Valid CardUpdateStatusReqDTO reqDTO) throws Exception {
        Boolean result = cardService.updateStatus(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "卡片管理-换卡", tags = "卡片管理")
    @PostMapping("/exchangeCard")
    public ResponseResult<Boolean> exchangeCard(@RequestBody @Valid CardExchangeReqDTO reqDTO) throws Exception {
        Boolean result = cardService.exchangeCard(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "卡片管理-绑定", tags = "卡片管理")
    @PostMapping("/boundCard")
    public ResponseResult<Boolean> boundCard(@RequestBody @Valid CardBoundReqDTO reqDTO) throws Exception {
        Boolean result = cardService.boundCard(reqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 卡片管理-导出
     *
     * @return
     */
    @ApiOperation(value = "卡片管理-导出", tags = "卡片管理")
    @PostMapping("/export")
    public void download(@RequestBody CardQuery query) {
        cardService.export(query);
    }

    @ApiOperation(value = "卡片管理-根据员工ID查询所有可用卡号", tags = "卡片管理")
    @GetMapping("/findAllCardByEmployeeId/{employeeId}")
    public Set<String> findAllCardByEmployeeId(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId) {
        return cardService.findAllCardByEmployeeId(employeeId);
    }

    @ApiOperation(value = "卡片管理-根据员工ID查询是否有关联的卡片", tags = "卡片管理")
    @GetMapping("/containsEmployeeRelationCard/{employeeId}")
    public Boolean containsEmployeeRelationCard(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId) {
        return cardService.containsEmployeeRelationCard(employeeId);
    }
}

