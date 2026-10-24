/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-10 13:50:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-10 14:02:33
 */
export default [
  '/park-system/business-user/register', // 企业注册
  '/park-system/parkManagement/auth/login', // 企业登录
  '/park-system/parkManagement/auth/validateIdentity', // 登录身份校验
  '/park-system/business-user/getResetPwdSmsCode', // 企业端用户-忘记密码-短信验证码
  '/park-system/parkManagement/auth/getResetPwdSmsCode', // 运营端-忘记密码-获取短信验证码
  '/park-system/business-user/resetPwdForForget',
  '/park-system/business-user/validateIdentity',
  '/park-system/parkManagement/auth/resetPwdForForget',
  '/park-system/business-user/getSmsCode', // 短信验证码
  '/park-system/common/validateImgCode',
  '/park-meeting/meeting-manage/overviewMeeting', // 会议室预定概览
];
