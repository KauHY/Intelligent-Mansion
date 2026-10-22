package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.dto.OrderDTO;
import com.crlink.smartpark.customer.dto.OrderProcessRecordDTO;
import com.crlink.smartpark.customer.entity.GoodsRelease;
import com.crlink.smartpark.customer.entity.Order;
import com.crlink.smartpark.customer.entity.OrderProcessRecord;
import com.crlink.smartpark.customer.mapper.OrderMapper;
import com.crlink.smartpark.customer.model.*;
import com.crlink.smartpark.customer.model.query.OrderQuery;
import com.crlink.smartpark.customer.model.query.WxOrderQuery;
import com.crlink.smartpark.customer.service.GoodsReleaseService;
import com.crlink.smartpark.customer.service.OrderProcessRecordService;
import com.crlink.smartpark.customer.service.OrderService;
import com.crlink.smartpark.customer.vo.OrderListVO;
import com.crlink.smartpark.customer.vo.OrderStatistic;
import com.crlink.smartpark.customer.vo.OrderVO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.field.EmployeeFiled;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.*;
import static com.crlink.smartpark.common.constants.enums.OperationScreenConstants.*;
import static com.crlink.smartpark.customer.common.Constant.*;
import static com.crlink.smartpark.customer.common.Constant.GoodsReleaseStatusEnum.*;
import static com.crlink.smartpark.customer.common.enums.OrderEnum.*;
import static com.crlink.smartpark.customer.constants.Constant.ORDER_TYPE_COMPLAINT;
import static com.crlink.smartpark.customer.constants.Constant.ORDER_TYPE_REPAIR;

/**
 * <p>
 * 报事投诉、报修服务表 服务实现类
 * </p>
 * */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Autowired
    private OrderProcessRecordService orderProcessRecordService;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private GoodsReleaseService goodsReleaseService;


    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Order, T> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery()
                .select(Order::getId, sValue)
                .in(Order::getId, ids)
                .list().stream().collect(HashMap::new, (map, entity) ->
                        map.put(entity.getId(), sValue.apply(entity)), HashMap::putAll);
        return result;
    }

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 存储对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createOrder(OrderDTO dto) {
        Order order = MyBeanUtils.copy(dto, Order::new);
        EntityUserUtil.setAll(order);
        String orderNo = getOrderNo(order.getOrderType());
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(orderNo);
        String no = new StringBuilder(orderNo).append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                .append(numberSeq.getValue()).toString();
        order.setOrderNo(no);
        Integer state = ORDER_STATE_WAIT_ASSIGN.code;
        order.setOrderState(state);
        this.save(order);
        OrderProcessRecord record = newOrderRecord(order, RECORD_STATE_CREATE_ORDER.code);
        return orderProcessRecordService.save(record);
    }


    /**
     * 获取工单详情
     *
     * @param id     查询条件
     * @param fromWx
     * @return
     */
    @Override
    public OrderVO getOrderById(String id, Boolean fromWx) {
        Order entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("50007"));
        OrderVO vo = order2OrderVO(Lists.newArrayList(entity), OrderVO::new).get(0);
        vo.setRecordVOList(orderProcessRecordService.getRecordList(vo, fromWx));
        return vo;
    }

    /**
     * 分页查询-PC
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<OrderListVO> listOrderPage(OrderQuery condition) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        condition.setParkOrgIds(parkOrgIds);
        LambdaQueryWrapper<Order> queryWrapper = getConditionWrapper(condition);
        condition.startPage();
        List<Order> orderList = this.list(queryWrapper);
        MyPageInfoUtils.isEmptyPage(orderList);
        List<OrderListVO> orderListVOS = MyBeanUtils.copyList(orderList, OrderListVO::new);
        return condition.createPageInfo(orderListVOS);
    }

    /**
     * 分页查询-小程序
     *
     * @param orderQuery
     * @return
     */
    @Override
    public PageInfo<OrderListVO> listWxOrderPage(WxOrderQuery orderQuery) {
        LambdaQueryWrapper<Order> queryWrapper = getWxConditionWrapper(orderQuery);
        orderQuery.startPage();
        List<Order> orderList = this.list(queryWrapper);
        MyPageInfoUtils.isEmptyPage(orderList);
        List<OrderListVO> orderListVOS = MyBeanUtils.copyList(orderList, OrderListVO::new, (s, t) -> {
            //转办中，返回转办员对应的employeeId
            if (ORDER_STATE_TRANSFER.code.equals(s.getOrderState())) {
                t.setEmployeeId(s.getTransferMan());
            }
        });
        return orderQuery.createPageInfo(orderListVOS);
    }

    /**
     * C端小程序中筛选园区：筛选出相关工单的所有园区
     *
     * @return
     */
    @Override
    public List<DictVO> listOrderParkDict() {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>();
        queryWrapper.select(Order::getParkOrgId)
                .eq(Order::getCreateBy, SystemUtil.getInstance().getCurrentUserId())
                .or().eq(Order::getOrderOwnerMobile, SystemUtil.getInstance().getWxUserMobile());
        Set<String> parkOrgIds = this.list(queryWrapper).parallelStream().map(Order::getParkOrgId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            return Lists.newLinkedList();
        }
        Map<String, String> parkOrgId2NameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        List<DictVO> parkOrgNames = parkOrgIds.parallelStream()
                .filter(t -> StringUtils.isNotBlank(parkOrgId2NameMap.get(t)))
                .map(t -> new SimpleNode(t, parkOrgId2NameMap.get(t), SimpleNode.TypeEnum.PARK.getCode()))
                .collect(Collectors.toList());
        return parkOrgNames;
    }

    /**
     * 派单
     *
     * @param assignOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignUser(AssignOrderDTO assignOrderDTO) {
        String orderId = assignOrderDTO.getOrderId();
        Map<String, Integer> orderId2OrderState = this.id2ValueMap(Sets.newHashSet(orderId), Order::getOrderState);
        if (ObjectUtil.isEmpty(orderId2OrderState)) {
            throw new ResolinkRuntimeException("50002");
        }
        Order order = new Order(orderId, ORDER_STATE_WAIT_HANDLE.code);
        order.setEmployeeId(assignOrderDTO.getHandleUserId());
        EntityUserUtil.setUpdateBy(order);
        this.updateById(order);
        OrderProcessRecord record = newOrderRecord(order, RECORD_STATE_ASSIGN_ORDER.code);
        record.setAskFinishTime(assignOrderDTO.getAskFinishTime());
        String employeeId = assignOrderDTO.getHandleUserId();
        //获取处理人手机号
        record.setEmployeeId(employeeId);
        Map<String, Object> employeeInfo = getEmployeeMobileAndCompanyId(employeeId);
        if (CollectionUtil.isNotEmpty(employeeInfo)) {
            String mobile = (String) employeeInfo.get(USER_MOBILE);
            record.setEmployeeMobile(mobile);
        }
        record.setCompanyId(assignOrderDTO.getCompanyId());
        EntityUserUtil.setAll(record);
        return orderProcessRecordService.save(record);
    }

    /**
     * 退单
     *
     * @param returnOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean returnOrder(ReturnOrderDTO returnOrderDTO) {
        String orderId = returnOrderDTO.getId();
        Optional.ofNullable(getById(orderId)).orElseThrow(() -> new ResolinkRuntimeException("50002"));
        Order order = new Order(orderId, ORDER_STATE_WAIT_ASSIGN.code);
        EntityUserUtil.setUpdateBy(order);
        this.updateById(order);

        OrderProcessRecord record = preRecordToCurrentRecord(order, RECORD_STATE_RETURN_ORDER.code);
        record.setReturnOrderReason(returnOrderDTO.getReturnOrderReason());
        return orderProcessRecordService.save(record);
    }

    /**
     * 转办
     *
     * @param transferOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transferOrder(TransferOrderDTO transferOrderDTO) {
        String orderId = transferOrderDTO.getId();
        String employeeId = transferOrderDTO.getEmployeeId();
        Integer type = id2ValueMap(Sets.newHashSet(orderId), Order::getOrderType).get(orderId);
        Optional.ofNullable(type).orElseThrow(() -> new ResolinkRuntimeException("50002"));
        if (!type.equals(ORDER_TYPE_REPAIR)) {
            throw new ResolinkRuntimeException("10023");
        }
        Order order = new Order(orderId, ORDER_STATE_TRANSFER.code);
        order.setTransferMan(employeeId);
        EntityUserUtil.setUpdateBy(order);
        this.updateById(order);
        OrderProcessRecord record = newOrderRecord(order, RECORD_STATE_TRANSFER.code);
        record.setRemark(transferOrderDTO.getRemark());
        record.setEmployeeId(employeeId);
        //获取处理人手机号、所在公司
        Map<String, Object> employeeInfo = getEmployeeMobileAndCompanyId(employeeId);
        if (CollectionUtil.isNotEmpty(employeeInfo)) {
            String mobile = (String) employeeInfo.get(USER_MOBILE);
            String companyId = (String) employeeInfo.get(EmployeeFiled.COMPANY_ID);
            record.setEmployeeMobile(mobile);
            record.setCompanyId(companyId);
        }
//        String mobile = (String) getEmployeeMobileAndCompanyId(employeeId).get(USER_MOBILE);
//        String companyId = (String) getEmployeeMobileAndCompanyId(employeeId).get(EmployeeFiled.COMPANY_ID);
//        record.setEmployeeMobile(mobile);
//        record.setCompanyId(companyId);
        return orderProcessRecordService.save(record);
    }

    /**
     * 转办处理
     *
     * @param transferHandleOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transferHandleOrder(TransferHandleOrderDTO transferHandleOrderDTO) {
        String orderId = transferHandleOrderDTO.getId();
        Optional.ofNullable(id2ValueMap(Sets.newHashSet(orderId), Order::getOrderType)).orElseThrow(() -> new ResolinkRuntimeException("50002"));
        Order order = new Order(orderId, ORDER_STATE_WAIT_HANDLE.code);
        EntityUserUtil.setUpdateBy(order);
        this.updateById(order);
        OrderProcessRecord record = newOrderRecord(order, RECORD_STATE_TRANSFER_HANDLE.code);
        record.setResult(transferHandleOrderDTO.getResult());
        return orderProcessRecordService.save(record);
    }

    /**
     * 工单统计（近三十天）
     * 服务访评：进行中指待派单+待处理；已完成。
     * 报修服务：进行中指待派单+待处理+转办中；已完成。
     * 物品放行：进行中指待审核；已完成指审核通过+审核不通过
     * key：1报事服务(服务访评)、2报修服务、3 物品放行
     *
     * @return
     */
    @Override
    public Map<Integer, OrderStatistic> getOrderStatistic(String parkOrgId) {
        //投诉、报修
        LocalDateTime now = LocalDateTime.now();
        List<Order> orderList = this.lambdaQuery()
                .select(Order::getOrderType, Order::getOrderState, Order::getId)
                .eq(Order::getParkOrgId, parkOrgId)
                .between(Order::getCreateTime, LocalDateTimeUtil.beginOfDay(now.minusDays(30)), now)
                .in(Order::getOrderState, ORDER_STATE_WAIT_ASSIGN.code, ORDER_STATE_WAIT_HANDLE.code,
                        ORDER_STATE_FINISH.code, ORDER_STATE_TRANSFER.code).list();
        Map<Integer, OrderStatistic> result = new HashMap<>(3);
        OrderStatistic statistic = new OrderStatistic(0, 0);
        result.put(COMPLAINT, statistic);
        result.put(REPAIR, statistic);
        result.put(GOODS_RELEASE, statistic);
        if (CollectionUtil.isNotEmpty(orderList)) {
            Map<Integer, List<Order>> maps = orderList.parallelStream().collect(Collectors.groupingBy(Order::getOrderType));
            maps.forEach((k, v) -> setOrderCount(v, result, k));
        }
        //物品放行
        List<GoodsRelease> goodsReleaseList = goodsReleaseService.lambdaQuery()
                .select(GoodsRelease::getId, GoodsRelease::getStatus)
                .eq(GoodsRelease::getParkOrgId, parkOrgId)
                .between(GoodsRelease::getCreateTime, LocalDateTimeUtil.beginOfDay(now.minusDays(30)), now)
                .in(GoodsRelease::getStatus, NOT_CHECK.state, CHECK_PASS.state, CHECK_REJECT.state)
                .list();
        if (CollectionUtil.isNotEmpty(goodsReleaseList)) {
            setGoodsReleaseCount(goodsReleaseList, result);
        }
        return result;
    }

    /**
     * 工单响应（近三十天累计）
     * 1、处理工单数量：已完成状态的服务访评、报修服务、物品放行累计工单数；
     * 2、响应工单数量：进行中状态的服务访评、报修服务、物品放行累计工单数；
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public OrderStatistic getOrderResponse(String parkOrgId) {
        Map<Integer, OrderStatistic> orderStatistic = getOrderStatistic(parkOrgId);
        AtomicReference<Integer> finishCount = new AtomicReference<>(0);
        AtomicReference<Integer> ongoingCount = new AtomicReference<>(0);
        orderStatistic.values().forEach(t -> {
            finishCount.updateAndGet(v -> v + t.getFinishCount());
            ongoingCount.updateAndGet(v -> v + t.getOngoingCount());
        });
        OrderStatistic result = new OrderStatistic(finishCount.get(), ongoingCount.get());
        return result;
    }

    private void setOrderCount(List<Order> list, Map<Integer, OrderStatistic> result, Integer type) {
        AtomicReference<Integer> finishCount = new AtomicReference<>(0);
        list.forEach(order -> {
            if (ORDER_STATE_FINISH.code.equals(order.getOrderState())) {
                finishCount.getAndSet(finishCount.get() + 1);
            }
        });
        Integer ongoingCount = list.size() - finishCount.get();
        result.put(type, new OrderStatistic(finishCount.get(), ongoingCount));
    }

    private void setGoodsReleaseCount(List<GoodsRelease> list, Map<Integer, OrderStatistic> result) {
        AtomicReference<Integer> ongoingCount = new AtomicReference<>(0);
        list.forEach(t -> {
            if (NOT_CHECK.state == t.getStatus()) {
                ongoingCount.getAndSet(ongoingCount.get() + 1);
            }
        });
        Integer finishCount = list.size() - ongoingCount.get();
        result.put(GOODS_RELEASE, new OrderStatistic(finishCount, ongoingCount.get()));
    }

    /**
     * 初始化工单记录对象
     *
     * @param order
     * @param recordState
     * @return
     */
    private OrderProcessRecord newOrderRecord(Order order, Integer recordState) {
        OrderProcessRecord record = new OrderProcessRecord();
        record.setOrderId(order.getId());
        record.setOrderState(order.getOrderState());
        record.setState(recordState);
        EntityUserUtil.setAll(record);
        return record;
    }


    @Override
    public void test() {
        this.handleExceptionOrder();
    }

    /**
     * 如工单的当前处理人已在员工列表删除、或该员工为“禁用”状态、或者该员工与当前岗位无关联时，则工单打回至上一节点处理人。
     * 退单人：系统自动处理
     * 退单原因：因员工不存在/岗位调整，系统自动退单
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleExceptionOrder() {
        log.info("开始处理异常投诉、报修工单");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        //查询出可能异常的工单数据：orderState为2
        List<Order> orders = this.lambdaQuery().select(Order::getId, Order::getEmployeeId, Order::getOrderType)
                .le(Order::getUpdateTime, now).ge(Order::getUpdateTime, yesterday)
                .eq(Order::getOrderState, ORDER_STATE_WAIT_HANDLE.code).list();
        if (CollUtil.isEmpty(orders)) {
            log.info("无异常投诉、报修工单");
            return;
        }
//        Map<String, String> orderId2EmployeeIdMap = orders.parallelStream().collect(Collectors.toMap(Order::getId, Order::getEmployeeId));
        Set<String> employeeIds = orders.parallelStream().map(Order::getEmployeeId).collect(Collectors.toSet());
        Map<String, Set<Integer>> employeeJobCodesMap = employeeProvider.getJobCodeByEmployeeIds(employeeIds);
        List<OrderProcessRecord> records = Lists.newArrayList();
        List<Order> exceptionOrders = orders.parallelStream()
                .filter(o -> !isJobMatch(o.getOrderType(), employeeJobCodesMap.get(o.getEmployeeId())))
                .map(t -> {
                    t.setOrderState(ORDER_STATE_WAIT_ASSIGN.code);
                    t.setUpdateBy(CREATE_BY_SYSTEM);
                    records.add(buildExceptionRecord(t));
                    return t;
                })
                .collect(Collectors.toList());
        log.info("存在异常投诉、报修工单，开始处理，{}", JSONUtil.toJsonStr(exceptionOrders));
        this.updateBatchById(exceptionOrders);
        orderProcessRecordService.saveBatch(records);

    }


    /**
     * 结单
     *
     * @param finishOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean finishOrder(FinishOrderDTO finishOrderDTO) {
        String orderId = finishOrderDTO.getId();
        Optional.ofNullable(getById(orderId)).orElseThrow(() -> new ResolinkRuntimeException("50002"));
        Order order = new Order(orderId, ORDER_STATE_FINISH.code);
        EntityUserUtil.setUpdateBy(order);
        this.updateById(order);
        OrderProcessRecord record = preRecordToCurrentRecord(order, RECORD_STATE_FINISH_ORDER.code);
        record.setFinishTime(finishOrderDTO.getFinishTime());
        record.setRemark(finishOrderDTO.getRemark());
        orderProcessRecordService.save(record);
        return true;
    }

    /**
     * 撤单
     *
     * @param cancelOrderDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(CancelOrderDTO cancelOrderDTO) {
        String orderId = cancelOrderDTO.getOrderId();
        Map<String, Integer> orderId2OrderState = this.id2ValueMap(Sets.newHashSet(orderId), Order::getOrderState);
        if (ObjectUtil.isEmpty(orderId2OrderState)) {
            throw new ResolinkRuntimeException("50002");
        }
        //在小程序端由于页面加载原因、可能在撤单后仍可以再次点击撤单按钮造成第二次撤单，所以进行检验
        if (ORDER_STATE_CANCEL.code.equals(orderId2OrderState.get(orderId))) {
            throw new ResolinkRuntimeException("50003");
        }
        Order order = new Order(orderId, ORDER_STATE_CANCEL.code);
        EntityUserUtil.setUpdateBy(order);
        this.updateById(order);
        OrderProcessRecord record = newOrderRecord(order, RECORD_STATE_CANCEL_ORDER.code);
        record.setCancelReason(cancelOrderDTO.getCancelReason());
        return orderProcessRecordService.save(record);
    }


    /**
     * 退单、结单时获取上一个处理流程
     *
     * @param order
     * @return
     */
    private OrderProcessRecord preRecordToCurrentRecord(Order order, Integer recordState) {
        String orderId = order.getId();
        OrderProcessRecordDTO preRecordDTO = orderProcessRecordService.getPreRecord(orderId);
        if (preRecordDTO == null) {
            throw new ResolinkRuntimeException("50007");
        }
        OrderProcessRecord record = newOrderRecord(order, recordState);
        record.setPreProNo(preRecordDTO.getId());
        record.setEmployeeId(preRecordDTO.getEmployeeId());
        record.setPreEmployeeId(preRecordDTO.getEmployeeId());
        return record;
    }

    private String getOrderNo(Integer orderType) {
        if (orderType.equals(ORDER_TYPE_COMPLAINT)) {
            return ORDER_NO_PREFIX_TS;
        }
        if (orderType.equals(ORDER_TYPE_REPAIR)) {
            return ORDER_NO_PREFIX_BX;
        }
        return null;
    }

    /**
     * 封装投诉、报修小程序端分页查询条件
     *
     * @param orderQuery
     * @return
     */
    private LambdaQueryWrapper<Order> getWxConditionWrapper(WxOrderQuery orderQuery) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        Set<String> parkOrgIds = SystemUtil.getInstance().getTenantParkOrgIds(headOrgId);
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
//        queryWrapper.select(Order::getId, Order::getOrderTypeItem, Order::getParkOrgId, Order::getOrderState,
//                Order::getOrderContent, Order::getOrderOwnerName, Order::getCreateTime, Order::getOrderType, Order::getCreateBy);
        Integer state = orderQuery.getOrderState();
        Integer orderType = orderQuery.getOrderType();
        queryWrapper.eq(Order::getOrderType, orderType)
                .eq(Objects.nonNull(state), Order::getOrderState, state)
                .in(Order::getParkOrgId, parkOrgIds);
        String source = SystemUtil.getInstance().getSource();
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        //运营端
        if (USER_WX_SOURCE_B.equals(source)) {
            String employeeId = SystemUtil.getInstance().getEmployeeId();
            Set<Integer> jobCodes = SystemUtil.getInstance().getEmployeeJobCodes();
//            String parkOrgId = getParkOrgIdByEmployeeId(employeeId);
            String parkOrgId = SystemUtil.getInstance().getParkOrgIdByEmployeeId();
            queryWrapper.eq(Order::getParkOrgId, parkOrgId);
            getQueryByJob(employeeId, orderType, jobCodes, queryWrapper);
        }
        //C端小程序
        if (USER_WX_SOURCE_C.equals(source)) {
            String parkOrgId = orderQuery.getParkOrgId();
            queryWrapper.eq(StringUtils.isNotBlank(parkOrgId), Order::getParkOrgId, parkOrgId);
            queryWrapper.and(wrapper -> wrapper.eq(Order::getCreateBy, currentUserId)
                    .or().eq(Order::getOrderOwnerMobile, SystemUtil.getInstance().getWxUserMobile()));
        }
        queryWrapper.orderByDesc(Order::getCreateTime);
        return queryWrapper;
    }

    /**
     * 根据岗位获取查询条件
     * 如果具有管理员或者客服岗位，可看到园区内所有相关数据：ADMINISTRATOR_JOB_CODE、CUSTOMER_SERVICE_JOB_CODE
     *
     * @param employeeId
     * @param orderType
     * @param jobCodes
     * @return
     */
    private void getQueryByJob(String employeeId, Integer orderType, Set<Integer> jobCodes, LambdaQueryWrapper<Order> queryWrapper) {
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        if (CollectionUtil.isNotEmpty(jobCodes)) {
            if (!(jobCodes.contains(ADMINISTRATOR_JOB_CODE.code) || jobCodes.contains(CUSTOMER_SERVICE_JOB_CODE.code))) {
                if (isJobMatch(orderType, jobCodes)) {
                    //如果具有投诉、报修相关岗位，则可以看到当前用户创建的当前园区的工单申请，“反馈人手机号”为当前用户手机号的工单、以及派单给当前员工的工单
                    queryWrapper.and(wrapper -> wrapper.eq(Order::getCreateBy, currentUserId)
                            .or().eq(Order::getEmployeeId, employeeId));
                } else {
                    queryWrapper.eq(Order::getCreateBy, currentUserId);
                }
            }
        } else {
            //如果没有任何岗位，当前用户创建的当前园区的工单申请
            queryWrapper.eq(Order::getCreateBy, currentUserId);
        }
    }


    private LambdaQueryWrapper<Order> getConditionWrapper(OrderQuery condition) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>();
        if (StringUtils.isNotBlank(condition.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper.like(Order::getOrderContent, condition.getKeyword())
                    .or().like(Order::getOrderOwnerName, condition.getKeyword())
                    .or().like(Order::getOrderOwnerMobile, condition.getKeyword()));
        }
        queryWrapper.eq(condition.getOrderTypeItem() != null, Order::getOrderTypeItem, condition.getOrderTypeItem());
        queryWrapper.eq(StringUtils.isNotBlank(condition.getParkOrgId()), Order::getParkOrgId, condition.getParkOrgId())
                .in(Order::getParkOrgId, condition.getParkOrgIds());
        queryWrapper.ge(Objects.nonNull(condition.getCreateStartTime()), Order::getCreateTime, condition.getCreateStartTime());
        queryWrapper.le(Objects.nonNull(condition.getCreateEndTime()), Order::getCreateTime, condition.getCreateEndTime());
        queryWrapper.eq(condition.getOrderState() != null, Order::getOrderState, condition.getOrderState());
        queryWrapper.eq(Order::getOrderType, condition.getOrderType());
        queryWrapper.orderByDesc(Order::getCreateTime);
        return queryWrapper;
    }


    /**
     * 获取员工手机号、所在公司
     *
     * @param employeeId
     * @return
     */
    private Map<String, Object> getEmployeeMobileAndCompanyId(String employeeId) {
        Map<String, Map<String, Object>> employeeMap = employeeProvider.queryEmployeeColumns(
                EmployeeQueryDTO.employeeIdsBuildDTO(Sets.newHashSet(employeeId), USER_MOBILE, EmployeeFiled.COMPANY_ID));
        Map<String, Object> result = employeeMap.get(employeeId);
        if (Objects.isNull(result)) {
            return Collections.emptyMap();
        }
        return result;
    }

    /**
     * 判断当前员工的岗位是否为相关岗位
     * 投诉：保洁、保安、维修、绿化、行政
     * 报修：维修
     *
     * @param orderType
     * @param jobCodes
     * @return
     */
    private boolean isJobMatch(Integer orderType, Set<Integer> jobCodes) {
        if (ObjectUtil.isEmpty(jobCodes)) {
            return false;
        }
        if (orderType.equals(ORDER_TYPE_COMPLAINT)) {
            //投诉工单
//            Set<Integer> complaintJobCodes = Arrays.stream(ComplaintJobTypeEnum.values())
//                    .map(t -> t.code).collect(Collectors.toSet());
            Set<Integer> complaintJobCodes = Sets.newHashSet(
                    REPAIR_JOB_CODE.code, CLEANER_JOB_CODE.code, SECURITY_JOB_CODE.code,
                    GREEN_JOB_CODE.code, ADMINISTRATION_JOB_CODE.code);
            if (CollectionUtil.containsAny(jobCodes, complaintJobCodes)) {
                return true;
            }
        } else if (orderType.equals(ORDER_TYPE_REPAIR) && jobCodes.contains(REPAIR_JOB_CODE.code)) {
            //维修工单
            return true;
        }
        return false;
    }

//    /**
//     * 根据员工Id获取公司所在园区
//     *
//     * @param employeeId
//     * @return
//     */
//    private String getParkOrgIdByEmployeeId(String employeeId) {
//        EmployeeQueryDTO queryDTO = EmployeeQueryDTO.employeeIdsBuildDTO(Sets.newHashSet(employeeId), "park_org_id");
//        Map<String, Map<String, Object>> employeeColumns = employeeProvider.queryEmployeeColumns(queryDTO);
//        String parkOrgId = (String) employeeColumns.get(employeeId).get("park_org_id");
//        return parkOrgId;
//    }
//
//    private String getCompanyIdByEmployeeId(String employeeId) {
//        EmployeeQueryDTO queryDTO = EmployeeQueryDTO.employeeIdsBuildDTO(Sets.newHashSet(employeeId), "company_id");
//        Map<String, Map<String, Object>> employeeColumns = employeeProvider.queryEmployeeColumns(queryDTO);
//        String companyId = (String) employeeColumns.get(employeeId).get("company_id");
//        return companyId;
//    }


    private List<OrderVO> order2OrderVO(List<Order> orderList, Supplier<OrderVO> supplier) {
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        Set<String> parkOrgIds = Sets.newHashSet();
        Set<String> userIds = Sets.newHashSet();
        orderList.forEach(t -> {
            parkOrgIds.add(t.getParkOrgId());
            userIds.add(t.getCreateBy());
        });
        Map<String, String> parkOrgId2NameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        Map<String, Map<String, Object>> userId2NameMap = commonProvider.getUsernameByUserId(userIds, Sets.newHashSet(USER_REAL_NAME));
        List<OrderVO> result = orderList.parallelStream().map(t -> {
            OrderVO orderVO = supplier.get();
            BeanUtils.copyProperties(t, orderVO);
            orderVO.setCreateByName((String) userId2NameMap.get(t.getCreateBy()).get(USER_REAL_NAME));
            orderVO.setParkOrgName(parkOrgId2NameMap.get(t.getParkOrgId()));
            orderVO.setIsCreateByCurrentUser(isCreateByCurrentUser(t, currentUserId));
            return orderVO;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * 异常工单退单记录对象创建
     *
     * @param t
     * @return
     */
    private OrderProcessRecord buildExceptionRecord(Order t) {
        OrderProcessRecord record = new OrderProcessRecord();
        record.setOrderId(t.getId());
        record.setReturnOrderReason(SYSTEM_RETURN_ORDER_REASON);
        record.setOrderState(t.getOrderState());
        record.setState(RECORD_STATE_RETURN_ORDER.code);
        record.setCreateBy(CREATE_BY_SYSTEM);
        record.setUpdateBy(CREATE_BY_SYSTEM);
        return record;
    }

    /**
     * 是否为当前用户创建：true 是，false 否"
     *
     * @param order
     * @param currentUserId
     * @return
     */
    private Boolean isCreateByCurrentUser(Order order, String currentUserId) {
        if (order.getCreateBy().equals(currentUserId)) {
            return true;
        }
        return false;
    }

}
