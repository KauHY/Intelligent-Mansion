/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-21 10:37:14
 */

import { Ref } from 'vue';
import { companyGroupTable } from '~models/company-group';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';
// ['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE','ENTERPRISE_SIZE']
export const getColumns = (options:Ref<DictionaryMap<['COMPANY_TYPE', 'INDUSTRY_FIELD', 'ENTERPRISE_SIZE']>>) => [

  // export const columns = [
  {
    title: '所属群组',
    dataIndex: 'groupName',
    slots: { customRender: 'groupName' },
  }, {
    title: '企业名称',
    dataIndex: 'companyName',
    slots: { customRender: 'companyName' },
  }, {
    title: '企业类型',
    dataIndex: 'companyType',
    customRender: ({ record }:{record:companyGroupTable}) => (record.companyType || record.companyType === 0 ? options.value.COMPANY_TYPE?.[record.companyType]?.label : ''),
  }, {
    title: '行业领域',
    dataIndex: 'industryField',
    customRender: ({ record }:{record:companyGroupTable}) => (record.industryField || record.industryField === 0 ? options.value.INDUSTRY_FIELD?.[record.industryField]?.label : ''),
  }, {
    title: '企业规模',
    dataIndex: 'enterpriseSize',
    customRender: ({ record }:{record:companyGroupTable}) => (record.enterpriseSize || record.enterpriseSize === 0 ? options.value.ENTERPRISE_SIZE?.[record.enterpriseSize]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增',
  edit: '编辑',
  del: '删除',
};
