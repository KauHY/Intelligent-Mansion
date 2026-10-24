import { AddParams } from '~models/smarter-fire';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getCustomerColumns = (options: DictionaryMap<['SYSTEM_TYPE', 'WARN_TYPE', 'FILE_DEVICE_WARN_TYPE']>) => [
  {
    title: '设备ID',
    dataIndex: 'deviceId',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '设备名称',
    dataIndex: 'deviceName',
  },
  // {
  //   title: '所属系统',
  //   dataIndex: 'systemType',
  //   customRender: ({ record }: { record: AddParams }) => options.SYSTEM_TYPE?.[record.warnType as number]?.label,
  // },
  {
    title: '报警类型',
    dataIndex: 'warnType',
    customRender: ({ record }: { record: AddParams }) => options.FILE_DEVICE_WARN_TYPE?.[record.warnType as number]?.label,
  },
  // {
  //   title: '报警原因',
  //   dataIndex: 'warnReasonCode',
  //   customRender: ({ record }: { record: AddParams }) => options.FILE_DEVICE_WARN_TYPE?.[record.warnReasonCode as number]?.label,
  // },
  {
    title: '报警时间',
    dataIndex: 'warnTime',
  },
  {
    title: '处理状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 200,
    slots: { customRender: 'operate' },
  },
];
