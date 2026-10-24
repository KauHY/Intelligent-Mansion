export const getHouseColumns = () => [
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
    customRender: ({ record }:{record:any}) => (`${record.buildingName}`),
  },
  {
    title: '楼层房号',
    dataIndex: 'houseName',
  },
  {
    title: '房屋功能',
    dataIndex: 'houseFunction',
    slots: { customRender: 'houseFunction' },
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];

export const getContractColumns = () => [
  {
    title: '房源名称',
    dataIndex: 'buildingName',
    customRender: ({ record }:{record:any}) => (`${record.buildingName}-${record.name}`),
  },
  {
    title: '建筑面积（㎡）',
    dataIndex: 'buildArea',
  },
  {
    title: '总楼层数',
    dataIndex: 'totalFloor',
  },
  {
    title: '所在楼层',
    dataIndex: 'floor',
  },
  {
    title: '房屋功能',
    dataIndex: 'houseFunction',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];
