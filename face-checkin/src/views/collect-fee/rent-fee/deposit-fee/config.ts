/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-21 14:46:11
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { RentFeeBillInfo, RentFeeInfo, RentDepositFeeDeductionContent } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getColumns = (options:DictionaryMap<['FEE_TYPE', 'RENT_CASH_STATUS']>) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
    width: 150,
    fixed: 'left',
  },
  {
    title: '合同编号',
    dataIndex: 'contractNo',
    width: 200,
    slots: { customRender: 'contractNo' },
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '收费项',
    dataIndex: 'chargeName',
    ellipsis: false,
    // slots: { customRender: 'feeType' },
  },

  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeInfo}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '计费周期',
    // dataIndex: 'billingCycle',
    customRender: ({ record }:{record:RentFeeInfo}) => (`${record.startDate}-${record.endDate}`),
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
    dataIndex: 'receivableAmount',
  },
  {
    title: '待收金额（元）',
    dataIndex: 'dueAmount',
  },
  {
    title: '已收金额（元）',
    dataIndex: 'receivedAmount',
  },
  {
    title: '减免金额（元）',
    dataIndex: 'deductionAmount',
  },
  {
    title: '已抵扣金额（元）',
    dataIndex: 'offsetAmount',
  },
  {
    title: '已退款金额（元）',
    dataIndex: 'refundedAmount',
  },
  {
    title: '退租日期',
    dataIndex: 'leaseDate',
  },
  {
    title: '状态',
    dataIndex: 'cashStatus',
    slots: { customRender: 'cashStatus' },
    // customRender: ({ record }:{record:BillingScheme}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
    // dataIndex: 'remark',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },

  {
    title: '经办人',
    dataIndex: 'userName',
  },
  {
    title: '办理日期',
    dataIndex: 'handleTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    slots: { customRender: 'operate' },
    width: 150,
    fixed: 'right',
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

export const deductionContentColumns = [ // 抵扣内容
  {
    title: '抵扣内容',
    dataIndex: 'remark',
    slots: { customRender: 'remark' },
    // customRender: ({ record }:{record:any}) => `${record.chargeName}${record.startDate.year}-${record.startDate.monthValue}-${record.startDate.dayOfMonth}-${record.endDate.year}-${record.endDate.monthValue}-${record.endDate.dayOfMonth}`,
  },
  {
    title: '已抵扣金额（元）',
    dataIndex: 'deAmount',
  },
];

export const depositFeeRentrnRecordColumns = [ // 押金退还记录
  {
    title: '退款金额（元）',
    dataIndex: 'refundedAmount',
  },
  {
    title: '操作人员',
    dataIndex: 'userName',
  },
  {
    title: '办理时间',
    dataIndex: 'handleTime',
  },
];

export const selectDeductionContentColumns = [ // 选择抵扣内容列表
  {
    title: '收费项',
    dataIndex: 'chargeName',
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeBillInfo}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '合同编号',
    dataIndex: 'contractNo',
    width: 200,
    slots: { customRender: 'contractNo' },
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
];

export const operateDialogTitleName = {
  add: '新增表单',
  edit: '编辑表单',
  del: '删除',
  details: '表单详情',
};
