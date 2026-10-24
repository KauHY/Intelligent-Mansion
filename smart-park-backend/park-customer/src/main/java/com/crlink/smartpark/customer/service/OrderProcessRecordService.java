package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.dto.OrderProcessRecordDTO;
import com.crlink.smartpark.customer.entity.Order;
import com.crlink.smartpark.customer.entity.OrderProcessRecord;
import com.crlink.smartpark.customer.vo.OrderProcessRecordVO;
import com.crlink.smartpark.customer.vo.OrderVO;

import java.util.List;

/**
 * <p>
 * 报事报修处理流程表 服务类
 * </p>
 * */
public interface OrderProcessRecordService extends IService<OrderProcessRecord> {

    /**
     * 查询工单流程
     *
     * @param
     * @return
     */
    List<OrderProcessRecordVO> getRecordList(OrderVO vo, Boolean fromWx);

    /**
     * 查询工单上一流程
     *
     * @param orderId
     * @return 分页结果
     */
    OrderProcessRecordDTO getPreRecord(String orderId);


}
