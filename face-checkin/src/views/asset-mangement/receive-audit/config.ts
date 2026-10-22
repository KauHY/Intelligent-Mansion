/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-30 14:17:36
 */

import { Ref } from 'vue';
import { GoodsCard, NeedReturnType, ReceiveDetails } from '~models/asset-mangement';
import { DictionaryMap } from '~composition/useHashMapDic';
// import useForm from '~composition/useForm';
import useForm from '~utils/useForm';

export const stockTitleName = {
  details: '详情',
  auditInStock: '入库审核',
  auditOutStock: '出库审核',
};

export const getColumns = () => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '授权公司',
    dataIndex: 'companyName',
  },
  {
    title: '授权员工',
    dataIndex: 'employeeName',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 250,
    slots: { customRender: 'operate' },
  },
];
