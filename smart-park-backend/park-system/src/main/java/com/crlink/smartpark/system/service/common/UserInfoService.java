package com.crlink.smartpark.system.service.common;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.entity.RsUserInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 * */
public interface UserInfoService extends IService<RsUserInfo> {


    Map<String, ?> userid2ValueMap(Collection<String> userIds, SFunction<RsUserInfo, ?> sValue);

    RsUserInfo queryUserInfoByUserId(String userId);

    List<RsUserInfo> listUserByMobile(String mobile, Integer status);

    Collection<String> queryUserIdByOrgIds(Collection<String> orgIds);
}
