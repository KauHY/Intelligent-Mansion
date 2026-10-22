/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-01 17:29:35
 */

import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '企业名称',
    dataIndex: 'name',
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '连接类型',
    dataIndex: 'visitorClientTypeLabel',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: 'APPID|地址',
    dataIndex: 'visitorClientValue',
  }, {
    title: '创建时间',
    dataIndex: 'visitorClientTime',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增企业信息',
  edit: '编辑企业信息',
  del: '删除',
};
