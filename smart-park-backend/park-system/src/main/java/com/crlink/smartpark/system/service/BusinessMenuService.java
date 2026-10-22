package com.crlink.smartpark.system.service;

import com.crlink.smartpark.system.entity.BusinessMenu;
import com.crlink.smartpark.system.model.query.BusinessMenuQuery;
import com.crlink.smartpark.system.model.BusinessMenuReqDTO;
import com.crlink.smartpark.system.dto.BusinessMenuDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 企业端菜单/按钮表 服务类
 * </p>
 * */
public interface BusinessMenuService extends IService<BusinessMenu> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BusinessMenu, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateBusinessMenu(BusinessMenuReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    BusinessMenuDTO getBusinessMenuById(String id);

    /**
     *  配置菜单列表获取
     *
     * @return   实体对象
     */
    List<BusinessMenu> listBusinessMenu();

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<BusinessMenuDTO> listBusinessMenuPage(BusinessMenuQuery query);
}
