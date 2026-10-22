/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:44:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-05 17:56:30
 */
//  interface LoginFormData { // 登录入参
//     password: string
//     username: string,
//     validateCode: string,
//     validateCodeKey: string
// }

// interface UserInfo { // 用户信息
//   username:string, // 用户名
//   headOrgId:string,
//   accessToken:string, // token
//   realName:string, // 角色名称
//   roleIds:string, // 角色ids
//   mobile:string, // 手机号
//   status:string, // 用户状态
//   isPlatformAdminRole:boolean, // 是否平台管理员角色
// }

interface SearchParams extends BaseSearchParams{ // 入住企业分页查询入参
    name: string // 入住企业名称
    companyType: string, // 入住企业类型
    parkOrgId: string, // 园区id
}

interface CompanyInfoMation { // 分页查询出参数
    atts:string
    address:string // 地址
    code:string // 统一社会信用代码
    companyType:number|null, // 企业类型
    id?:string
    licenses:string // 营业执照
    name:string // 企业名称
    enterpriseSize:number|null,
    companyTaxesType:number|null,
    industryField:number|null,
    remark:string,
    corporateTax?:number|null,
}
interface CompanyInfo { // 分页查询出参数
    address:string // 地址
    code:string // 统一社会信用代码
    companyType:number|null, // 企业类型
    type?:number|null, // 企业类型
    contacts:string, // 联系人
    id?:string
    parkOrgName?:string
    licenses:string // 营业执照
    name:string // 企业名称
    parkOrgId:string // 园区id
    tel:string // 联系人电话
    status?:number|null, // 状态
}

interface BindHouseInfo { // 绑定房屋
    houseId:string, // 房屋id
    buildingId:string, // 楼层id
    rentTimes:string[],
    rentStartTime:string, // 起租时间
    rentEndTime:string, // 到期时间

}

interface RenewHouse {
    id:string, // 租赁ID
    rentRenewEndTime:string // 续租时间
}

interface CompanyHouseBindListPageParams extends BaseSearchParams{ // 企业已绑定房屋分页查询
    buildingId: string // 楼栋ID
    companyId: string, // 入驻企业ID
    houseId: string, // 房屋ID
}

interface CompanyHouseBindListPageItem { // 入住企业绑定房屋分页数据
    buildArea: string,
    buildingName: string,
    coverArea: string,
    houseName: string,
    id: string,
    parkOrgName: string,
    rentEndTime: string,
    rentStartTime: string
}

interface PageHouseBindRecordParams extends BaseSearchParams{ // 房屋入住记录分页查询参数
    buildingId?: string // 楼栋ID
    companyName?: string, // 企业名称
    houseId?: string, // 房屋ID
    parkOrgId?:string, // 园区id
}

interface HouseBindRecord{ // 房屋入住记录
    bind: true, // 绑定状态：true：已绑定 false：已解绑
    buildArea: number, // 建筑面积
    buildingName: string, // 楼栋单元名
    companyName: string, // 企业名称
    coverArea: number, // 实际面积
    houseName: string, // 楼层房号
    id: string,
    parkOrgName: string, // 园区名
    rentEndTime: string, // 到期时间
    rentStartTime: string, // 起租时间
    unbindTime:string // 解绑时间

}

interface PageRenewalRecordSearchParams extends BaseSearchParams{
    rentRecordId:string // 房屋租赁id
}
interface PageRenewalRecordInfo { // 续租记录分页查询
    buildingName: string,
    companyName: string,
    createTime: string,
    houseName: string,
    parkOrgName: string,
    renewEndTime: string,
    rentEndTime: string,
    rentStartTime: string
}

interface EmployeePageSearchParams extends BaseSearchParams{ // 分页查询员工信息
    name?:string // 姓名
    mobile?:string // 手机
    parkOrgId?:string // 园区
    companyId?:string // 企业
    jobId?:string
    status?:0|1
}

interface EmployeeInfo { // 员工
        companyId: string, // 所属入驻企业ID
        companyName: string, // 企业名称
        id?: string,
        createTime?:string
        jobCode: string[], // 岗位名称
        jobCodes?:number[],
        jobIds: string[], // 岗位id
        mobile: string,
        name: string, // 员工姓名
        parkOrgId: string, // 园区id
        parkOrgName: string, // 园区名称
        pics: string, // 照片
        status: 0|1, // 状态
        tagIds: string[], // 员工标签 id
        tagNames:string[] // 员工标签
        remark:string,
}

interface SearchEmployeesSelectOptParams { // 查询寻员工下拉列表请求入参
    companyId?:string, // 公司id
    companyType?:0|1 // '0：物业公司，1：入驻企业',
    value:string, // 员工姓名 employeeName[后台接口真实字段名]
    jobCodes?:string[], // 岗位code
    orgId?:string, // 组织/园区id
}

interface EmployeesDictByTagSortSearchParma { // 查询员工参数（根据岗位分类）
    companyType?:number,
    employeeName?:string,
    jobCodes?:number[],
    parkOrgId?:string
}

interface EmployeesDictByTagSort {
    children?:EmployeesDictByTagSort[],
    label:string,
    value?:string
    expanded?:boolean
}

interface EmployeeUpdateStatus{
    id:string
    status: 0|1, // 状态
}

// 标签
interface TagPageSearchParams extends BaseSearchParams{ // 标签分页查询
    tagName?:string // 标签名称
    parkOrgId?:string // 园区
    companyId?:string // 企业
}
interface TagInfo { // 标签信息
    id?:string
    companyId:string // 公司id
    companyName?:string // 公司名称
    parkOrgName?:string // 园区名称
    parkOrgId:string// 园区id
    tagName:string // 标签名
    updateTime?:string // 更新时间
}

// 岗位管
interface JobPageSearchParams extends BaseSearchParams{ // 标签分页查询
    jobCode?:string // 岗位
    parkOrgId?:string // 园区
    companyId?:string // 企业id
    companyType?:string, // 企业类型
    name?:string // 员工姓名
}
interface JobInfo { // 标签信息
    companyId: string,
    companyName: string,
    companyType: '0'|'1',
    employeeIds: string[], // 关联员工id
    employeeNames: string[], // 关联员工名称
    id?: string,
    jobName?:string
    jobCode:number|null,
    parkOrgId: string,
    parkOrgName: string
}

// 选人组件-----------

interface SelectTagTreeSearchParams { // 园区|公司|标签树 查询参数
    companyType:0|1|null, // 0物业|1 除了物业得公司
    orgId:string|string[],
    parkOrgIds:string|string[]
    companyIds:string|string[]
}
interface SelectTagTree extends SelectOptions{ // 园区|公司|标签树
    label:string,
    value:string,
    type?:any
    disabled?:boolean,
    children:SelectTagTree[],
    extraInfo?:{[props:string]:any}
}

interface SelectEmployeesByIdAndType extends BaseSearchParams { // 根据筛选条件分页查询员工，查询接口参数
    // ids: string[],
    jobCodes:string|string[]
    // idType: number|null,
    name: string,
    parkOrgId: string
    companyIds:string[],
    tagIds:string[],
}

interface CompanyCheckInfo { // 分页查询出参数
  companyType:number|null, // 企业类型
  auditStatus:number|null, // 状态
  applyTime:string, // 申请时间
  id:string
  parkOrgName?:string
  companyInformationName:string // 企业名称
  name:string // 企业名称
  auditMsg:string // 审核意见
  createTime:string // 提交时间
  auditTime:string // 审核时间
}

interface CompanyCheckData { // 分页查询出参数
  id:string,
  auditStatus:number|null, // 状态
  auditMsg:string // 审核意见
}

export {
  SearchParams,
  CompanyInfo,
  BindHouseInfo,
  CompanyHouseBindListPageParams,
  CompanyHouseBindListPageItem,
  PageHouseBindRecordParams,
  HouseBindRecord,
  RenewHouse,
  PageRenewalRecordInfo,
  PageRenewalRecordSearchParams,
  EmployeePageSearchParams,
  EmployeeInfo,
  EmployeeUpdateStatus,
  TagPageSearchParams,
  TagInfo,
  JobPageSearchParams,
  JobInfo,
  EmployeesDictByTagSortSearchParma,
  EmployeesDictByTagSort,
  SearchEmployeesSelectOptParams,
  // -----------------选人组件
  SelectTagTree,
  SelectTagTreeSearchParams,
  SelectEmployeesByIdAndType,
  CompanyInfoMation,
  CompanyCheckInfo,
  CompanyCheckData,
};
