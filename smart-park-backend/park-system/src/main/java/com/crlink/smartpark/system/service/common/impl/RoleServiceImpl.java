package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.mapper.RoleMapper;
import com.crlink.smartpark.system.service.common.RoleService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 * */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Role, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> id2ValueMap = this.lambdaQuery()
                .select(Role::getId, sValue)
                .in(Role::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(Role::getId, sValue));
        return id2ValueMap;
    }
}
