package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.ProInventoryManager;
import com.crlink.smartpark.customer.model.dto.ProInventoryManagerReqDTO;
import com.crlink.smartpark.customer.dto.ProInventoryManagerDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.model.query.ProInventoryManagerQuery;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 盘点管理 服务类
 * </p>
 * */
public interface ProInventoryManagerService extends IService<ProInventoryManager> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProInventoryManager, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProInventoryManager(ProInventoryManagerReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ProInventoryManagerDTO getProInventoryManagerById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProInventoryManagerDTO> listProInventoryManagerPage(ProInventoryManagerQuery query);

    /**
     * 盘点管理 - 根据查询条件导出
     * @param query:
     * @return  void
     * @MethodName export
     */
    void export(ProInventoryManagerQuery query);
}
