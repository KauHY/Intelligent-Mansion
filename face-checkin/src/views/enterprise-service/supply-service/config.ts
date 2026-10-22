/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-03-29 13:07:17
 */

import { Ref } from 'vue';
import { supplyTable } from '~models/supply-service';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';
// ['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE','ENTERPRISE_SIZE']
export const getColumns = (options:Ref<DictionaryMap<['SUPPLY_TYPE', 'ADD_DATA_SOURCE', 'SUPPLY_PUBLISH_STATUS']>>) => [

  // export const columns = [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '企业名称',
    dataIndex: 'companyName',
    slots: { customRender: 'companyName' },
  }, {
    title: '服务名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },
  }, {
    title: '供应类型',
    dataIndex: 'supplyType',
    slots: { customRender: 'supplyType' },
    customRender: ({ record }:{record:supplyTable}) => (record.supplyType || record.supplyType === 0 ? options.value.SUPPLY_TYPE?.[record.supplyType]?.label : ''),
  }, {
    title: '发布时间',
    dataIndex: 'publishTime',
  }, {
    title: '来源',
    dataIndex: 'source',
    slots: { customRender: 'source' },
    customRender: ({ record }:{record:supplyTable}) => (record.source || record.source === 0 ? options.value.ADD_DATA_SOURCE?.[record.source]?.label : ''),
  }, {
    title: '状态',
    dataIndex: 'state',
    slots: { customRender: 'state' },
    customRender: ({ record }:{record:supplyTable}) => (record.state || record.state === 0 ? options.value.SUPPLY_PUBLISH_STATUS?.[record.state]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '完善信息',
  edit: '编辑',
  del: '删除',
  shelves: '下架',
};
