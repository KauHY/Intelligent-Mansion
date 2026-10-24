/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: your name
 * @LastEditTime: 2022-03-14 17:25:47
 */

import { Ref } from 'vue';
import { ChargeItem } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';
// import useForm from '~composition/useForm';
export const statusNames = {
  0: '关闭',
  1: '开启',
};

export const operateDialogTitleName = {
  add: '新增收费项',
  edit: '编辑收费项',
  details: '收费项详情',
  // statusChange: '',
};

export const getColumns = (options:DictionaryMap<['FEE_TYPE', 'GROUP_FEE_TYPE', 'BENEFICIARY']>) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '费用项名称',
    dataIndex: 'chargeName',
    slots: { customRender: 'chargeName' },
  },
  {
    title: '费用类型',
    dataIndex: 'pointName',
    // slots: { customRender: 'pointName' },
    customRender: ({ record }:{record:ChargeItem}) => (record.feeType || record.feeType === 0 ? options.FEE_TYPE?.[record.feeType]?.label : ''),

  }, {
    title: '税率',
    dataIndex: 'taxRate',
    customRender: ({ record }:{record:ChargeItem}) => `${record.taxRate}%`,
  }, {
    title: '收款方',
    // dataIndex: 'payeeId',
    // slots: { customRender: 'payeeId' },
    customRender: ({ record }:{record:ChargeItem}) => (record.payeeId || record.payeeId === 0 ? options.BENEFICIARY?.[record.payeeId]?.label : ''),

  }, {
    title: '备注',
    dataIndex: 'remark',
  }, {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
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
