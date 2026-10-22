/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:44:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 11:37:57
 */
 interface LoginFormData { // 登录入参
    password: string
    username: string,
    validateCode: string,
    validateCodeKey: string
}

interface UserInfo { // 用户信息
  code?:string, // 企业code
  username:string, // 用户名
  headOrgId:string,
  accessToken:string, // token
  realName:string, // 角色名称
  roleIds:string, // 角色ids
  mobile:string, // 手机号
  status:string, // 用户状态
  orgId?:string, // 组织id
  isPlatformAdminRole:boolean, // 是否平台管理员角色
}

interface SearchParams extends BaseSearchParams{ // 分页查询入参
  username?:string
  status?:string
  mobile?:string
}

interface UserListItem { // 分页查询出参数
  mobile: string
  orgId: string
  orgName: string
  password: string
  realName: string
  roleIds: string[]
  roleNames: string[]
  status: number
  userId: string
  username: string
}
interface UserPwdReset { // 用户管理重置密码
  newPassword:string
  newPasswordAgain:string
  username?:string
  userId:string
}

 interface AddUserParams { // 新增更新接口入参
  id?:string
  userId?:string
  mobile:string
  orgId:string
  realName:string
  roleIds:string[]
  roleNames:string[]
  username:string
  // password:string
  // [propsName:string]:string
}

interface RsUserListItem { // 查询当前用户权限内所有用户
  userId:string,
  username:string
}

interface UserPwdUpdate { // 修改密码
  newPassword:string
  newPasswordAgain:string
  oldPassword:string
}

interface ValidateIdentity { // 忘记密码身份校验
  mobilePhone:string, // 手机号
  telValidateCode:string, // 短信验证码
  username:string, // 用户名
  validateCode: string // 图形验证码
  validateCodeKey:string // 图形验证码key
}

interface ResetPwdForForget { // 忘记密码重置密码
  newPassword:string, // 手机号
  newPasswordAgain:string,
  validateToken:string // 图形验证码key
}

export {
  LoginFormData,
  UserInfo,
  UserListItem,
  UserPwdReset,
  AddUserParams,
  SearchParams,
  RsUserListItem,
  UserPwdUpdate,
  ValidateIdentity,
  ResetPwdForForget,
};
