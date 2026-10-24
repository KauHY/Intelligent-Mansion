/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 18:35:16
 */
import { AttendanceRules } from '~models/attendance';

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
export const statusNames = {
  0: {
    label: '禁用',
    value: 0,
  },
  1: {
    label: '启用',
    value: 1,
  },
};

export const columns = [
  {
    title: '规则名称',
    dataIndex: 'ruleName',
    slots: { customRender: 'ruleName' },
  },
  {
    title: '上班时间',
    dataIndex: 'punchInTime',
  }, {
    title: '下班时间',
    dataIndex: 'punchOutTime',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '考勤地点',
    dataIndex: 'location',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    // customRender: ({ record }:{record:AttendanceRules}) => (record.status || record.status === 0 ? statusNames[record.status]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增考勤规则',
  edit: '编辑考勤规则',
  del: '删除',
  detail: '考勤规则详情',
};
