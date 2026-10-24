package com.crlink.smartpark.system.service.common;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.user.*;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Set;

public interface UserManagementService {
    Boolean saveUserInfo(RsUserCreateDTO userReq);

    RsUserLoginVO checkUser(int userType, RsAuthLoginDTO rsUser);

    /**
     * 修改用户状态 ：0-已禁用,1-正常'
     *
     * @param status
     * @return
     */
    Boolean updateStatus(UpdateStatusReqDTO status);

    /**
     * 忘记密码时验证身份
     *
     * @param userPwdResetDTO
     * @return
     */
    String validateIdentity(RsUserValidateDTO userPwdResetDTO);

    /**
     * 忘记密码时重置密码
     *
     * @param resetPwdForForgetDTO
     * @return
     */
    Boolean resetPwdForForget(ResetPwdForForgetDTO resetPwdForForgetDTO);

    /**
     * 修改密码
     *
     * @param updatePwdDTO
     * @return
     */
    Boolean updatePwd(UpdatePwdDTO updatePwdDTO);

    /**
     * 用户/租户管理中的充值密码
     *
     * @param rsUser
     * @return
     */
    Boolean resetPwd(RsUserPwdResetDTO rsUser);

    /**
     * 退出系统
     *
     * @return
     */
    Boolean logout();

    Boolean removeUserById(String userId);

    Boolean judgeAdminUser(String userId);

    PageInfo<DictVO> listDictUsers(int userType, BaseQuery<String> userQuery, Set<String> orgIds);

    Boolean updateUserInfo(UserReqDTO rsUserReq);

    /**
     * 忘记密码时获取短信验证码
     *
     * @param reqDTO
     * @return
     */
    Boolean getResetPwdSmsCode(BusinessUserMobileDTO reqDTO);

    /**
     * {"1",{"id":"1","name":"周新建"}}
     *
     * @param userIds      用户id
     * @param queryColumns 需要查询的列
     * @return
     */
    Map<String, Map<String, Object>> queryUserByUserId(Set<String> userIds, Set<String> queryColumns);
}
