package com.crlink.smartpark.system.service.common;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.entity.RsUser;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 * */
public interface RsUserService extends IService<RsUser> {


    Map<String, ?> id2ValueMap(Set<String> ids, SFunction<RsUser, ?> sValue);

    PageInfo<RsUserInfoVO> listPage(Set<String> orgIds, RsUserQueryDTO rsUserQueryDTO);


}
