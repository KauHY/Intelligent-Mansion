/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 18:52:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 15:57:36
 */
import { HouseInfo } from '~models/house';

interface CurrentHouseInfo extends HouseInfo { // 后台的狗屎接口，不通人定义的字段名不一样
  parkOrgName:string
}
interface CustomerManagementInfoParams extends BaseSearchParams{ // 房屋招商分页查询(用于房源招商)
    customerName:string, // 客户名称
    type:number|null // 客户类型

}
interface HousingBusinessSearchParams extends BaseSearchParams{ // 房屋招商分页查询(用于房源招商)
    effectType: number, // 招商类型
    status:number, // 招商状态
    title:string, // 招商标题
    orgId:string

}

interface HousingSelectBusinessSearchParams extends BaseSearchParams{ // 选择意向房源接口
  houseId?: string, // 招商类型
  buildingId?:string,
  parkOrgId?:string

}

interface HousingSelectBusinessInfo{
  buildArea: number|null,
  buildingName: string,
  createTime: string,
  houseInfoId: string,
  name: string,
  // parkOrgName: string
}

interface SelectHouseInfo {
[props:string]:{
  buildArea?:number|null,
  houseId?:string,
  name:string,
  houseInfoId?:string,
}
}

interface CustomerManagementInfo{
  customerName:string, // 客户名称
  type:number|null, // 客户类型
  contacts:string, // 客户类型
  contactsTel:string, // 客户类型
  code:string, // 客户类型
  address:string, // 客户类型
  name:string, // 客户类型
  id:string, // 客户类型
  tel:string, // 客户类型
}
interface HousingBusinessInfo{ // 房源信息
    address: {
      name?:string,
      latitude?:number,
      longitude?:number,
    }, // 地址
    deadline:string, // 截至时间
    businessHouseVO?:SelectHouseInfo,
    amount: number, // 数量
    area: number, // 面试
    companyId?: string,
    content: string, // 内容详情
    createTime: string, // 创建时间
    effectType: 0|1|null, // 招商时效类型,1:长期有效 | 0 固定期限
    employeeId: string, // 咨询顾问（员工）id
    employeeName: string, // 咨询顾问（员工）
    employeePhone: string, // 咨询电话
    highlight: number[], // 亮点描述
    id?: string,
    houseInfoIds:string[],
    parkOrgId: string, //
    parkOrgName: string, //  园区名称
    rentOrSale: number[], // 租售属性，1出租，2出售，3出租出售
    status: 0|1|null, // 发布状态,0，已下架，1已发布
    title: string, // 标题
    titlePicture: string, // 标题图片
}

interface HouseInfoSearchParams extends BaseSearchParams{ // 招商房屋分页查询
  effectType: number, // 招商类型
  status:number, // 招商状态
  employeeName:string, // 招商顾问
  orgId:string
}

interface HouseIntentionSearchParams extends BaseSearchParams{ // 意向房源分页查询
  buildingId: string, // 招商类型
  houseId: string, // 招商状态
  parkOrgId: string, // 招商顾问
}

interface HouseIntentionInfo { // 招商房屋列表
  buildingName:string,
  name:string,
  houseInfoId:string
  houseId:string
  unitRent:string
  earliestRentTime:string // 最早租期
}

interface BaseHousingInfo {
  id: string, //
  stationNum: number|null, // 容纳工位
  visitTime: string, //
  chargeType: number|null, // 押付方式
  freeRentMonth: number|null, // 免租期
  canDivided: boolean, // 能否分割
  unitRent: string|null, // 单位租金
  earliestRentTime: string, // 最早租期
  houseId:string,
  visitStartTime: string,
  parkOrgId: string
  visitEndTime: string,
  minRentMonth: number|null, // 最短租期
  discussPersonally:boolean
}

interface HousingInfo extends BaseHousingInfo {
  businessStatus: number, // 招商状态
  createBy: string, // 创建人
  createTime: string, // 创建时间
  deadline: string, // 截止时间
  effectType: 0|1|null, // 招商时效类型,1:长期有效 | 0 固定期限
  employeeName: string, // 咨询顾问名
  houseId: string, // 房屋id
  // parkOrgId: string,
  updateBy: string, // 更新人
  houseVO:CurrentHouseInfo
}

// 商机管理
interface BusinessInfoSearchParams extends BaseSearchParams{ // 房屋招商分页查询
  businessSource: number, // 商机来源
  followEndTime:string, // 跟进结束时间
  followStartTime:string, // 跟进开始时间
  keyword:string, // 关键字
  orgId:string
}

 interface BusinessInfo {
    businessSource: number|null, // 商机来源：1商机录入，2入驻申请
    chanceName: string, // 商机名称
    chanceStage: number|null, // 商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭
    contactMail: string, // 联系人邮箱
    contactName: string, // 联系人
    contactPhone: string, // 联系人电话
    expectedPrice: number|null, // 期望价格（元/㎡/月）
    followEmployeeId: string, // 跟进人id
    followEmployeeName: string, // 跟进人
    id: string,
    houseIds:string[],
    latestFollowTime: string, // 最近实际跟进时间
    parkOrgId:string,
    parkOrgName: string, // 关联园区
    quantityNumber: number|null, // 需求数量
    remark: string, // 备注
    remindByDay: string, // 按天提醒
    remindByWeek: string, //  按周提醒
    remindContent: string, // 提醒内容
    remindFrequency: number|null, // 提醒频次: 1 天、2 周
    threadSource: number|null, // 线索来源：1老客户介绍，2政府资源，3公共资源，4第三方资源，5内部资源，6网络资源，7招商人员资源
    updateTime: string, // 修改时间
    recordType?:number, // 操作类型
    businessHouseVO?:SelectHouseInfo
 }

 interface OperateBussInfo { // 分配接口参数
  parkOrgId?:string,
  followEmployeeId?:string, // 跟进人id
  companyId?:string,
  id:string,
  recordType:number, // 操作类型
 }

interface BusinessInfoFollow {
  id:string,
  chanceStage:number|null, // 商机阶段
  followContent:string, // 跟进内容
  followTime:string, // 跟进时间
  followType:number|null // 跟进类型
}

interface BusinessInRemind { // 商机提醒
  id:string,
  remindByDay:number|null, // 按天提醒:天数
  remindByWeek:number[], // 按周提醒: 0 全选，1~~7 周一~~周日
  remindContent:string, // 提醒内容
  remindFrequency:number|null // 提醒频次: 1 天、2 周
  remindTime:string // 提醒时间: HH:mm
}

interface BusinessInconvertToCustomer{
  id:string,
  parkOrgId:string,
}

// 商机管理
interface FollowRecordSearchParams extends BaseSearchParams{ // 房屋招商分页查询
  id:string
}

interface FollowRecord { // 跟进记录
  chanceStage: number,
  createTime: string,
  followContent: string,
  followEmployee: string,
  followEmployeeName: string,
  followTime: string,
  followType: number,
  id: string
}

interface StageProcess{ // 阶段流程
  createBy: string,
  createByName: string,
  createTime: string,
  id: string,
  recordTypeName: number
}

interface ViewHouseApplySearchParams extends BaseSearchParams{ // 房屋招商分页查询
  status?:1|2|3|null, // 受理状态
  createStartTime?:string,
  createEndTime?:string,
}

interface ViewHouseApplyInfo {
  address: string,
  companyName: string,
  createTime: string,
  employeeId:string,
  employeeName: string,
  houseFunction: string,
  houseInfoId: string,
  id: string,
  mobile: string,
  parkOrgName: string,
  parkOrgId: string,
  status: 1|2|3|0|null,
  title: string,
  username: string,
  businessHouseVO?:SelectHouseInfo,
}

interface TurnIntoBusiness {
  businessId: string // 商机id
  code: string // 统一社会信用代码
  contacts: string // 联系人
  contactsMail: string // 联系人邮箱
  contactsTel: string // 联系电话
  employeeId: string // 跟进人
  employeeIdName: string // 跟进人
  name: string // 客户名称
  parkOrgId: string
  status: number | null // 客户类型 0：物业公司，1：普通企业
  type: number | null // 客户状态,1，已成交，2未成交，3历史成交
}

export {
  CustomerManagementInfoParams,
  HousingBusinessSearchParams,
  CustomerManagementInfo,
  HousingBusinessInfo,
  HousingSelectBusinessSearchParams,
  HousingSelectBusinessInfo,
  SelectHouseInfo,
  HouseInfoSearchParams,
  HousingInfo,
  BaseHousingInfo,
  BusinessInfoSearchParams,
  BusinessInfo,
  OperateBussInfo,
  HouseIntentionSearchParams,
  HouseIntentionInfo,
  BusinessInfoFollow,
  BusinessInRemind,
  BusinessInconvertToCustomer,
  FollowRecordSearchParams,
  FollowRecord,
  StageProcess,
  ViewHouseApplySearchParams,
  ViewHouseApplyInfo,
  TurnIntoBusiness,
};
