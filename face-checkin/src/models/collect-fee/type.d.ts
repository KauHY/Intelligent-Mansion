/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-17 11:13:55
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-21 14:43:32
 */
interface ChargeItemSearchParams extends BaseSearchParams { // 费用项分页查询
}
interface ChargeItem { // 收费项信息
    chargeName: string, // 收费项名称
    feeType: number|null, // 收费项类型
    id?: string,
    parkOrgId: string, // 园区
    parkOrgName?: string,
    payeeId: number|null, // 收款方
    remark: string, // 备注
    status: 0|1, // 状态：0-关闭；1-开启
    taxRate: number|null, // 税率
    updateTime?:string
}
interface settlementPayParams {
  checkoutDate:	string	// 公司ID
  remark:	string	// 合同id
}

interface SettlementParams {
companyId:	string	// 公司ID
contractId:	string	// 合同id
leaveDate:	string	// 结算日期
parkOrgId:	string
}

interface BillingSchemeSearchParams extends BaseSearchParams { // 计费方案分页查询
}

interface leaveSettlementCreatedParams {
  houseId?:string,
  companyId	:string	// 公司ID
  companyName:	string	// 公司名称
  contractId:	string	// 合同id
  contractNo:	string	// 合同编号
  leaveDate:	string	// 结算日期
  parkOrgId:	string	// 园区组织ID
  parkOrgName:	string
  contractType: 3|4|5, // 合同费用类型3-租赁；4-物业；5-公寓
}
interface BillingScheme { // 收费项信息
    feeType?:number,
    lateCharge:number,
    lateDayAmount:number|null, // 每日滞纳金
    monthBy: number|null, // 账单生成日期-按月
    lateType: number|null, // 滞纳率计算规则
    amountWithTax: number|null, // 含税金额
    amountWithoutTax: number|null, // 不含税金额
    billingCycle: number|null, // 计费周期,计费周期：1-一次性；2-月；3-季；4-年
    calculationStandard: number|null, // 计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价
    carryMode: number|null, // 进位方式：4-四舍五入；0-向上取整；1-向下取整
    chargeItemDTO?: ChargeItem, // 收费项
    chargeItemId: string, // 收费项id
    chargeType: number|null, // 收费方式
    chargingCycle: number|null, // 收费周期：1-与计费周期一致；2-多计费周期
    cumulativeCycle: number|null, // 累计周期
    dayOfMonth: number|null, // 账单生成日期:每月第x天生成账单
    daysForDelay: number|null, // 最晚缴纳日期 最晚缴纳日期 最晚缴纳日期，账单后的第几天
    exemptDays: number|null, // 豁免天数
    formulaType: number|null, // 公式类型
    id: string,
    lateRate: number|null, // 滞纳率
    parkOrgId: string,
    parkOrgName: string,
    houseName?:string[],
    reservedDigits: number|null, // 保留位数,保留位数：0-取整数；1-保留一位小数；2-保留两位小数
    schemeName: string, // 方案名称
    status: number|null, // 状态：0-关闭；1-开启
    unitPriceWithTax: number|null, // 单价元（含税）
    unitPriceWithoutTax: number|null, // 单价元（不含税）
    taxRate?:number|null,
    updateTime?:string
}

interface Calculation {
  billingEndDate?: string, // 计费结束日期
  billingStartDate?: string, // 计费开始日期
  buildingAreas?: number[], // 建筑面积队列
  calculationStandard: number|null, // 计费标准
  carryMode: number|null, // 进位方式：4-四舍五入；0-向上取整；1-向下取整
  coverAreas?: number[], // 套内面积队列
  dosage?: number|null, // 用量
  reservedDigits: number|null, // 保留位数：0-取整数；1-保留一位小数；2-保留两位小数
  unitPrice: number|null, // 单价
  freeRentDay?: number|null, // 免租期
  numberOfRooms?: number|null, // 房间数
  feeType: number|null,
}

// ------------- 租赁/物业/公寓/费用 ---------------------
interface RentFeeSearchParams extends BaseSearchParams { // 已出/未出/账单费用分页查询
  companyName:string, // 入驻企业名称
  parkOrgId:string, // 园区id
  contractNo:string, // 合同编码
  dueDate:string, // 应收日期(前端界面使用)
  dueDateEnd:string,
  dueDateStart:string,
  latestPayDate:string, // 最晚缴纳日期(前端界面使用)
  latestPayDateEnd:string,
  latestPayDateStart:string,
  billingStatus:string, // 出账状态 1待出账 2已出账 3已废除
  status:string, // 收款状态：0未收，1已收
  queryType:0|1|2|null, // 0：查询未出费用  1：查询已出费用  2：押金费用
  times:[], // 页面使用
  leaseDateEnd?:string, // 退租开始时间
  leaseDateStart?:string, // 退租结束时间
  cashStatus?:1|2|3, // 押金状态 1未缴清 2已缴清 3已结清
}

interface baseRentFee {
  id:string,
  contractId:string, // 合同id
  contractNo:string, // 合同编号
  parkOrgId:string,
  parkOrgName:string,
  feeType:string, // 收费项类型
  totalArea:number|null, // 建筑面积（㎡）
  companyId:string,
  companyName:string, // 入驻企业名称
  houseName:string[], // 房源名称
  startDate:string, // 计费周期 开始日期
  endDate:string, // 计费周期 结束日期
  dueDate:string, // 应收日期
  latestPayDate:string, // 最晚缴纳日期
  dataSource: 1 // 数据来源 1合同生成
  receivableAmount:number, // 应收金额
  dueAmount:number, // 待收金额
  receivedAmount:number, // 已收金额
  deductionAmount:number, // 减免金额
  billingStatus: 1|2|3, // 出账状态 1待出账 2已出账 3已废除
  cashStatus:1|2|3, // 押金状态 未缴清		押金状态 1未缴清 2已缴清 3已结清
  offsetAmount:number, // 已抵扣金额
  refundedAmount:number, // 押金已退款金额
  leaseDate:string, // 退租日期
  remark:string, // 备注
  chargeName:string, // 所含收费项名称
}

interface RentFeeInfo extends baseRentFee { // 租赁费用
  employeeId:string, // 经办人
  handleTime:string, // 办理时间
  createBy:string, // 创建人
  updateBy:string, // 修改人
  updateTime:string, // 修改时间
  feeStatus: 0|1, // 费用状态 0未收 1已收
  chargeDetail?:object,
}

interface RentFeeInfoDetails extends RentFeeInfo { // 租赁费用详情
  deductionMsg:RentDepositFeeDeductionContent[], // 抵扣内容
  deduction: 0|1, // 是否抵扣
  deAmount:number, // 抵扣金额
  userName:string,
  deductionRecordList:DeductionRecord[]
  depositFeeRentrnRecord: { // 押金退还记录
    refundedAmount:number,
    handleTime:string,
    userName:string, // 操作人
  }[],
}

interface UnknownRentFeeModifyDate{ // 租赁未出费用修改收款日期
  ids:string[],
  dueDate:string,
  latestPayDate:string
}

interface RentFeeBillInfo extends baseRentFee { // 租赁费用账单
  billNo:string,
  checkoutDate:string, // 收款日期
  status: 0|1, // 费用状态  0未收 1已收
  accountPeriod:string, // 所在账期
  employeeIdNames:string[], // 入驻人员
  employeeIds:string,
  payType:1|null, // 支付方式
}

interface CheckoutRecordList { // 收款记录明细
  dueAmount:number, // 待收金额
  endDate:string, // 计费周期 结束日期
  startDate:string,
  feeType:number, // 收费项
  houseName:string[],
  id:string,
  receivableAmount:number, // 应收金额
  receivedAmount:number,
}

interface RentDepositFeeDeductionContent {
  chargeName:string,
  deAmount:number,
  endDate:string, // 计费周期开始时间
  startDate:string, // 计费周期结束时间
  id:string,
}

type RentDepositFeeDeductionContentSelectData = {
  [props:string]:RentDepositFeeDeductionContent
}

interface ReturnRentDepositFee { // 租赁押金退还
  deduction:0|1, // 是否抵扣
  deductionMsg:RentDepositFeeDeductionContent[] // 抵扣内容
  id:string,
  offsetAmount:number, // 待抵扣总金额
  refundedAmount:number, // 待退还金额
  remark:string, //
}

interface RentFeeDetail {

}

interface DeductionRecord {
  deductionAmount:string,
  createByName:string,
  createTime:string,
  remark:string,
  fileList:{name:string, key:string}[]
}
interface RentFeeBillDetail extends baseRentFee { // 租赁费用账单明细
  billNo:string,
  checkoutDate:string, // 收款日期
  status: number, // 费用状态  0未收 1已收
  accountPeriod:string, // 所在账期
  employeeIdNames:string[], // 入驻人员
  employeeIds:string,
  checkoutRecordList:CheckoutRecordList[], // 收款记录
  deductionRecordList:DeductionRecord[], // 减免记录
  feeDetailList:RentFeeDetail[], // 费用明细
  lateFeeDetailList:RentLateFeeInfo[], // 滞纳金明细
  companyFiles:string, // 凭证（企业端上传的文件key）
  payType:1, // 支付方式
  chargeDetail?:[],
}

interface BeforeCheckoutRentFee { // 租赁费用收款前合计
  receivableFine:number, // 应收滞纳金
  receiveFine:number, // 待收滞纳金
  dueAmount:number, // 逾期金额/待收金额
  totalAmount:number, // 总金额（元）
  overdueFine:number, // 已收滞纳金
}

interface RentFeeTransactionDetail extends RentFeeBillDetail { // 交易明细
  extraTradeNo:string,
  returnAmount:number, // 租赁费用退款金额（退全部已收金额）
  returnTime:string, // 退款时间
  tradeNo:string, // 订单编号
  type:1|2, // 订单类型 1付款订单，2退款订单
  billId:string, // 账单id
  applicantName:string, // 申请人
  createTime:string, // 支付时间
  createByName:string, // 操作人
  canReturn: 0|1, // 是否可以退款
  extraTradeId:string, // 退款记录id
  returnReason:string, // 退款原因
  transactionDetailList:{
    chargeName:string, // 所含收费项名称
    houseName:string[], // 房源名称
    accountPeriod:string, // 所在账期
    receivableAmount:number, // 应收金额
    receivedAmount:number, // 实付金额
  }[],
  returnMoneyRecords:{
    extraTradeNo:string, // 退款记录单号
    applicantName:string, // 申请人
    returnTime:string, // 退款时间
    returnAmount:number, // 退款金额
  }[]
  returnMoneyContent:{
    accountPeriod:string, // 所在账期
    chargeName:string, // 所含收费项名称
    houseName:string[],
    returnAmount:number
  }[]
}
interface RentFeeTransactionDetailSearchParams extends BaseSearchParams { // 分页查询交易明细
  companyName:string, // 入驻企业名称
  parkOrgId:string, // 园区id
  billNo:string, // 账单编号
  times:string, // 收款日期（仅前端使用）
  payTimeEnd:string, // 收款结束时间
  payTimeStart:string, // 收款开始时间
  type:1|2, // 订单类型
  extraTradeNo:string, // 退款记录单号
}

interface RentFeeReturnMoney {
  id:string,
  payType:1, // 退款方式:1线下银行转账
  remark:string,
  returnAmount:number | null, // 退款金额
  returnReason:string, // 退款原因
}

interface RentLateFeeInfo extends RentFeeBillDetail{ // 滞纳金信息
  dueAmount:number, // 逾期金额
  dueDays:number, // 逾期天数
  overdueFine:number, // 已收滞纳金
  receivableFine:number, // 应收滞纳金
  receiveFine:number, // 待收滞纳金
  reduceFine:number, // 减免滞纳金
  rentBillId:string, // 租赁账单id
  status: number, // 状态 1计算中 2已缴清 3已免除
  createByName:string,
  createTime:string,
  contractType?:number,
}

interface RentLateFeeSearchParams extends BaseSearchParams{ // 滞纳金分页查询
  companyName:string, // 入驻企业名称
  parkOrgId:string, // 园区id
  status: number, // 状态 1计算中 2已缴清 3已免除
}

export {
  ChargeItemSearchParams,
  ChargeItem,
  BillingSchemeSearchParams,
  BillingScheme,
  Calculation,
  SettlementParams,
  settlementPayParams,
  // ------ 租赁费用-------
  RentFeeSearchParams,
  RentFeeInfo,
  UnknownRentFeeModifyDate,
  RentFeeBillInfo,
  RentFeeBillDetail,
  BeforeCheckoutRentFee,
  RentFeeTransactionDetailSearchParams,
  RentFeeTransactionDetail,
  RentFeeReturnMoney,
  ReturnRentDepositFee, // 押金退款'
  RentDepositFeeDeductionContent, // 押金退款抵扣内容
  leaveSettlementCreatedParams,
  RentDepositFeeDeductionContentSelectData, // 勾选选中的押金退款抵扣数据
  RentLateFeeInfo,
  RentLateFeeSearchParams,
  RentFeeInfoDetails,
};
