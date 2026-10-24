import { AuditParams } from '~models/company';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getCustomerColumns = (options: DictionaryMap<['SUPPLY_TYPE', 'SUPPLY_PUBLISH_STATUS', 'ADD_DATA_SOURCE' ]>) => [

  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '服务名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },

  },
  {
    title: '供应类型',
    dataIndex: 'supplyType',
    customRender: ({ record }: { record: AuditParams }) => options.SUPPLY_TYPE?.[record.supplyType as number]?.label,
  },

  {
    title: '发布时间',
    dataIndex: 'publishTime',

  },
  {
    title: '来源',
    dataIndex: 'source',
    customRender: ({ record }: { record: AuditParams }) => options.ADD_DATA_SOURCE?.[record.source as number]?.label,
  },
  {
    title: '状态',
    dataIndex: 'state',
    customRender: ({ record }: { record: AuditParams }) => options.SUPPLY_PUBLISH_STATUS?.[record.state as number]?.label,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 200,
    slots: { customRender: 'operate' },
  },
];
