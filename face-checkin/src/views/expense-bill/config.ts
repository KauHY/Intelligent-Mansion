/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 13:47:19
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { RentFeeInfo } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const columns = [
  {
    title: '账单编号',
    dataIndex: 'billNo',
    slots: { customRender: 'billNo' },
  },
  {
    title: '所在账期',
    dataIndex: 'accountPeriod',
  },
  {
    title: '企业名称',
    dataIndex: 'companyName',
  }, {
    title: '所含收费项',
    dataIndex: 'chargeName',
    ellipsis: false,
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:any}) => ((record.houseName ? record.houseName?.join(';') : '-')),
    // customRender: ({ record }:{record:BillingScheme}) => (record.chargingCycle || record.chargingCycle === 0 ? options.FEE_CHARGING_CYCLE?.[record.chargingCycle]?.label : ''),
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
    title: '待收金额（元）',
    dataIndex: 'dueAmount',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];

export const operateDialogTitleName = {
  add: '新增',
  edit: '批量修改收款日期',
  del: '删除',
  details: '详情',
};
