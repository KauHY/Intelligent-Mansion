/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-15 14:52:07
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 11:24:37
 */

interface SearchPaymentTrend { // 缴费趋势
  chargeType?:0|1|2|3|4|5, // 费用类型 0全部  1企业水电费 2公寓水电  3租赁 4物业 5公寓
  endMonth?:string, // 结束月份
  parkOrgId?:string,
  month?:string,
  startMonth?:string,
  type?:1|2, // 类型 1本月 2累计
}

// 缴费趋势
interface PaymentTrendItem {
  dueAmount:number, // 待收金额
  month:string, // 当前月份
  receivableAmount:number, // 应收金额
  receivedAmount:number, // 已收金额
}

// 收入分析
interface IncomeNnalysis {
  totalAmount:number,
  billData:{
    [key:number]:number
  }
}

interface OverdueTrendItem { // 逾期客户趋势
  customers:number, // 逾期客户数量
  dueAmount:number, // 逾期金额
  month:string, // 当前月份
}

interface LateCustomerItem { // 逾期客户top5
  companyName:number, // 逾期客户名称
  dueAmount:number, // 逾期金额
  dueDays:number, // 逾期天数
}

interface TopStatistic { // 收费统计-顶部统计
  billReceived:number, // 账单收入（元）
  dueAmount:number, // 待收金额（元）
  overDateAmount:number, // 逾期金额（元）
  payRate:number, // 收缴率
  receivedAmount:number, // 已收金额（元）

}

export {
  SearchPaymentTrend,
  PaymentTrendItem,
  IncomeNnalysis,
  OverdueTrendItem,
  LateCustomerItem,
  TopStatistic,
};
