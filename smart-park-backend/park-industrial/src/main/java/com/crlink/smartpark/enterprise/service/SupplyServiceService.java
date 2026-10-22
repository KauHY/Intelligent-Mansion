package com.crlink.smartpark.enterprise.service;

import com.crlink.smartpark.enterprise.entity.SupplyService;
import com.crlink.smartpark.enterprise.model.StateReqDTO;
import com.crlink.smartpark.enterprise.model.query.SupplyServiceQuery;
import com.crlink.smartpark.enterprise.model.SupplyServiceReqDTO;
import com.crlink.smartpark.industrial.dto.SupplyServiceDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 供应服务 服务类
 * </p>
 * */
public interface SupplyServiceService extends IService<SupplyService> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<SupplyService, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateSupplyService(SupplyServiceReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    SupplyServiceDTO getSupplyServiceById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<SupplyServiceDTO> listSupplyServicePage(SupplyServiceQuery query);

    Boolean changeState(StateReqDTO reqDTO);
}
