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
    title: '出入库单号',
    dataIndex: 'recordNo',
  },
  {
    title: '物品编号',
    dataIndex: 'goodsId',
  },
  {
    title: '物品名称',
    dataIndex: 'goodsName',
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
    title: '单位',
    dataIndex: 'unit',
  },
  {
    title: '入库数量',
    dataIndex: 'stockInNum',
  },
  {
    title: '入库单价(元)',
    dataIndex: 'stockInUnitPrice',
  },
  {
    title: '操作后库存',
    dataIndex: 'currentNum',
  },
  {
    title: '操作后均价(元)',
    dataIndex: 'currentAveragePrice',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '出入库方式',
    dataIndex: 'stockType',
    customRender: ({ record }:{record:any}) => (record.stockType || record.stockType === 0 ? stockTypeList[record.stockType] : ''),
  },
  {
    title: '操作人',
    dataIndex: 'createByName',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
];
