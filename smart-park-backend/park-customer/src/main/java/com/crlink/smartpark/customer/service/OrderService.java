package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.dto.OrderDTO;
import com.crlink.smartpark.customer.entity.Order;
import com.crlink.smartpark.customer.model.*;
import com.crlink.smartpark.customer.model.query.OrderQuery;
import com.crlink.smartpark.customer.model.query.WxOrderQuery;
import com.crlink.smartpark.customer.vo.OrderListVO;
import com.crlink.smartpark.customer.vo.OrderStatistic;
import com.crlink.smartpark.customer.vo.OrderVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报事投诉、报修服务表 服务类
 * </p>
 * */
public interface OrderService extends IService<Order> {

    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Order, T> sValue);
    /**
     * 创建工单
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean createOrder(OrderDTO dto);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    OrderVO getOrderById(String id, Boolean fromWx);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<OrderListVO> listOrderPage(OrderQuery condition);

    /**
     * 派单，分配人员
     *
     * @param assignOrderDTO
     * @return
     */
    Boolean assignUser(AssignOrderDTO assignOrderDTO);

    /**
     * 撤单
     *
     * @param cancelOrderDTO
     * @return
     */
    Boolean cancelOrder(CancelOrderDTO cancelOrderDTO);


    /**
     * 分页查询——小程序
     *
     * @param orderQuery
     * @return
     */
    PageInfo<OrderListVO> listWxOrderPage(WxOrderQuery orderQuery);

    /**
     * flag:false代表正常流程；true代表异常处理
     *
     * @param returnOrderDTO
     * @return
     */
    Boolean returnOrder(ReturnOrderDTO returnOrderDTO);

    /**
     * 结单
     *
     * @param finishOrderDTO
     * @return
     */
    Boolean finishOrder(FinishOrderDTO finishOrderDTO);

    List<DictVO> listOrderParkDict();

    void test();

    Boolean transferOrder(TransferOrderDTO transferOrderDTO);

    Boolean transferHandleOrder(TransferHandleOrderDTO transferHandleOrderDTO);

    Map<Integer, OrderStatistic> getOrderStatistic(String parkOrgId);

    OrderStatistic getOrderResponse(String parkOrgId);

//    PageInfo<OrderVO> listWxOrderClientPage(WxOrderQuery orderQuery);
}
