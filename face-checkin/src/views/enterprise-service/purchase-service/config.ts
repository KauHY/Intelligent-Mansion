/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-01 14:55:23
 */

import { Ref } from 'vue';
import { purchaseTable } from '~models/purchase-service';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';
// ['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE','ENTERPRISE_SIZE']
export const getColumns = (options:Ref<DictionaryMap<['PURCHASE_TYPE', 'ADD_DATA_SOURCE', 'PURCHASE_STATE']>>) => [

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
    title: '采购名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },
  }, {
    title: '采购类型',
    dataIndex: 'purchaseType',
    slots: { customRender: 'purchaseType' },
    customRender: ({ record }:{record:purchaseTable}) => (record.purchaseType || record.purchaseType === 0 ? options.value.PURCHASE_TYPE?.[record.purchaseType]?.label : ''),
  }, {
    title: '创建日期',
    dataIndex: 'createTime',
  }, {
    title: '截止日期',
    dataIndex: 'effectiveEndTime',
  }, {
    title: '来源',
    dataIndex: 'source',
    slots: { customRender: 'source' },
    customRender: ({ record }:{record:purchaseTable}) => (record.source || record.source === 0 ? options.value.ADD_DATA_SOURCE?.[record.source]?.label : ''),
  }, {
    title: '状态',
    dataIndex: 'state',
    slots: { customRender: 'state' },
    customRender: ({ record }:{record:purchaseTable}) => (record.state || record.state === 0 ? options.value.PURCHASE_STATE?.[record.state]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 320,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  release: '发布',
  edit: '编辑',
  del: '删除',
  shelves: '下架',
  bid: '投标详情',
};
