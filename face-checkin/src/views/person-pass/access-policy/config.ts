/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-20 14:12:35
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { AccessPolicy } from '~models/person-pass';

export const columns = [
  {
    title: '策略名称',
    dataIndex: 'policyName',
    slots: { customRender: 'policyName' },
  },
  {
    title: '允许员工标签',
    dataIndex: 'tagsName',
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '关联企业',
    dataIndex: 'companyName',
  }, {
    title: '访客',
    dataIndex: 'allowVisitor',
    slots: { customRender: 'allowVisitor' },
    // customRender: ({ record }:{record:AccessPolicy}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '可通行的设备',
    dataIndex: 'deviceNames',
  }, {
    title: '状态',
    dataIndex: 'status',
    customRender: ({ record }:{record:AccessPolicy}) => (record.status === 1 ? '启用' : '禁用'),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增通行策略',
  edit: '编辑通行策略',
  del: '删除',
  details: '通行策略详情',
};
