/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-13 17:45:34
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '序号',
    dataIndex: 'index',
    customRender: ({ text, record, index }:any) => index + 1,
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '模板名称',
    dataIndex: 'templateName',
    // slots: { customRender: 'templateName' },
  },
  {
    title: '合同类型',
    dataIndex: 'type',
    slots: { customRender: 'type' },
  },
  {
    title: '创建人',
    dataIndex: 'createByName',
  },
  {
    title: '更新日期',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];

export const operateDialogTitleName = {
  add: '新增表单',
  edit: '编辑表单',
  del: '删除',
  details: '表单详情',
};
