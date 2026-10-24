export const columns = [
  {
    title: '设备ID',
    dataIndex: 'deviceId',
  }, {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '表计名称',
    dataIndex: 'deviceName',
    slots: { customRender: 'deviceName' },
  }, {
    title: '表计属性',
    dataIndex: 'effectProperty',
    slots: { customRender: 'effectProperty' },
  }, {
    title: '表计类别',
    dataIndex: 'effectType',
    slots: { customRender: 'effectType' },
  }, {
    title: '抄表方式',
    dataIndex: 'meterType',
    slots: { customRender: 'meterType' },
  }, {
    title: '运行状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
  }, {
    title: '表计状态',
    dataIndex: 'usingStatus',
    slots: { customRender: 'usingStatus' },
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];
