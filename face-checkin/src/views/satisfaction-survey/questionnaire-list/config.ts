/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 15:50:22
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '问卷名称',
    dataIndex: 'title',
    fixed: 'left',
    slots: { customRender: 'title' },
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '创建者',
    dataIndex: 'createByName',
  },
  {
    title: '答卷数',
    dataIndex: 'answeredCount',
  },
  {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    fixed: 'right',
    width: 500,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增问卷',
  edit: '编辑问卷',
  del: '删除',
  // details: '问卷详情',
};
