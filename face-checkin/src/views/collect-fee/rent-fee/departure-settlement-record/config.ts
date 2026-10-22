/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-13 16:38:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-21 14:45:28
 */
import { BillingScheme } from '~models/collect-fee';

export const getCustomerColumns = () => [

  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
    slots: { customRender: 'companyName' },
  },
  {
    title: '计费类型',
    dataIndex: 'type',
    customRender: () => '离场结算',
  },
  {
    title: '实收总金额（元）',
    dataIndex: 'totalAmount',
  },

  {
    title: '提交时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作人',
    dataIndex: 'createByName',
  },
];

export const getDetailColumns = () => [

  {
    title: '收费项',
    dataIndex: 'chargeName',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:BillingScheme}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '计费周期',
    customRender: ({ record }:{record:any}) => `${record.startDate || ''}-${record.endDate || ''}`,
  },

  {
    title: '实收金额（元）',
    dataIndex: 'dueAmount',
  },
  {
    title: '实收滞纳金（元）',
    dataIndex: 'receiveFine',
  },
];
