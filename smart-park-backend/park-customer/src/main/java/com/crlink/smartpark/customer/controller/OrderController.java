package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.dto.OrderDTO;
import com.crlink.smartpark.customer.model.AssignOrderDTO;
import com.crlink.smartpark.customer.model.CancelOrderDTO;
import com.crlink.smartpark.customer.model.query.OrderQuery;
import com.crlink.smartpark.customer.service.OrderService;
import com.crlink.smartpark.customer.vo.OrderListVO;
import com.crlink.smartpark.customer.vo.OrderStatistic;
import com.crlink.smartpark.customer.vo.OrderVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 园区运营平台-报事投诉、报修服务
 * */
@Slf4j
@RestController
@Api(tags = "报事投诉、报修服务")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "报事投诉、报修服务-新增", tags = "报事投诉、报修服务")
    @PostMapping("/create")
    public ResponseResult create(@RequestBody @Valid OrderDTO orderDTO) {
        Boolean result = orderService.createOrder(orderDTO);
        return ResponseResult.success(result);
    }


    /**
     * 报事投诉、报修服务-根据ID查询
     *
     * @param id 主键
     * @return 报事投诉、报修服务实体
     */
    @ApiOperation(value = "报事投诉、报修服务-根据ID查询", tags = "报事投诉、报修服务")
    @GetMapping("/findById/{id}")
    public ResponseResult<OrderVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        OrderVO result = orderService.getOrderById(id, false);
        return ResponseResult.success(result);
    }


    /**
     * 报事投诉、报修服务-分配人员
     *
     * @param assignOrderDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "报事投诉、报修服务-分配人员", tags = "报事投诉、报修服务")
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
    @ApiOperation(value = "报事投诉、报修服务-撤销工单", tags = "报事投诉、报修服务")
    @PostMapping(value = "/cancel")
    public ResponseResult cancel(@RequestBody @Valid CancelOrderDTO cancelOrderDTO) {
        Boolean result = orderService.cancelOrder(cancelOrderDTO);
        return ResponseResult.success(result);
    }

    /**
     * 报事投诉、报修服务-分页查询
     *
     * @param orderQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "报事投诉、报修服务-分页查询", tags = "报事投诉、报修服务")
    @PostMapping("/listOrderPage")
    public ResponseResult<PageInfo<OrderListVO>> listOrderPage(@Valid @RequestBody OrderQuery orderQuery) {
        PageInfo<OrderListVO> result = orderService.listOrderPage(orderQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "工单统计", tags = "运营大屏")
    @GetMapping("/orderStatistic")
    public ResponseResult<Map<Integer, OrderStatistic>> getOrderStatistic(@RequestParam("parkOrgId") String parkOrgId) {
        AssertUtil.requireNotNull(parkOrgId, "10016");
        Map<Integer, OrderStatistic> result = orderService.getOrderStatistic(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "工单响应", tags = "运营大屏")
    @GetMapping("/orderResponse")
    public ResponseResult<OrderStatistic> getOrderResponse(@RequestParam("parkOrgId") String parkOrgId) {
        AssertUtil.requireNotNull(parkOrgId, "10016");
        OrderStatistic result = orderService.getOrderResponse(parkOrgId);
        return ResponseResult.success(result);
    }

}

