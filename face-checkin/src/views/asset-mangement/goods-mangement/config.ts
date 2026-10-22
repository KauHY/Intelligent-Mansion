/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-30 14:17:36
 */

import { GoodsCard } from '~models/asset-mangement';

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const orgTypeNames = {
  0: '机构',
  1: '园区',
};

export const operateDialogTitleName = {
  add: '新增物品',
  edit: '编辑物品',
  details: '物品详情',
};

export const goodsTypeList = [
  { label: '维修耗材', value: 1 },
  { label: '办公用品', value: 2 },
  { label: '清洁用品', value: 3 },
  { label: '工具用品', value: 4 },
  { label: '电器设备', value: 5 },
  { label: '其他', value: 6 },
];

export const getColumns = () => [
  {
    title: '物品编码',
    dataIndex: 'goodsNo',
  },
  {
    title: '物品名称',
    dataIndex: 'goodsName',
    slots: { customRender: 'goodsName' },
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
    // slots: { customRender: 'pointName' },
  },
  {
    title: '物品类别',
    dataIndex: 'goodsType',
    customRender: ({ record }:{record:GoodsCard}) => (record.goodsType ? goodsTypeList[record.goodsType - 1].label : '-'),
  }, {
    title: '单位',
    dataIndex: 'unit',
  }, {
    title: '物品单价(元)',
    dataIndex: 'unitPrice',
  }, {
    title: '更新时间',
    dataIndex: 'updateTime',
  }, {
    title: '库存下限预警值',
    dataIndex: 'minStock',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 250,
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
