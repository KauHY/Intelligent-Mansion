/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-24 15:27:14
 */
import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { VisitorProcess } from '~models/visitors-management';

export const invitorAuditOpts = {
  1: {
    label: '被访人审核',
    value: 1,
  },
  0: {
    label: '自定义审核',
    value: 0,
  },
};

export const getColumns = (options:Ref<DictionaryMap<['VISIT_SOURCE_TYPE', 'VISIT_PROCESS_NODE']>>) => [
  {
    title: '序号',
    // dataIndex: 'tagName',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '企业名称',
    dataIndex: 'companyName',
  },
  {
    title: '流程名称',
    dataIndex: 'processName',
  }, {
    title: '访客类型',
    dataIndex: 'visitorTypeName',
  }, {
    title: '流程类型',
    customRender: ({ record }:{record:VisitorProcess}) => (record.processType || record.processType === 0 ? options.value.VISIT_SOURCE_TYPE?.[record.processType]?.label : ''),
  },
  // {
  //   title: '审批人',
  //   dataIndex: 'auditPersonName',
  // },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
  }, {
    title: '状态',
    dataIndex: 'enable',
  }, {
    title: '操作',
    dataIndex: 'operate',
    fixed: 'right',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增访客流程',
  edit: '编辑访客流程',
  del: '删除',
};
