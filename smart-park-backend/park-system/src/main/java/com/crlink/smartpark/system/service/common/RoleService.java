package com.crlink.smartpark.system.service.common;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.entity.Role;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 * */
public interface RoleService extends IService<Role> {
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Role, ?> sValue);
}
