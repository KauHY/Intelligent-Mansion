export const statusName = {
  0: '禁用',
  1: '启用',
};

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
  }, {
    title: '能耗类别',
    dataIndex: 'deviceType',
    slots: { customRender: 'deviceType' },
  }, {
    title: '告警事件',
    dataIndex: 'warningCode',
    slots: { customRender: 'warningCode' },
  }, {
    title: '告警等级',
    dataIndex: 'warningLevel',
  }, {
    title: '告警时间',
    dataIndex: 'warningTime',
  },
];
