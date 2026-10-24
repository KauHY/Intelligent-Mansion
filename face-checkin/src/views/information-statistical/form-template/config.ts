/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 20:06:59
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [

  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '模板名称',
    dataIndex: 'title',
    slots: { customRender: 'title' },
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 360,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增模板',
  edit: '编辑模板',
  del: '删除',
  details: '模板详情',
};
