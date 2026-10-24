/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 11:21:24
 */

import { Ref } from 'vue';
import { BillingScheme } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';
// import useForm from '~composition/useForm';
export const statusNames = {
  0: '关闭',
  1: '开启',
};
export const operateDialogTitleName = {
  add: '新增计费方案',
  edit: '编辑计费方案',
  details: '计费方案详情',
  // statusChange: '',
};

export const getColumns = (options:DictionaryMap<['FEE_TYPE', 'FEE_BILLING_CYCLE', 'FEE_CHARGING_CYCLE']>) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '方案名称',
    dataIndex: 'schemeName',
    slots: { customRender: 'schemeName' },
  },
  {
    title: '收费项',
    dataIndex: ['chargeItemDTO', 'chargeName'],
    // slots: { customRender: 'pointName' },
    // customRender: ({ record }:{record:BillingScheme}) => (record.feeType || record.feeType === 0 ? options.value.FEE_TYPE?.[record.feeType]?.label : ''),

  }, {
    title: '计费周期',
    dataIndex: 'billingCycle',
    customRender: ({ record }:{record:BillingScheme}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
  },
  {
    title: '收费周期',
    dataIndex: 'chargingCycle',
    customRender: ({ record }:{record:BillingScheme}) => (record.chargingCycle || record.chargingCycle === 0 ? options.FEE_CHARGING_CYCLE?.[record.chargingCycle]?.label : ''),
  },
  {
    title: '更新日期',
    dataIndex: 'updateTime',
  }, {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    // dataIndex: 'remark',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 160,
    slots: { customRender: 'operate' },
  }];

export const houseColumns = [
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
  },
  {
    title: '楼层房号',
    dataIndex: 'name',
  },
];
