package com.crlink.smartpark.customer.controller;

import cn.hutool.json.JSONObject;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.dto.OrderDTO;
import com.crlink.smartpark.customer.model.*;
import com.crlink.smartpark.customer.model.query.WxOrderQuery;
import com.crlink.smartpark.customer.service.OrderService;
import com.crlink.smartpark.customer.vo.OrderListVO;
import com.crlink.smartpark.customer.vo.OrderVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 园区运营平台-报事投诉、报修服务
 * */
@Slf4j
@RestController
@Api(tags = "报事投诉、报修服务-小程序")
@RequestMapping("/order/wx")
public class WxOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 报事投诉、报修服务-新增
     *
     * @param orderDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "报事投诉、报修服务-新增", tags = "报事投诉、报修服务-小程序")
    @PostMapping("/create")
    public ResponseResult create(@RequestBody @Valid OrderDTO orderDTO) {
        orderDTO.setId(null);
        Boolean result = orderService.createOrder(orderDTO);
        return ResponseResult.success(result);
    }


    /**
     * 报事投诉、报修服务-根据ID查询
     *
     * @param id 主键
     * @return 报事投诉、报修服务实体
     */
    @ApiOperation(value = "报事投诉、报修服务-根据ID查询", tags = "报事投诉、报修服务-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<OrderVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        OrderVO result = orderService.getOrderById(id, true);
        return ResponseResult.success(result);
    }

    /**
     * 报事投诉、报修服务-退单
     *
     * @param returnOrderDTO 实体对象
     */
    @ApiOperation(value = "报事投诉、报修服务-退单", tags = "报事投诉、报修服务-小程序")
    @PostMapping("/returnOrder")
    public ResponseResult returnOrder(@RequestBody @Valid ReturnOrderDTO returnOrderDTO) {
        Boolean result = orderService.returnOrder(returnOrderDTO);
        return ResponseResult.success(result);
    }

    /**
     * 报事投诉、报修服务-结单
     *
     * @param finishOrderDTO
     * @return
     */
    @ApiOperation(value = "报事投诉、报修服务-结单", tags = "报事投诉、报修服务-小程序")
    @PostMapping("/finishOrder")
    public ResponseResult finish(@RequestBody @Valid FinishOrderDTO finishOrderDTO) {
        Boolean result = orderService.finishOrder(finishOrderDTO);
        return ResponseResult.success(result);
    }


    /**
     * 报事投诉、报修服务-分配人员
     *
     * @param assignOrderDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "报事投诉、报修服务-分配人员", tags = "报事投诉、报修服务-小程序")
    @PostMapping(value = "/assignUser")
    public ResponseResult assignUser(@RequestBody @Valid AssignOrderDTO assignOrderDTO) {
        Boolean result = orderService.assignUser(assignOrderDTO);
        return ResponseResult.success(result);
    }


    /**
     * 报事投诉、报修服务-撤销工单
     *
     * @param cancelOrderDTO
     * @return
     */
    @ApiOperation(value = "报事投诉、报修服务-撤销工单", tags = "报事投诉、报修服务-小程序")
    @PostMapping(value = "/cancel")
    public ResponseResult cancel(@RequestBody @Valid CancelOrderDTO cancelOrderDTO) {
        Boolean result = orderService.cancelOrder(cancelOrderDTO);
        return ResponseResult.success(result);
    }


    /**
     * 报事投诉、报修服务-转办工单
     *
     * @param transferOrderDTO
     * @return
     */
    @ApiOperation(value = "报修服务-转办工单", tags = "报事投诉、报修服务-小程序")
    @PostMapping(value = "/transfer")
    public ResponseResult transfer(@RequestBody @Valid TransferOrderDTO transferOrderDTO) {
        Boolean result = orderService.transferOrder(transferOrderDTO);
        return ResponseResult.success(result);
    }

    /**
     * 报事投诉、报修服务-转办工单
     *
     * @param transferHandleOrderDTO
     * @return
     */
    @ApiOperation(value = "报修服务-转办处理工单", tags = "报事投诉、报修服务-小程序")
    @PostMapping(value = "/transferHandle")
    public ResponseResult transferHandle(@RequestBody @Valid TransferHandleOrderDTO transferHandleOrderDTO) {
        Boolean result = orderService.transferHandleOrder(transferHandleOrderDTO);
        return ResponseResult.success(result);
    }

    /**
     * 报事投诉、报修服务-分页查询
     *
     * @param orderQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "报事投诉、报修服务-分页查询", tags = "报事投诉、报修服务-小程序")
    @PostMapping("/listWxOrderPage")
    public ResponseResult<PageInfo<OrderListVO>> listWxOrderPage(@Valid @RequestBody WxOrderQuery orderQuery) {
        PageInfo<OrderListVO> result = orderService.listWxOrderPage(orderQuery);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "报事投诉、报修服务-筛选园区", tags = "报事投诉、报修服务-小程序")
    @GetMapping("/listOrderParkDict")
    public ResponseResult<List<DictVO>> listOrderParkDict() {
        List<DictVO> result = orderService.listOrderParkDict();
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "test", tags = "报事投诉、报修服务-小程序")
    @GetMapping("/test")
    public ResponseResult<String> test() {
        String source = SystemUtil.getInstance().getSource();
        JSONObject jsonObject = SystemUtil.getInstance().parseHeader();
        System.out.println(jsonObject);
        orderService.test();
        return ResponseResult.success(source);
    }


}

