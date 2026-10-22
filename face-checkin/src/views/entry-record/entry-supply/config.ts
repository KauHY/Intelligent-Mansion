/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-03-02 16:13:59
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import CustomerVo, { AuditParams } from '~models/company';
import { DictionaryMap } from '~composition/useHashMapDic';

export const statusOpt = [
  {
    label: '禁用',
    value: '0',
  }, {
    label: '启用',
    value: '1',
  },
];
export const getCustomerColumns = (options: DictionaryMap<['SYS_COMPANY_AUDIT_STATUS' ]>) => [
  {
    title: '企业名称',
    dataIndex: 'companyInformationName',
  },
  {
    title: '入驻园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '联系人',
    dataIndex: 'contactPerson',

  },
  {
    title: '联系人电话',
    dataIndex: 'contactPhone',
  },
  {
    title: '状态',
    customRender: ({ record }: { record: AuditParams }) => options.SYS_COMPANY_AUDIT_STATUS?.[record.auditStatus as number]?.label || '-',
  },
  {
    title: '申请时间',
    dataIndex: 'applyTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 250,
    slots: { customRender: 'operate' },
  },
];

export const getCustomerTagColumns = () => [
  {
    title: '标签名称',
    dataIndex: 'name',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];
