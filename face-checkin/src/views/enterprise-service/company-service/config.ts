/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-03-30 11:36:16
 */

import { Ref } from 'vue';
import { companyTable } from '~models/company-service';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';
// ['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE','ENTERPRISE_SIZE']
export const getColumns = (options:Ref<DictionaryMap<['SERVICE_GROUPING', 'SUPPLY_PUBLISH_STATUS']>>) => [

  // export const columns = [
  {
    title: '服务名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },
  }, {
    title: '服务分组',
    dataIndex: 'groupType',
    slots: { customRender: 'groupType' },
    customRender: ({ record }:{record:companyTable}) => (record.groupType || record.groupType === 0 ? options.value.SERVICE_GROUPING?.[record.groupType]?.label : ''),
  }, {
    title: '推送范围',
    dataIndex: 'pushRangeName',
  }, {
    title: '更新时间',
    dataIndex: 'updateTime',
    slots: { customRender: 'updateTime' },
  }, {
    title: '状态',
    dataIndex: 'state',
    slots: { customRender: 'state' },
    customRender: ({ record }:{record:companyTable}) => (record.state || record.state === 0 ? options.value.SUPPLY_PUBLISH_STATUS?.[record.state]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '发布',
  edit: '编辑',
  del: '删除',
  blockUp: '停用',
};
