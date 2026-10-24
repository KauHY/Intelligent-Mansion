/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:44:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-24 15:35:27
 */

interface VisitorSubscribeSearchParams extends BaseSearchParams{ // 预约访客记录分页查询
  visitorName:'', // 访客姓名
}

interface VisitorInfo {
  id: string,
  visitorName: string, // 访客名
  visitorTel: string, // 访客电话号码
  invitorName: string, // 被访人姓名
  visitDate: string, // 来访日期
  visitorTypeName: string, // 访客类型
  visitReason: 1|2|3, // 来访事由 1:商务交流 2:面试 3:其他
}

interface RegisterInfo {
  invitorName: string, // 被访人姓名
  invitorTel: string, // 被访人电话号码
  visitReason: number|null, // 来访事由 1:商务交流 2:面试 3:其他
  visitorFaceImg: string, // 访客人脸图url路径
  visitorIdCard: string, // 访客身份证号
  visitorName: string, // 访客名
  visitorTel: string, // 访客电话号码
  accessPolicyId:string,
  companyId: string, // 通行策略ID,无预约ID时不能为空
  id: string,
  registerVerifyType: 1|2|3|null, // 核验类型，1:无任何凭证 2:身份证 3:预约凭证
  visitorCount: number|null, // 访客人数
  visitorRemark: string, // 访客备注
  carNum?:string, // 访客车牌
  subscribeProcessStatus?:number, // 审核状态
  visitorTypeId?:string, // 访客类型
  visitorSource?:1|2|3, // 1:访客预约 2:访客邀约 3:现场登记
  auditRemark?:string, // 审核批注
  visitorTypeName?:string // 访客类型名称
  visitDate?:string,
  cardNo:string,
  parkOrgId:string,
}

interface AuditPerson {
  id:string,
  auditEmployeeNames?:string // 审批人姓名
  invitorEmployeeAudit: number|null, // 被访人审核
  companyName?:string, // 入驻企业名称
  updateTime?:string, // 创建时间
  parkOrgName?:string, // 园区名称
  // auditEmployeeId:string, // 员工id（用于编辑）
  auditEmployeeIds:string[], // 员工id（用于新增）
  companyId:string, // 公司id
  parkOrgId?:string
}

// 审批人分页查询
interface AuditPersonSearchParams extends BaseSearchParams{
  companyName:string,
  parkOrgId:string
}

// 访客类型分页查询
interface VisitorTypeSearchParams extends BaseSearchParams{
  name?:string
}
interface VisitorType {
  companyId: string, // 入驻企业ID
  id: string,
  parkOrgId: string,
  visitorTypeName: string // 访客类型
  parkOrgName?: string,
  companyName?: string, // 企业名称
  updateTime?: string,
}

// 访客流程分页查询
interface VisitorProcessSearchParams extends BaseSearchParams{
  name?:string
}
// 访客流程配置
interface VisitorProcess {
  companyId: string,
  id: string,
  invitorAudit: 0|1|null, // 被访人审核，0：否，1：是
  parkOrgId: string,
  processName: string,
  processNodes: string, // 访客流程节点集合（英文逗号分隔），1：访客预约 2：员工审核 3：员工邀请 4：访客补充信息
  processType: 1|2| null, // 流程类型，1：访客预约 2：访客邀约
  visitorTypeId: string, // 访客类型ID
  auditPersonId?: string, // 审核人ID
  auditPersonName?: string,
  companyName?: string,
  parkOrgName?: string,
  updateTime?: string,
  enable?: 0|1,
  visitorTypeName?: string, //
}

// 企业绑定信息分页
interface CompanyBindInfoSearchParams extends BaseSearchParams{
  name?:string, // 企业名称
  parkOrgId?:string // 园区id
}

// 企业绑定信息
interface CompanyBindInfo {
  id:string, // 公司id
  visitorClientValue: string
  visitorClientType: 0|1|2|null, // 企业绑定访客客户端类型，0：无 1：小程序 2：超链接
  visitorClientPicUrl:string, // 客户端图片地址
  name?:string, // 公司名称
  parkOrgId?: string,
  createTime?:string,
  parkOrgName?:string, // 园区名称
  visitorClientTypeLabel?:string, // 连接类型名
}

// 来访须知分页
interface visitingInstructionsSearchParams extends BaseSearchParams{
  parkOrgId?:string // 园区id
}

// 来访须知配置
interface visitingInstructions {
  id: string,
  noticeContent?: string, // 须知内容
  noticeName?: string, // 须知名称
  parkOrgId:string // 园区id
  createTime?:string,
  parkOrgName?:string,
  updateTime?:string
}

export {
  VisitorSubscribeSearchParams,
  VisitorInfo,
  RegisterInfo,
  AuditPerson,
  AuditPersonSearchParams,
  VisitorType,
  VisitorTypeSearchParams,
  VisitorProcess,
  VisitorProcessSearchParams,
  CompanyBindInfo, // 企业绑定信息
  CompanyBindInfoSearchParams,
  visitingInstructionsSearchParams,
  visitingInstructions,
};
