/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-01 14:36:10
 */

import { Ref } from 'vue';
import { enterpriseTable } from '~models/enterprise-service';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';
// ['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE','ENTERPRISE_SIZE']
export const getColumns = (options:Ref<DictionaryMap<['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'ENTERPRISE_SIZE']>>) => [

  // export const columns = [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '企业名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },
  }, {
    title: '企业类型',
    dataIndex: 'companyType',
    slots: { customRender: 'companyType' },
    customRender: ({ record }:{record:enterpriseTable}) => (record.companyType || record.companyType === 0 ? options.value.COMPANY_TYPE?.[record.companyType]?.label : ''),
    // 0-中型企业；1-小型企业；2-微型企业；3-中小微型企业
  }, {
    title: '企业领域',
    dataIndex: 'industryField',
    slots: { customRender: 'industryField' },
    customRender: ({ record }:{record:enterpriseTable}) => (record.industryField || record.industryField === 0 ? options.value.INDUSTRY_FIELD?.[record.industryField]?.label : ''),
  }, {
    title: '企业纳税类型',
    dataIndex: 'companyTaxesType',
    slots: { customRender: 'companyTaxesType' },
    customRender: ({ record }:{record:enterpriseTable}) => (record.companyTaxesType || record.companyTaxesType === 0 ? options.value.COMPANY_TAXES_TYPE?.[record.companyTaxesType]?.label : ''),
  }, {
    title: '企业规模',
    dataIndex: 'enterpriseSize',
    slots: { customRender: 'enterpriseSize' },
    customRender: ({ record }:{record:enterpriseTable}) => (record.enterpriseSize || record.enterpriseSize === 0 ? options.value.ENTERPRISE_SIZE?.[record.enterpriseSize]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '完善信息',
};
