import { DictionaryMap } from '~composition/useHashMapDic';

export const getCustomerColumns = (options: DictionaryMap<['DEVICE_STATUS' ]>) => [
  {
    title: '设备ID',
    dataIndex: 'deviceId',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  // {
  //   title: '所属系统',
  //   dataIndex: 'systemType',
  //   slots: { customRender: 'systemType' },

  // },
  {
    title: '设备名称',
    dataIndex: 'deviceName',
    slots: { customRender: 'deviceName' },
  },
  {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    // customRender: ({ record }:{record:AttendanceRules}) => (record.status || record.status === 0 ? statusNames[record.status]?.label : ''),
  },

  // {
  //   title: '所属楼栋',
  //   dataIndex: 'buildingName',
  // },
  // {
  //   title: '所属楼层',
  //   dataIndex: 'floor',
  //   slots: { customRender: 'floor' },
  // },
  // {
  //   title: '所属房屋',
  //   dataIndex: 'houseName',
  // },
  // {
  //   title: '安装位置',
  //   dataIndex: 'installationLocation',
  // },
  // {
  //   title: '操作',
  //   dataIndex: 'operate',
  //   width: 200,
  //   slots: { customRender: 'operate' },
  // },
];
