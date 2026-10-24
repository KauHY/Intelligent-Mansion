/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-28 14:56:12
 */

import { TagInfo } from '~models/company-mangement';

export const shiftOutOpts = [
  {
    label: '立即签出',
    value: '1',
  },
  {
    label: '自定义签出时间',
    value: '2',
  },
];

export const columns = [
  {
    title: '序号',
    // dataIndex: 'tagName',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '访客类型',
    dataIndex: 'visitorTypeName',
  }, {
    title: '访客姓名',
    dataIndex: 'visitorName',
  }, {
    title: '访客手机号码',
    dataIndex: 'visitorTel',
  }, {
    title: '登记时间',
    dataIndex: 'signInTime',
  }, {
    title: '登记类型',
    dataIndex: 'signInTypeLabel',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  },
];

export const operateDialogTitleName = {
  shiftOut: '签出时间设置',
  details: '详情',
};
