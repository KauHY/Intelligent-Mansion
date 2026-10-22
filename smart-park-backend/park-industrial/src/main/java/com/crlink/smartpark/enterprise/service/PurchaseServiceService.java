package com.crlink.smartpark.enterprise.service;

import com.crlink.smartpark.enterprise.entity.PurchaseService;
import com.crlink.smartpark.enterprise.model.StateReqDTO;
import com.crlink.smartpark.enterprise.model.query.PurchaseServiceQuery;
import com.crlink.smartpark.enterprise.model.PurchaseServiceReqDTO;
import com.crlink.smartpark.industrial.dto.PurchaseServiceDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 采购服务 服务类
 * </p>
 * */
public interface PurchaseServiceService extends IService<PurchaseService> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<PurchaseService, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdatePurchaseService(PurchaseServiceReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    PurchaseServiceDTO getPurchaseServiceById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<PurchaseServiceDTO> listPurchaseServicePage(PurchaseServiceQuery query);

    Boolean changeState(StateReqDTO reqDTO);
}
