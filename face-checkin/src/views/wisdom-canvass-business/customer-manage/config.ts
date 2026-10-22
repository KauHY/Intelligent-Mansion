/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-03-02 16:13:59
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import CustomerVo, { CustomerInfoKind } from '~models/customer-manage';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getCustomerColumns = (options: DictionaryMap<['COMPANY_TYPE', 'CUSTOMERINFO_STATUS']>) => [
  {
    title: '客户编码',
    dataIndex: 'customerCode',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '客户名称',
    dataIndex: 'name',
    slots: { customRender: 'customerName' },
  },
  {
    title: '客户标签',
    dataIndex: 'tagName',
  },
  {
    title: '客户类型',
    dataIndex: 'type',
    customRender: ({ record }: { record: CustomerInfoKind }) => options.COMPANY_TYPE?.[record.type as number]?.label,
  },
  {
    title: '客户状态',
    dataIndex: 'status',
    customRender: ({ record }: { record: CustomerInfoKind }) => options.CUSTOMERINFO_STATUS?.[record.status as number]?.label,
  },
  {
    title: '联系人',
    dataIndex: 'contacts',
  },
  {
    title: '联系电话',
    dataIndex: 'tel',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 200,
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
