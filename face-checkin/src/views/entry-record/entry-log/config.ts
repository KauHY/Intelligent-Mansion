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

export const getCustomerColumns = (options: DictionaryMap<['ADD_DATA_SOURCE' ]>) => [
  {
    title: '企业名称',
    dataIndex: 'name',
  },
  {
    title: '入驻园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '联系人',
    dataIndex: 'contacts',

  },
  {
    title: '联系人电话',
    dataIndex: 'tel',
  },

  {
    title: '地址',
    dataIndex: 'address',

  },
  {
    title: '来源',
    dataIndex: 'source',
    customRender: ({ record }: { record: AuditParams }) => options.ADD_DATA_SOURCE?.[record.source as number]?.label,
  },
  {
    title: '成功入驻时间',
    dataIndex: 'createTime',
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
