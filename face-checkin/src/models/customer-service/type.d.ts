/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:44:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-22 16:52:58
 */

interface OrderSearchParams extends BaseSearchParams{ // 工单分页查询
    orderType: 1|2 // 报事报修类型
    orderOwnerMobile: string, // 请输入内容/反馈人/手机号
    parkOrgId: string, // 园区id
    dateTimes?:string[]
    orderState: 1|2|8|9
}
// type OrderTypeComplaint<T> = T extends 1 ? 11|12|13|14|15|16|17|18|19 : 21|22|23|24|25|99
export type OrderType = 11|12|13|14|15|16|17|18|19| 21|22|23|24|25|99|null

interface OrderInfo { // 订单详情
    createByName: string, // 发布人
    createTime: string, // 报事报修创建时间
    dataSources: 1|2, // 数据来源 1后台 2移动端
    id?: string,
    orderAddress: string, // 报事报修详细地址
    orderContent: string, //  报事报修内容
    orderNo: string, // 报事报修工单编号
    orderOwnerMobile: string, // 报事报修人电话
    orderOwnerName: string, // 报事报修反馈人
    orderState: 1|2|8|9, // 处理状态：1待派单,2待处理,8已完成,9已撤单
    orderType: 1|2|null// 报事报修类型：1-报事投诉、2-报修服务
    orderTypeItem: OrderType // 报事投诉类型：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务 报修服务类型：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备 99-其他
    orderUrl: string, // 图片uri，多个时逗号分隔
    parkOrgName: string, // 园区名
    parkOrgId: string,
}

interface AssignUser { // 派单
  companyId:string, // 物业公司id
  handleUserId:string, // 处理人id
  askFinishTime:string, // 要求完成时间
  orderNo:string,
  orderId:string,
}

interface OrderCancelParams{
  cancelReason:string, // 撤销原因
  orderId:string, // 工单id
}

interface recordListItem { // 处理流程
  askFinishTime:string, // 要求完成时间
  cancelReason:string, // 撤单原因
  companyId:string, // 物业公司id
  createByName:string, // 创建人姓名
  createTime:string, // 创建时间
  finishTime:string, // 完成时间
  id:string, // id
  state: 1|2|3|4|5,
  orderNo:string, // 保修no
  orderState:1|2|8|9, // 处理状态：1待派单,2待处理,8已完成,9已撤销
  preProNo:string, // 上一节点处理No
  preStaffName:string, // 上一节点处理员工名称
  preStaffId:string, // 上一节点处理员工
  startTime:string, // 开始时间
  staffName:string, // 当前处理员工名称
  staffMoblie:string, // 当前处理员工手机号
  remark:string, // 处理备注
}
interface OrderDetails extends OrderInfo{
  recordVOList:Array<recordListItem>
}

interface AnnouncementSearchParams extends BaseSearchParams{ // 公告分页查询
  title?: string // 标题
  contentType?: 1|2|3|4|5|6|7|8, // 公告类型
  effectTimeEnd?: string, // 公告时间段
  effectTimeStart?:string, // 开始时间
  effectStatus?: 0|1|2
}

interface NoticeSearchParams extends BaseSearchParams{ // 咨询分页查询
  title?: string // 标题
  effectTimeEnd?: string, // 公告时间段
  effectTimeStart?:string, // 开始时间
  effectStatus?: 0|1|2
}

interface BaseInformation{
  content: string, // 内容
  effectTimeEnd: string, // 发布结束时间
  effectTimeStart: string, // 发布开始时间
  id?: string,
  parkOrgIds: string[], // 园区id
  parkOrgNames: string[], // 园区名称
  title: string, // 标题
  views: number, // 浏览量
}

interface AnnouncementInfo extends BaseInformation{ // 公告信息
  parkOrgId:string,
  summary:string, // 摘要
  type:1|2|3|4|5|6|7|8|null, // 类型,
  putIntoType:number[],
  popuped:boolean, // 移动端是否弹窗提示
  withoutLimit: boolean, // 推送范围是否不限：0 指定公司/人员, 1 不限
  files:string, // 附件
  employeeIds:string[], // 指定人员
  companyIds:string[], // 指定公司
  status?:0|1|2,
}

interface NoticeInfo extends BaseInformation{ // 咨询信息
  contentType:0|1|null
  picture: string, // 图片
  status?:0|1,
  putIntoType?:1|2|null,
  type?:1|2|null,
}

// ------------- 物品放行 ------------------------
interface GoodsReleaseSearchParams extends BaseSearchParams{ // 物品放行分页查询
  submitterName?: string // 申请人姓名
  submitterMobile?: string, // 申请人手机号
  parkOrgId?:string, // 园区
  createTimeEnd?:string, // 结束时间
  createTimeStart?:string, // 开始时间
  status?:number|null
}

interface Goods {
  count: number, // 数量
  name: string, // 名称
  picture: string // 图片
}
interface GoodsReleaseInfo { // 物品放行
  carNumber: string, // 车牌号
  checkOpinion: string, // 审核意见
  company: string, // 所在公司
  goodsList: Goods[],
  goodsNo: string, // 编号
  id: string,
  parkOrgName: string,
  releaseCode: string, // 放行码
  releaseDatetimeEnd: string, // 放行时间
  releaseDatetimeStart: string, // 放行时间
  remark: string, // 备注
  selfCreate: boolean, // 是否自己申请
  status: number, // 是否自己申请
  submitterMobile: string, // 申请人手机号
  submitterName: string, // 申请人
  createTime: string, // 申请时间
  updateTime:string,
  employeeName:string,
}

interface GoodsReleaseAudit{ // 物品放行审核
  checkOpinion:string,
  checkStatus:2|3|null,
  goodsReleaseId:string
}

export {
  OrderSearchParams,
  OrderInfo,
  AssignUser,
  OrderCancelParams,
  OrderDetails,
  AnnouncementSearchParams,
  AnnouncementInfo,
  NoticeInfo,
  NoticeSearchParams,
  GoodsReleaseSearchParams,
  GoodsReleaseInfo,
  GoodsReleaseAudit,
};
