/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 20:24:33
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '表单名称',
    dataIndex: 'title',
    slots: { customRender: 'title' },
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增表单',
  edit: '编辑表单',
  del: '删除',
  details: '表单详情',
};
