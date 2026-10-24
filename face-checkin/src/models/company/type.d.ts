import { string } from 'mathjs';

/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:44:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 11:39:06
 */
 interface LoginFormData { // 登录入参
    password: string
    username: string,
    validateCode: string,
    validateCodeKey: string
}

interface UserInfo { // 用户信息
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

interface AuditParams extends BaseSearchParams{ // 分页查询入参
  auditStatus?:number | null// 状态 1待审核 2通过 3拒绝 4已取消
  companyType?:number | null// 企业类型:0-物业公司；1-其他；2-科研院所；3-事业单位；4-机关单位；5-科技型企业
  endTime?:string
  name?:string
}

interface SupplySearchParams extends BaseSearchParams{ // 分页查询入参
  supplyType?:string
  name?:string
}

interface createSearchParams { // 分页查询入参
  contactPerson:string// 联系人
  contactPhone:string// 联系电话
  parkOrgId:string
}

interface SupplyListItem { // 分页查询出参数
  companyId:string//  公司Id
  companyName:string// 企业名称
  contact:string//  联系人
  detail:string//
  id:string
  jumpType:number | null// 跳转类型：0-无；1-URL；2-小程序
  name:string// 服务名
  parkOrgId:string// 园区Id
  parkOrgName:string// 所属园区
  publishTime:string// 发布时间
  remark:string// 备注
  source:number | null//  数据来源：0-企业新增；1-管理员新增
  state:number | null// 状态：0-待发布；1-已发布；2-停用
  supplyType:number | null //  供应类型：0-产品供应；1-服务供应；2-其他供应
  tel:string//  联系电话
  url:string
}

interface PurchaseListItem { // 分页查询出参数
  attKey:string
  companyId:string//  公司Id
  companyName:string// 企业名称
  contact:string//  联系人
  effectiveEndTime:string,
  detail:string//
  id:string
  jumpType:number | null// 跳转类型：0-无；1-URL；2-小程序
  name:string// 服务名
  parkOrgId:string// 园区Id
  parkOrgName:string// 所属园区
  publishTime:string// 发布时间
  purchaseType:number | null//
  remark:string// 备注
  source:number | null//  数据来源：0-企业新增；1-管理员新增
  state:number | null// 状态：0-待发布；1-已发布；2-停用
  tel:string//  联系电话
  url:string
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
  address: string// 地址
  code: string // 统一社会信用代码
  companyType: null, // 企业类型
  id: string
  licenses: string // 营业执照
  name: string // 企业名称
  enterpriseSize: null,
  companyTaxesType: null,
  industryField: null,
  atts: string
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
  mobile:string, // 手机号
  password:string, // 短信验证码
  username:string, // 用户名
  validateCode: string // 图形验证码
  validateCodeKey:string // 图形验证码key
  headOrgId:string
  id:string
  smsCode:string
}
interface tender {
biddingDocumentsPaid:number | null// 支付招标文件费：0-未支付；1-已支付
biddingDocumentsPaidName:string
companyName:string// 企业名称
contact:string// 联系人
id:string
mail:string// 邮箱
marginPaid:number | null// 保证金缴纳：0未缴纳；1-已缴纳
format:number | null
marginPaidName:string
purchaseId:string// 招标id
remark:string// 备注
tel:string
}
interface ResetPwdForForget { // 忘记密码重置密码
  newPassword:string, // 手机号
  newPasswordAgain:string,
  validateToken:string // 图形验证码key
}

export {
  SupplyListItem,
  SupplySearchParams,
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
  PurchaseListItem,
  createSearchParams,
  tender,
  AuditParams,
};
