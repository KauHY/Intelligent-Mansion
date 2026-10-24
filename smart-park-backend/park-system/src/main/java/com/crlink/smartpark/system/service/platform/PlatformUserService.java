package com.crlink.smartpark.system.service.platform;

import com.crlink.smartpark.system.dto.user.RsAuthLoginDTO;
import com.crlink.smartpark.system.dto.user.RsUserCreateDTO;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.dto.user.SysUserReqDTO;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.github.pagehelper.PageInfo;

public interface PlatformUserService {


    Boolean savePlatformSysRsUser(RsUserCreateDTO userReq);

    Boolean updatePlatformSysUser(SysUserReqDTO userUpdateDTO);

    RsUserLoginVO checkPlatformLogin(RsAuthLoginDTO rsAuthLoginDTO);


    PageInfo<RsUserInfoVO> listSysUserPage(RsUserQueryDTO rsUserQueryDTO);


    Boolean removeUserById(String userId);
}
