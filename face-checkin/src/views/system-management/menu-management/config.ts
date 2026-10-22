/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-07 16:48:59
 */

export const menuTypeName = {
  0: '菜单',
  1: '按钮',
  2: '导航',
};

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const columns = [{
  title: '菜单名称',
  dataIndex: 'name',
}, {
  title: '菜单类型',
  dataIndex: 'type',
  slots: { customRender: 'type' },
},
// {
//   title: '菜单地址',
//   dataIndex: 'route',
// },
{
  title: '顺序',
  dataIndex: 'sort',
},
// {
//   title: '状态',
//   dataIndex: 'status',
//   slots: { customRender: 'status' },
// },
{
  title: '操作',
  dataIndex: 'operate',
  slots: { customRender: 'operate' },
}];

export const operateDialogTitleName = {
  add: '新增菜单',
  edit: '编辑菜单',
  resetMenu: '重置菜单',
};
