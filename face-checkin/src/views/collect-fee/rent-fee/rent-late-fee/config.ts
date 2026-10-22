/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-27 19:00:01
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { RentFeeInfo } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getColumns = (options:DictionaryMap<['FEE_TYPE', 'RENT_LATE_STATUS']>, contractType: any) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
    width: 160,
    fixed: 'left',
  },
  {
    title: '账单编号',
    dataIndex: 'billNo',
    slots: { customRender: 'billNo' },
    width: 160,
    fixed: 'left',
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '房源名称',
    hidden: () => contractType === 6 || contractType === 7,
    dataIndex: 'houseName',
    customRender: ({ record }:{record:any}) => ((record.houseName ? record.houseName?.join(';') : '-')),
    // customRender: ({ record }:{record:BillingScheme}) => (record.chargingCycle || record.chargingCycle === 0 ? options.FEE_CHARGING_CYCLE?.[record.chargingCycle]?.label : ''),
  },
  {
    title: '面积（㎡）',
    hidden: () => contractType === 6 || contractType === 7,
    dataIndex: 'totalArea',
  },
  {
    title: '所在账期',
    dataIndex: contractType === 6 || contractType === 7 ? 'settledMonth' : 'accountPeriod',
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
    title: '已收滞纳金（元）',
    dataIndex: 'overdueFine',
  },
  {
    title: '免除滞纳金（元）',
    dataIndex: 'reduceFine',
  },
  {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    // customRender: ({ record }:{record:BillingScheme}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
    // dataIndex: 'remark',
  },
  {
    title: '操作',
    width: 100,
    fixed: 'right',
    slots: { customRender: 'operate' },
  },
];

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
];

export const operateDialogTitleName = {
  add: '新增表单',
  edit: '编辑表单',
  del: '删除',
  details: '表单详情',
};
