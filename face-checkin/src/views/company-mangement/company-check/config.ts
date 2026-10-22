/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-03-28 15:24:24
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import CompanyMangementVo, { CompanyCheckInfo } from '~models/company-mangement';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getCompanyCheckColumns = (options:DictionaryMap<['SYS_COMPANY_AUDIT_STATUS']>) => [
  {
    title: '企业名称',
    dataIndex: 'companyInformationName',
    slots: { customRender: 'companyInformationName' },
  },
  {
    title: '申请入驻园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '企业端用户名',
    dataIndex: 'businessUserName',
  },
  {
    title: '申请时间',
    dataIndex: 'createTime',
  },
  {
    title: '状态',
    dataIndex: 'auditStatus',
    customRender: ({ record }:{record:CompanyCheckInfo}) => options.SYS_COMPANY_AUDIT_STATUS?.[record.auditStatus as number]?.label,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  },
];
