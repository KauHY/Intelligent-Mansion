import { RoleListItem } from '~models/smarter-apartment';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getCustomerColumns = (options: DictionaryMap<['CARD_TYPE', 'CARD_MEDIUM', 'CARD_STATUS']>) => [
  {
    title: '卡号',
    dataIndex: 'cardNo',
    slots: { customRender: 'cardNo' },
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '卡介质',
    dataIndex: 'cardMedium',
    customRender: ({ record }: { record: RoleListItem }) => options.CARD_MEDIUM?.[record.cardMedium as number]?.label,
  },
  {
    title: '卡类型',
    dataIndex: 'cardType',
    customRender: ({ record }: { record: RoleListItem }) => options.CARD_TYPE?.[record.cardType as number]?.label,
  },
  {
    title: '持卡人',
    dataIndex: 'employeeName',

  },
  {
    title: '联系电话',
    dataIndex: 'mobile',
  },
  {
    title: '关联企业',
    dataIndex: 'companyName',
  },
  {
    title: '有效期',
    dataIndex: 'effectStartDate',
    slots: { customRender: 'effectStartDate' },
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender: ({ record }: { record: RoleListItem }) => options.CARD_STATUS?.[record.status as number]?.label,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 250,
    slots: { customRender: 'operate' },
  },
];
