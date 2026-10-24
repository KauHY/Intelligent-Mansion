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

export const goodsTypeList = [
  { label: '维修耗材', value: 1 },
  { label: '办公用品', value: 2 },
  { label: '清洁用品', value: 3 },
  { label: '工具用品', value: 4 },
  { label: '电器设备', value: 5 },
  { label: '其他', value: 6 },
];

export const stockTypeList = ['', '手动入库', '归还入库', '导入入库', '手动出库', '领用出库', '修改库存'];

export const getColumns = () => [
  {
    title: '盘点月份',
    dataIndex: 'month',
  },
  {
    title: '物品名称',
    dataIndex: 'goodsName',
    slots: { customRender: 'goodsName' },
  },
  {
    title: '物品类别',
    dataIndex: 'goodsType',
    customRender: ({ record }:{record:GoodsCard}) => (record.goodsType ? goodsTypeList[record.goodsType - 1]?.label : '-'),
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '所属仓库',
    dataIndex: 'employeeName',
    customRender: () => '公寓仓库',
  },
  {
    title: '上月结存数',
    dataIndex: 'lastBalance',
  },
  {
    title: '上月结存总额(元)',
    dataIndex: 'lastBalanceTotal',
  },
  {
    title: '本月库存数',
    dataIndex: 'currentBalance',
  },
  {
    title: '本月入库数',
    dataIndex: 'currentStockInNumber',
  },
  {
    title: '本月库存总额(元)',
    dataIndex: 'currentBalanceTotal',
  },
  {
    title: '盘点时间',
    dataIndex: 'inventoryTime',
  },
];
