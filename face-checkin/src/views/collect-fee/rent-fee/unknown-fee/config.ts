/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-21 14:49:37
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { RentFeeInfo } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const columns = [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '收费项',
    dataIndex: 'chargeName',
    slots: { customRender: 'chargeName' },
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  }, {
    title: '合同编号',
    dataIndex: 'contractNo',
    // customRender: ({ record }:{record:BillingScheme}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeInfo}) => ((record.houseName ? record.houseName?.join(';') : '-')),
    // customRender: ({ record }:{record:BillingScheme}) => (record.chargingCycle || record.chargingCycle === 0 ? options.FEE_CHARGING_CYCLE?.[record.chargingCycle]?.label : ''),
  },
  {
    title: '建筑面积（㎡）',
    dataIndex: 'totalArea',
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
    title: '数据来源',
    dataIndex: 'dataSource',
    slots: { customRender: 'dataSource' },
    // customRender: ({ record }:{record:BillingScheme}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
  },
  {
    title: '应收金额（元）',
    dataIndex: 'receivableAmount',
  },
  {
    title: '状态',
    dataIndex: 'billingStatus',
    slots: { customRender: 'billingStatus' },
    // customRender: ({ record }:{record:BillingScheme}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
    // dataIndex: 'remark',
  },
];

export const operateDialogTitleName = {
  add: '新增',
  edit: '批量修改收款日期',
  del: '删除',
  details: '详情',
};
