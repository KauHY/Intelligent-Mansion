/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-04 18:06:18
 */

export const floorDialogTitleNames = {
  add: '新增楼栋',
  edit: '编辑楼栋',
  detailBuilding: '楼栋详情',
};

export const houseDialogTitleNames = {
  detailHouse: '房屋详情',
  detailBuilding: '楼栋详情',
  add: '新增房屋',
  edit: '编辑房屋',
  del: '',
  importHouse: '',
  importFloor: '',
  export: '',
};

export const elevatorNames = {
  0: '无',
  1: '有',
};

export const columns = [{
  title: '楼层房号',
  dataIndex: 'name',
  slots: { customRender: 'name' },
}, {
  title: '所属楼栋单元',
  dataIndex: 'buildingName',
  slots: { customRender: 'buildingName' },
}, {
  title: '关联园区',
  dataIndex: 'parkOrgName',
}, {
  title: '房屋编码',
  dataIndex: 'code',
}, {
  title: '所在楼层',
  dataIndex: 'floor',
}, {
  title: '建筑面积（㎡）',
  dataIndex: 'buildArea',
}, {
  title: '套内面积（㎡）',
  dataIndex: 'coverArea',
},
//  {
//   title: '房屋状态',
//   dataIndex: 'houseStatus',
//   slots: { customRender: 'houseStatus' },
// },
{
  title: '操作',
  dataIndex: 'operate',
  width: 180,
  // fixed: 'right',
  slots: { customRender: 'operate' },
}];
