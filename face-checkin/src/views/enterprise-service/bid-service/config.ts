/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-03-30 17:06:31
 */

import { Ref } from 'vue';
import { bidTable } from '~models/bid-service';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';
// ['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE','ENTERPRISE_SIZE']
export const getColumns = (options:Ref<DictionaryMap<['PAYMENT', 'CONTRIBUTE']>>) => [

  // export const columns = [

  {
    title: '企业名称',
    dataIndex: 'companyName',
    slots: { customRender: 'companyName' },
  }, {
    title: '联系人',
    dataIndex: 'contact',
    slots: { customRender: 'contact' },
  }, {
    title: '联系电话',
    dataIndex: 'tel',
  }, {
    title: '邮箱',
    dataIndex: 'mail',
  }, {
    title: '备注',
    dataIndex: 'remark',
    slots: { customRender: 'remark' },
  }, {
    title: '支付招标文件费',
    dataIndex: 'biddingDocumentsPaid',
    slots: { customRender: 'biddingDocumentsPaid' },
    customRender: ({ record }:{record:bidTable}) => (record.biddingDocumentsPaid || record.biddingDocumentsPaid === 0 ? options.value.PAYMENT?.[record.biddingDocumentsPaid]?.label : ''),
  }, {
    title: '保证金缴纳情况',
    dataIndex: 'marginPaid',
    slots: { customRender: 'marginPaid' },
    customRender: ({ record }:{record:bidTable}) => (record.marginPaid || record.marginPaid === 0 ? options.value.CONTRIBUTE?.[record.marginPaid]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  confirm: '确认',
};
