import { AuditParams } from '~models/company';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getCustomerColumns = (options: DictionaryMap<['PURCHASE_STATE', 'ADD_DATA_SOURCE', 'PURCHASE_TYPE' ]>) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '企业名称',
    dataIndex: 'companyName',

  },
  {
    title: '采购名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },
  },

  {
    title: '采购类型',
    customRender: ({ record }:{record:AuditParams}) => options.PURCHASE_TYPE?.[record.source as number]?.label,

  },
  {
    title: '创建日期',
    dataIndex: 'createTime',
  },
  {
    title: '截止日期',
    dataIndex: 'effectiveEndTime',
  },
  {
    title: '来源',
    dataIndex: 'source',
    customRender: ({ record }:{record:AuditParams}) => options.ADD_DATA_SOURCE?.[record.source as number]?.label,
  },
  {
    title: '状态',
    dataIndex: 'state',
    customRender: ({ record }:{record:AuditParams}) => options.PURCHASE_STATE?.[record.state as number]?.label,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 330,
    slots: { customRender: 'operate' },
  },
];

export const getCustomerTagColumns = (options: DictionaryMap<['SYSTEM_TYPE' ]>) => [
  {
    title: '序号',
    dataIndex: 'index',
  },
  {
    title: '企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '联系人',
    dataIndex: 'contact',
  },
  {
    title: '联系电话',
    dataIndex: 'tel',
  },
  {
    title: '邮箱',
    dataIndex: 'mail',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '支付招标文件费',
    dataIndex: 'biddingDocumentsPaid',
    slots: { customRender: 'biddingDocumentsPaid' },
  },
  {
    title: '保证金缴纳情况',
    dataIndex: 'marginPaid',
    slots: { customRender: 'marginPaid' },
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];
