package com.crlink.smartpark.enterprise.service;

import com.crlink.smartpark.enterprise.entity.EnterpriseService;
import com.crlink.smartpark.enterprise.model.StateReqDTO;
import com.crlink.smartpark.enterprise.model.query.EnterpriseServiceQuery;
import com.crlink.smartpark.enterprise.model.EnterpriseServiceReqDTO;
import com.crlink.smartpark.industrial.dto.EnterpriseServiceDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 企业服务 服务类
 * </p>
 * */
public interface EnterpriseServiceService extends IService<EnterpriseService> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<EnterpriseService, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateEnterpriseService(EnterpriseServiceReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    EnterpriseServiceDTO getEnterpriseServiceById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<EnterpriseServiceDTO> listEnterpriseServicePage(EnterpriseServiceQuery query);

    Boolean changeState(StateReqDTO reqDTO);
}
