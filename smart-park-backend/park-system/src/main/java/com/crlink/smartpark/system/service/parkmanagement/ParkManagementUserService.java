package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.system.dto.user.RsAuthLoginDTO;
import com.crlink.smartpark.system.dto.user.RsUserCreateDTO;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.dto.user.UserReqDTO;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.github.pagehelper.PageInfo;

public interface ParkManagementUserService {

    RsUserLoginVO checkTenantLogin(RsAuthLoginDTO rsAuthLoginDTO);

    /**
     * 新增租户
     *
     * @param rsUserReq
     * @return
     */
    Boolean saveTenantUser(RsUserCreateDTO rsUserReq);

    /**
     * 编辑租户
     *
     * @param rsUserReq
     * @return
     */
    Boolean updateRsUser(UserReqDTO rsUserReq);

    PageInfo<RsUserInfoVO> listUserPage(RsUserQueryDTO rsUserQueryDTO);

    PageInfo<DictVO> listDictUsers(BaseQuery<String> roleQuery);

}
