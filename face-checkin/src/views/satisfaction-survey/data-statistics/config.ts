/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 16:19:51
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '问卷名称',
    dataIndex: 'title',
    slots: { customRender: 'title' },
  },
  {
    title: '用户手机号码',
    dataIndex: 'mobile',
  },
  {
    title: '提交时间',
    dataIndex: 'createTime',
  },
  // {
  //   title: '操作',
  //   dataIndex: 'operate',
  //   width: 360,
  //   slots: { customRender: 'operate' },
  // }
];

export const textQuestionnaireColumns = [
  {
    title: '序号',
    dataIndex: 'index',
    customRender: ({ text, record, index }:any) => index + 1,
  },
  {
    title: '答案文本',
    dataIndex: 'text',
    slots: { customRender: 'text' },
  },
];

export const fileQuestionnaireColumns = [
  {
    title: '序号',
    dataIndex: 'index',
    customRender: ({ text, record, index }:any) => index + 1,
  },
  {
    title: '附件',
    dataIndex: 'key',
    // slots: { customRender: 'fileName' },
  },
];

export const operateDialogTitleName = {
  // add: '新增问卷',
  // edit: '编辑问卷',
  // del: '删除',
  details: '问卷详情',
};
