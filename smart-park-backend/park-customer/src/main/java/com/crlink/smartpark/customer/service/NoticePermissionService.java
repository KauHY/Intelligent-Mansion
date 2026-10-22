package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.NoticePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 公告推送范围关联 服务类
 * </p>
 * */
public interface NoticePermissionService extends IService<NoticePermission> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<NoticePermission, ?> sValue);

}
