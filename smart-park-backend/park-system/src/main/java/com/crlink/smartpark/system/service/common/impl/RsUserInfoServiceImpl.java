package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.system.entity.RsUserInfo;
import com.crlink.smartpark.system.mapper.RsUserInfoMapper;
import com.crlink.smartpark.system.service.common.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 * */
@Service
public class RsUserInfoServiceImpl extends ServiceImpl<RsUserInfoMapper, RsUserInfo> implements UserInfoService {

    @Override
    public Map<String, ?> userid2ValueMap(Collection<String> userIds, SFunction<RsUserInfo, ?> sValue) {
        if (CollectionUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        Map<String, ?> userid2ValueMap = this.lambdaQuery()
                .select(RsUserInfo::getUserId, sValue)
                .in(RsUserInfo::getUserId, userIds)
                .list().parallelStream().collect(Collectors.toMap(RsUserInfo::getUserId, sValue));
        return userid2ValueMap;
    }

    @Override
    public RsUserInfo queryUserInfoByUserId(String userId) {
        RsUserInfo userInfo = this.getOne(Wrappers.<RsUserInfo>lambdaQuery().eq(RsUserInfo::getUserId, userId), false);
        return userInfo;
    }

    @Override
    public List<RsUserInfo> listUserByMobile(String mobile, Integer status) {
        return baseMapper.listUserByMobile(mobile, status);
    }

    @Override
    public Collection<String> queryUserIdByOrgIds(Collection<String> orgIds) {
        Set<String> result = this.lambdaQuery().select(RsUserInfo::getUserId).in(RsUserInfo::getOrgId, orgIds).list()
                .parallelStream().map(RsUserInfo::getUserId).collect(Collectors.toSet());
        return result;
    }

}
