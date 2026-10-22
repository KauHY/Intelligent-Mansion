/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-12 14:19:31
 * @LastEditTime: 2022-04-26 15:56:08
 * @LastEditors: Xucaizhi3
 * @Description:
 */
interface SearchContractParams extends BaseSearchParams {
  type: string; // 合同类型
  date: string; // 合同日期
  state: string; // 合同状态
  name: string; // 合同名称
}

interface ContractBillingPlanReqDTOS {
  lateCharge: number;// 是否收取滞纳金
  lateType: number | null; // 滞纳率计算规则
  lateDayAmount: number | null; // 每日滞纳金
  monthBy: number | null; // // 账单生成日期-按月
  billingStartAndEndDate: string[];
  amountWithTax: number | null; // 金额元（含税）
  amountWithoutTax: number | null; // 金额元（不含税）
  billingCycle: number | null; // 计费周期
  billingEndDate: string; // 计费结束日期
  billingStartDate: string; // 计费开始日期
  calculationStandard: number | null; // 计算标准
  carryMode: number | null; // 进位方式
  chargeType: number | null; // 收费类型
  chargeItemName: number | null; // 收费类型名称
  chargingCycle: number | null; // 收费周期
  cumulativeCycle: number | null; // 累计x个计费周期
  dayOfMonth: number | null; // 账单生成日期
  daysForDelay: number | null; // 最晚缴纳日期
  exemptDays: number | null; // 豁免天数
  feeBillingSchemeId: string; // 收费方案Id
  feeBillingSchemeName: string; // 收费方案名称
  formulaType: number | null; // 公式类型
  houseIds: string[]; // 应用房源ids
  lateRate: number | null; // 滞纳率
  reservedDigits: number | null; // 保留位数
  unitPriceWithTax: number | null; // 单价元（含税））
  unitPriceWithoutTax: number | null; // 单价元（不含税）
  fixedAmountWithTax: number | null; // 固定金额元（含税）
  fixedAmountWithoutTax: number | null; // 固定金额元（不含税）
  houseIdsNames: string;
  taxRate: number; // 税率
  freeRentDay: number | null; // 免租期（天）
  id?: string // 收费项 id
  chargeItemMessage: any,
}

interface HouseMessages {
  buildArea: number | null,
  coverArea: number | null,
  buildingName: string,
  name?: string,
  floor: number | null,
  houseFunction: number, // 房屋功能
  id: string,
  houseId: string,
  houseName?: string,
  totalFloor: number | null,
}
interface Contract {
  addressA: string; // 甲方地址
  addressB: string; // 乙方地址
  attachmentKey: string; // 附件id(com_disk_file主键)，多个时,分隔
  attributes: number | null; // 合同属性：1-新签合同；2-续签合同；3-变更合同；4-退约合同；5-历史合同
  companyIdA: string; // 甲方公司id
  contactA: string; // 甲方联系人
  contactB: string; // 乙方联系人
  contactTelA: string; // 甲方联系电话
  contactTelB: string; // 乙方联系电话
  contractBillingPlanReqDTOS: ContractBillingPlanReqDTOS[]; // 计费方案
  houseMessages: HouseMessages[]; // 关联房屋ids
  contractedDate: string; // 签约日期
  creditCodeA: string; // 甲方统一社会信用代码
  creditCodeB: string; // 乙方统一社会信用代码
  customerIdB: string; // 乙方公司id
  employeeId: string; // 经办人id，多个时,分隔
  employeeName: string; // 经办人name，多个时,分隔
  employeeDicts?: SelectOptions; // 经办人回显
  endDate: string; // 合同结束日期
  // extension: object, // 扩展字段json字符串(原合同编号:originContractNo、原合同id:originContractId、变更说明:changeExplanation、变更类型:changeType)
  id: string; // 8888
  name: string; // 合同名称
  nameA: string; // 甲方名称
  nameB: string; // 乙方企业名称
  no: string; // 合同编号
  parkOrgId: string; // 园区Id
  parkOrgName?: string; // 园区名称
  processNo: string; // 当前流程节点
  remark: string; // 备注
  startDate: string; // 合同开始日期
  state: number | null; // 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期
  type: number | null; // 合同类型
  decorationStartDate?: string; // 装修开始日期（年月）
  decorationEndDate?: string; // 装修结束日期（年月）
  totalArea?: number; // 总面积（㎡）
  withdrawDate?: string; // 退约日期
  withdrawReason?: string; // 退约原因
  performanceBond?: string; // 履约保证金（元）
  extension: Extension
}

interface Extension {
  originContractId?: string; // 原合同id
  originContractNo?: string; // 原合同编号
  changeExplanation?: string; // 变更说明
  leaseTermYear?: number | null; // 租期（年）
  leaseTermDay?: number | null; // 租期（天）
  rentAmount?: number | null; // 租金（元）
  numberOfRooms?: number; // 客房数（间）
  numberOfSingleRooms?: number; // 单间
  numberOfTwinRooms?: number; // 标间（间）
  numberOfSuite?: number; // 套间（间）
  businessScope?: string; // 经营范围
  renovationEndDate?: string; // 装修结束日期
  renovationStartDate?: string; // 装修开始日期
  contactIdCardNoB?: string; // 乙方联系人身份证号码
  annualTaxTenThousand?: number | null; // 年纳税额（万元）
  performanceBond?: number | null; // 履约保证金（元）
  deposit?: number | null; // 押金（元）
  propertyCosts?: number | null; // 物业费（元）
}

interface ContractList {
  id: string;
  endDate: string;
  endTime: string;
  name: string;
  no: string;
  orgId: string;
  parkOrgId: string;
  startDate: string;
  startTime: string;
  state: number | null;
  attributes: number | null;
  type: number | null;
}

interface HouseStatusInfo {
  buildingName: string; // 楼栋名称
  parkOrgName?: string; // 园区名称
  startDate?: string; // 合同开始时间
  endDate?: string; // 合同结束时间
  id: string;
  buildingId: string; // 楼栋id
  name: string; // 楼层房号
  code: string; // 房屋编码
  floor: number | null; // 所在楼层
  coverArea: number | null; // 套内面积
  buildArea: number | null; // 建筑面积
  parkOrgId: string;
  contractNo: string, // 合同编号
  type: number | null, // 合同类型
  state: number | null, // 合同状态
  houseStatus: number | null, // 房屋状态
}

interface TerminationAndChange {
  id: string,
  changeType?: number | null,
  withdrawDate?: string,
  withdrawReason?: string,
}

// 1: 入园协议 2: 招引合同 3: 租赁合同管理 4: 物业合同管理 5: 公寓合同管理
type ContractType = 1|2|3|4|5

type Operatetypes = 'add' | 'export' | 'edit' | 'nullify' | 'verify' | 'change' | 'extend' | 'termination' | 'withdrawal' | 'del' | 'copy' | 'detail' | 'sendVerify'

interface VerifyInfo {
  id: string,
  auditOpinion: string, // 审核意见
  auditResult: string, //  审核结果:1-通过；2-不通过
}

// ----------------------------------- 合同模板管理------------------------------------

interface TemplateListSearchParams extends BaseSearchParams { // 合同列表查询
  parkOrgId:string,
  templateName:string, // 合同模板名称
}
interface ContractTemplateListInfo {
  type: ContractType|null,
  parkOrgId:string,
  templateKey:string, // 合同模板key
  templateName:string, // 合同模板名称
  id?: string,
  parkOrgName?:string,
  createByName?:string, // 创建人
  updateTime?:string, // 更新时间
}

export {
  SearchContractParams,
  Contract,
  ContractBillingPlanReqDTOS,
  ContractList,
  HouseStatusInfo,
  ContractType,
  TerminationAndChange,
  Operatetypes,
  VerifyInfo,
  HouseMessages,
  TemplateListSearchParams,
  ContractTemplateListInfo,
};
