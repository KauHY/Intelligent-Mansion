export const statusName = {
  0: '禁用',
  1: '启用',
};

export const statusOpt = [
  {
    label: '禁用',
    value: '0',
  }, {
    label: '启用',
    value: '1',
  },
];

export const sourceOpt = [
  {
    label: '手动抄表',
    value: 1,
  }, {
    label: '自动抄表',
    value: 2,
  },
];

export const categoryOpt = [
  {
    label: '电能耗',
    value: 6,
  }, {
    label: '水能耗',
    value: 5,
  },
];

export const columns = [{
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
  title: '能耗类别',
  dataIndex: 'deviceType',
  slots: { customRender: 'deviceType' },
}, {
  title: '表计属性',
  dataIndex: 'effectProperty',
  slots: { customRender: 'effectProperty' },
}, {
  title: '表计类别',
  dataIndex: 'effectType',
  slots: { customRender: 'effectType' },
}, {
  title: '数据来源',
  dataIndex: 'thisMonth',
  slots: { customRender: 'thisMonth' },
}, {
  title: '抄表读数',
  dataIndex: 'meterReading',
}, {
  title: '抄表时间',
  dataIndex: 'readingTime',
}, {
  title: '导入备注',
  dataIndex: 'importRemark',
}, {
  title: '修正后读数',
  dataIndex: 'correctedReading',
}, {
  title: '修正备注',
  dataIndex: 'remark',
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 300,
  slots: { customRender: 'operate' },
}];
