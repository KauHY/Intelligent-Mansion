/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:41:35
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-16 17:30:30
 */
enum URLS {
    login = '/park-system/parkManagement/auth/login', // 登录
    logout = '/park-system/parkManagement/auth/logout', // 登出
    resetPwd = '/park-system/parkManagement/user/resetPwd', // 用户管理-重置密码
    updatePwd = '/park-system/parkManagement/auth/updatePwd', // 修改密码
    getSmsCode='/park-system/parkManagement/auth/getResetPwdSmsCode', // 运营端用户-忘记密码-0获取短信验证码

    updateCompanyPwd='/park-system/businessUserAudit/updatePwd', // 企业端修改密码
    validateCompanyIdentity='/park-system/business-user/validateIdentity', // 企业端用户-忘记密码-1校验身份
    resetPwdCompanyForForget='/park-system/business-user/resetPwdForForget', // 企业端用户-忘记密码-2重置密码
    getResetPwdSmsCode='/park-system/business-user/getResetPwdSmsCode', // 企业端用户-忘记密码-0获取短信验证码

    save = '/park-system/parkManagement/user/save', // 新增用户
    update = '/park-system/parkManagement/user/update', // 更新用户
    remove = '/park-system/parkManagement/user/remove/', // 删除用户
    listPage = '/park-system/parkManagement/user/listPage', // 分页查询
    // findById = '/park-system/rs-user/findById/', // 根据id查询用户
    // enable = '/park-system/rs-user/enable/', // 启用
    // disable = '/park-system/rs-user/disable/', // 根据id查询用户
    updateStatus = '/park-system/parkManagement/user/updateStatus',
    rsUserList = '/park-system/parkManagement/user/listUsers', // 查询当前用户权限内可操作的所有用户
    resetPwdForForget = '/park-system/parkManagement/auth/resetPwdForForget', // 忘记密码
    validateIdentity = '/park-system/parkManagement/auth/validateIdentity' // 忘记密码验证身份
}
export default URLS;
