export const columns = [{
  title: '所属园区',
  dataIndex: 'parkOrgName',
}, {
  title: '所属楼栋单元',
  dataIndex: 'buildingName',
}, {
  title: '所属楼层',
  dataIndex: 'floors',
  customRender: ({ record }:{record:any}) => ((record.floors ? record.floors?.join('层;') : '-')),
}, {
  title: '所属房屋',
  dataIndex: 'houseNames',
  customRender: ({ record }:{record:any}) => ((record.houseNames ? record.houseNames?.join(';') : '-')),
}, {
  title: '设备ID',
  dataIndex: 'deviceId',
}, {
  title: '表计名称',
  dataIndex: 'deviceName',
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
  title: '结算月份',
  dataIndex: 'settledMonth',
}, {
  title: '上期结算时间',
  dataIndex: 'lastTime',
}, {
  title: '本期结算时间',
  dataIndex: 'thisTime',
}, {
  title: '上月底数',
  dataIndex: 'lastReading',
}, {
  title: '本月示数',
  dataIndex: 'thisReading',
}, {
  title: '本月实际用量',
  dataIndex: 'consumption',
}];
