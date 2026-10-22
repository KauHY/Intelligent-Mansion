package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.ProReceiveFlow;
import com.crlink.smartpark.customer.controller.request.query.ProReceiveFlowQuery;
import com.crlink.smartpark.customer.model.dto.ProReceiveFlowReqDTO;
import com.crlink.smartpark.customer.dto.ProReceiveFlowDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 物品领用流程 服务类
 * </p>
 * */
public interface ProReceiveFlowService extends IService<ProReceiveFlow> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProReceiveFlow, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProReceiveFlow(ProReceiveFlowReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ProReceiveFlowDTO getProReceiveFlowById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProReceiveFlowDTO> listProReceiveFlowPage(ProReceiveFlowQuery query);
}
