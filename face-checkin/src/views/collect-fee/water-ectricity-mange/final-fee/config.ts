/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 16:55:17
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { BillingScheme, RentFeeInfo } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const columns = [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
    width: 150,
    fixed: 'left',
  },
  {
    title: '收费项',
    dataIndex: 'chargeName',
    slots: { customRender: 'chargeName' },
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeInfo}) => ((record.houseName ? record.houseName?.join(';') : '-')),

  },
  {
    title: '结算月份',
    dataIndex: 'settledMonth',
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
    title: '本期结算时间',
    dataIndex: 'settledTime',
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
    title: '数据来源',
    dataIndex: 'dataSource',
    slots: { customRender: 'dataSource' },
    // customRender: ({ record }:{record:BillingScheme}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
  },
  {
    title: '费用状态',
    dataIndex: 'feeStatus',
    slots: { customRender: 'feeStatus' },
    width: 80,
    fixed: 'right',
  },
];

export const getDetailColumns = [

  {
    title: '收费项',
    dataIndex: 'chargeName',
  },
  // {
  //   title: '房源名称',
  //   dataIndex: 'houseName',
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
    dataIndex: 'file',
    slots: { customRender: 'file' },
  },
];

export const operateDialogTitleName = {
  add: '新增',
  edit: '编辑',
  del: '删除',
  details: '详情',
};
