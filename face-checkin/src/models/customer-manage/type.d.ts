/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:23
 * @LastEditTime: 2022-03-02 16:29:08
 * @LastEditors: Xucaizhi3
 * @Description:
 */
interface SearchcustomerParams extends BaseSearchParams {
  status: string // 广告状态
  effectTimeEnd: string // 广告日期
  effectTimeStart: string // 广告日期
  parkOrgId: string // 广告状态
  name: string // 广告名称
}

interface CustomerListKind {
  customerCode: string
  parkOrgId: string
  parkOrgName: string
  name: string
  tagName: string
  type: string // 客户类型 0：物业公司，1：普通企业
  status: number | null // 客户状态,1，已成交，2未成交，3历史成交
  id: string
  contacts: string // 联系人
  tel: string // 联系电话
}

interface BusinessContactsListKind {
  contacts: string // 联系人
  department: string // 联系人部门
  mail: string // 联系人邮箱
  position: string // 联系人职位
  remark: string // 备注
  tel: string // 联系电话
}

interface companyForms {
  corporateTax: number | null,
  address: string,
  atts: string,
  businessUserId: string,
  code: string,
  companyTaxesType: number | null,
  companyType: number | null,
  createBy: null,
  createTime: string,
  deleted: false,
  enterpriseSize: number | null,
  headOrgId: string,
  id: string,
  industryField: number | null,
  licenses: string,
  name: string,
  remark: string,
  updateBy: null,
  updateTime: string,
}
interface CustomerInfoKind {
  companyType?: number | null,
  atts?:string,
  licenses?:string,
  remark?:string,
  address: string // 地址
  businessContactsList: BusinessContactsListKind[] // 业务联系人
  businessTerm: number | null // 营业期限
  code: string // 统一社会信用代码
  companyMail: string // 企业邮箱
  companyManagementStatus: number | null // 企业经营状态
  companyScale: number | null // 企业规模(人数)
  companyTaxesType: number | null // 企业纳税类型
  contacts: string // 联系人
  contactsDepartment: string // 联系人部门
  contactsMail: string // 联系人邮箱
  contactsPosition: string // 联系人职位
  contactsTel: string // 联系电话
  customerCode: string // 客户编码
  customerTagId: string // 客户标签
  employeeId: string // 跟进人
  id: string
  industryField: number | null // 行业领域
  legalRepresentative: string // 法人代表
  name: string // 客户名称
  parkOrgId: string // 园区id
  registeredCapital: number | null // 注册资本
  status: number | null // 客户状态,1，已成交，2未成交，3历史成交
  type: number | null // 客户类型 0：物业公司，1：普通企业
}

interface CustomerTagListKind {
  name: string
  id?: string
  parkOrgId: string
  parkOrgName: string
  createTime: string
}

interface CustomerTagInfoKind {
  name: string
  parkOrgId: string
  id?: string
}

export {
  SearchcustomerParams,
  CustomerListKind,
  CustomerInfoKind,
  BusinessContactsListKind,
  CustomerTagListKind,
  CustomerTagInfoKind,
  companyForms,
};
