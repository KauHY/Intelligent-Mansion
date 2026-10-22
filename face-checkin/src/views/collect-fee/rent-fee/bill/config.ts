/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 17:26:12
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { RentFeeBillInfo } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getColumns = (contractType: 3|4|5|6|7) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
    width: 140,
    fixed: 'left',
  },
  {
    title: '账单编号',
    dataIndex: 'billNo',
    width: 150,
    fixed: 'left',
    slots: { customRender: 'billNo' },
  },
  {
    title: '所在账期',
    dataIndex: 'accountPeriod',
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '入住人员',
    dataIndex: 'employeeIdNames',
    hidden: () => contractType !== 5,
    customRender: ({ text }:{text:string[]}) => text?.join(';'),
  },
  {
    title: '所含收费项',
    dataIndex: 'chargeName',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeBillInfo}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '计费周期',
    hidden: () => contractType === 6 || contractType === 7,
    customRender: ({ record }:{record:RentFeeBillInfo}) => (`${record.startDate}-${record.endDate}`),
  },
  {
    title: '应收日期',
    dataIndex: 'dueDate',
  },
  {
    title: '最晚缴纳日期',
    dataIndex: 'latestPayDate',
  },

  {
    title: '应收金额（元）',
    width: 150,
    dataIndex: 'receivableAmount',
  },
  {
    title: '待收金额（元）',
    width: 150,
    dataIndex: 'dueAmount',
  },
  {
    title: '已收金额（元）',
    width: 150,
    dataIndex: 'receivedAmount',
  },
  {
    title: '减免金额（元）',
    width: 150,
    dataIndex: 'deductionAmount',
  },
  {
    title: '数据来源',
    dataIndex: 'dataSource',
    slots: { customRender: 'dataSource' },
  },
  {
    title: '收款状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    // customRender: ({ record }:{record:RentFeeBillInfo}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
  },
  {
    title: '收款日期',
    width: 150,
    dataIndex: 'checkoutDate',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 200,
    fixed: 'right',
    slots: { customRender: 'operate' },
  },
];

// 费用明细
export const feeDetailsColumns = [
  {
    title: '收费项',
    dataIndex: 'chargeName',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeBillInfo}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '计费周期',
    // dataIndex: 'billingCycle',
    customRender: ({ record }:{record:RentFeeBillInfo}) => (`${record.startDate}-${record.endDate}`),
  },
  {
    title: '应收金额（元）',
    dataIndex: 'receivableAmount',
  },
  {
    title: '待收金额（元）',
    dataIndex: 'dueAmount',
  },
  {
    title: '实收金额（元）',
    dataIndex: 'receivedAmount',
  },
  {
    title: '减免金额（元）',
    dataIndex: 'deductionAmount',
  },
];

// 减免记录
export const deductionRecordColumns = [
  {
    title: '序号',
    // dataIndex: 'tagName',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '减免金额（元）',
    dataIndex: 'deductionAmount',
  },
  {
    title: '操作人员',
    dataIndex: 'createByName',
  },
  {
    title: '操作时间',
    dataIndex: 'createTime',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '附件',
    dataIndex: 'fileList',
    ellipsis: false,
    slots: { customRender: 'fileList' },
  },
];

// 滞纳金明细
export const lateFeeDetailColumns = [
  {
    title: '收费项',
    dataIndex: 'chargeName',
  },
  {
    title: '逾期金额（元）',
    dataIndex: 'dueAmount',
  },
  {
    title: '逾期天数（元）',
    dataIndex: 'dueDays',
  },
  {
    title: '应收滞纳金（元）',
    dataIndex: 'receivableFine',
  },
  {
    title: '待收滞纳金（元）',
    dataIndex: 'receiveFine',
  },
  {
    title: '免除滞纳金（元）',
    dataIndex: 'reduceFine',
  },
  {
    title: '实收滞纳金（元）',
    dataIndex: 'overdueFine',
  },
];

// 收款记录
export const checkoutRecordColumns = [
  {
    title: '序号',
    // dataIndex: 'tagName',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '账单编号',
    dataIndex: 'tradeNo',
    width: 160,
  },
  {
    title: '实收金额（元）',
    dataIndex: 'amount',
  },
  {
    title: '收款方式',
    dataIndex: 'payType',
    slots: { customRender: 'payType' },
  },
  {
    title: '收款日期',
    dataIndex: 'checkoutDate',
  },

  {
    title: '操作人员',
    dataIndex: 'createByName',
  },
  {
    title: '操作时间',
    dataIndex: 'createTime',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
];

export const waterBillDetailColumns = [

  {
    title: '收费项',
    dataIndex: 'chargeName',
  },
  // {
  //   title: '房源名称',
  //   dataIndex: 'houseName',
  //   customRender: ({ record }:{record: any}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  // },
  {
    title: '金额（元）',
    dataIndex: 'amountCount',
  },
  // {
  //   title: '本月示数',
  //   dataIndex: 'thisReading',
  // },
  // {
  //   title: '上月底数',
  //   dataIndex: 'lastReading',
  // },
  {
    title: '本月用量',
    dataIndex: 'roomAmount',
  },
  {
    title: '本月公共用量',
    dataIndex: 'publicAmount',
  },
  {
    title: '本月应摊用量',
    dataIndex: 'shareAmount',
  },
  {
    title: '本月实际用量',
    dataIndex: 'totalAmount',
  },
];

export const operateDialogTitleName = {
  edit: '编辑账单',
  details: '账单详情',
  batchReceive: '批量收款',
  deduction: '减免',
  receive: '收款',
};
