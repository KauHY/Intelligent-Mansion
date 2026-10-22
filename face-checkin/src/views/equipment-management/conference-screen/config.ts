import { RoleListItem } from '~models/smarter-apartment';
import { DictionaryMap } from '~composition/useHashMapDic';
import { EntranceGuardTable } from '~models/entranceGuard-management';

export const getCustomerColumns = (options: DictionaryMap<['SCREEN_DEVICE_STATUS', 'DEVICE_TYPE']>) => [
  {
    title: '设备名称',
    dataIndex: 'deviceName',
    slots: { customRender: 'deviceName' },
  },
  {
    title: '设备类型',
    dataIndex: 'deviceType',
    customRender: ({ record }:{record:EntranceGuardTable}) => (record.deviceType || record.deviceType === 0 ? options.DEVICE_TYPE?.[record.deviceType]?.label : ''),
  },
  {
    title: '设备ID',
    dataIndex: 'deviceId',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender: ({ record }: { record: RoleListItem }) => options.SCREEN_DEVICE_STATUS?.[record.status as number]?.label,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 200,
    slots: { customRender: 'operate' },
  },
];
