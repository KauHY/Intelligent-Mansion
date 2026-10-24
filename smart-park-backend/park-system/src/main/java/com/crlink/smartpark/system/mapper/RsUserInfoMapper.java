package com.crlink.smartpark.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.entity.RsUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 * */
public interface RsUserInfoMapper extends BaseMapper<RsUserInfo> {
    List<RsUserInfo> listUserByMobile(@Param("mobile") String params, @Param("status") Integer status);
}
