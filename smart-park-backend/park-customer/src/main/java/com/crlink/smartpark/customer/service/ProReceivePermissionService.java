package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.controller.request.ProReceivePermissionAddDTO;
import com.crlink.smartpark.customer.entity.ProReceivePermission;
import com.crlink.smartpark.customer.controller.request.query.ProReceivePermissionQuery;
import com.crlink.smartpark.customer.controller.request.ProReceivePermissionReqDTO;
import com.crlink.smartpark.customer.dto.ProReceivePermissionDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 领用权限设置 服务类
 * </p>
 * */
public interface ProReceivePermissionService extends IService<ProReceivePermission> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProReceivePermission, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProReceivePermission(ProReceivePermissionAddDTO reqDTO);

    /**
     * 判断用户领用申请权限
     *
     * @return 实体对象
     */
    Boolean auth(String parkOrgId);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProReceivePermissionDTO> listProReceivePermissionPage(ProReceivePermissionQuery query);
}
