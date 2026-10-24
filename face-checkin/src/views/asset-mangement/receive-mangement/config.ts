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
export const operateDialogTitleName = {
  add: '新增物品',
  edit: '编辑物品',
  details: '物品详情',
};

export const getColumns = () => [
  {
    title: '申请单号',
    dataIndex: 'applyNo',
    slots: { customRender: 'applyNo' },
  },
  {
    title: '领用人',
    dataIndex: 'employeeName',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '物品名称',
    dataIndex: 'goodsName',
  },
  {
    title: '领用数量',
    dataIndex: 'receiveNum',
  },
  {
    title: '是否需归还',
    dataIndex: 'needReturn',
    customRender: ({ record }: { record: any }) => (record.needReturn >= 0 ? NeedReturnType[record.needReturn] : '-'),
  },
  {
    title: '已归还数量',
    dataIndex: 'returnNumTotal',
  },
  {
    title: '申请时间',
    dataIndex: 'applyTime',
  },
  {
    title: '审批状态',
    dataIndex: 'auditState',
    customRender: ({ record }: { record: any }) => ((record.auditState || 0) >= 1 ? AuditStateType[record.auditState] : '-'),
  },
  {
    title: '出入库状态',
    dataIndex: 'stockState',
    customRender: ({ record }: { record: any }) => ((record.stockState || 0) >= 1 ? StockStateType[record.stockState] : '-'),
  },
  {
    title: '出库时间',
    dataIndex: 'stockOutTime',
  },
  {
    title: '入库时间',
    dataIndex: 'stockInTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 150,
    slots: { customRender: 'operate' },
  },
];

export const getGoodsListColumns = [
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
    title: '单位',
    dataIndex: 'unit',
  },
  {
    title: '入库单价(元)',
    dataIndex: 'unitPrice',
  },
];

export const AuditStateType = ['', '领用待审核', '领用审核通过', '领用审核不通过', '归还待审核', '归还审核通过', '归还审核不通过', '已取消',
];

export const StockStateType = ['', '未出库', '已出库', '部分已入库', '全部已入库'];

export const testList = [
  {
    applyNumber: '领用人',
    applyTime: '2020-12-01 00:00:00',
    auditState: null,
    employeeId: '124123',
    employeeName: '领用人名称',
    goodsName: '物品名称',
    id: '1234213',
    needReturn: 0,
    parkOrgId: '12341234',
    parkOrgName: '园区1234',
    receiveNum: '1234',
    returnNumTotal: 12341,
    stockInTime: '2020-20-02',
    stockOutTime: '20-652-36',
    stockState: 4,
  },
];

export const receiveDetailList = [
  {
    auditMsg: '',
    auditRemark: '备注',
    auditState: null,
    auditTime: '20-520-12',
    employeeId: '1234',
    employeeName: '领用人名称',
    needReturn: null,
    stockNo: '154却64犬瘟热',
  },
];

export const testReceiveDetailList = {
  applyNumber: '领用人',
  applyTime: '2020-12-01 00:00:00',
  auditState: null,
  employeeId: '124123',
  employeeName: '领用人名称',
  goodsName: '物品名称',
  id: '1234213',
  needReturn: 0,
  parkOrgId: '12341234',
  parkOrgName: '园区1234',
  receiveNum: '1234',
  returnNumTotal: 12341,
  stockInTime: '2020-20-02',
  stockOutTime: '20-652-36',
  stockState: 4,
  receiveDetailList,
};

// TODO 详情页文案跟AuditStateType不一样，但是state是一致的
export const AuditStateShowType = [
  '',
  '提交申请',
  '领用审核通过',
  '领用审核不通过',
  '提交归还申请',
  '归还审核通过',
  '归还审核不通过',
  '取消申请',
];
