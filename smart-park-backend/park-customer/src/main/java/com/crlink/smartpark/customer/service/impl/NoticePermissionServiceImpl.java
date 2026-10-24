package com.crlink.smartpark.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.customer.entity.NoticePermission;
import com.crlink.smartpark.customer.mapper.NoticePermissionMapper;
import com.crlink.smartpark.customer.service.NoticePermissionService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 公告推送范围关联 服务实现类
 * */
@Service
public class NoticePermissionServiceImpl extends ServiceImpl<NoticePermissionMapper, NoticePermission> implements NoticePermissionService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<NoticePermission, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<NoticePermission> lambdaQuery = this.lambdaQuery().in(NoticePermission::getId, ids);
        if (flag) {
            lambdaQuery.select(NoticePermission::getId, sValue);
        }
        Stream<NoticePermission> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(NoticePermission::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(NoticePermission::getId, Function.identity()));
        }
        return result;

    }
}

