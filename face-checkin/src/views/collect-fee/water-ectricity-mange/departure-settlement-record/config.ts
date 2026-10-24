/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-27 16:09:41
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-21 14:50:21
 */
import { RentFeeInfo } from '~models/collect-fee';

export const getCustomerColumns = () => [

  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
    slots: { customRender: 'companyName' },
  },
  {
    title: '计费类型',
    dataIndex: 'type',
    customRender: () => '离场结算',
  },
  {
    title: '实收总金额（元）',
    dataIndex: 'totalAmount',
  },

  {
    title: '提交时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作人',
    dataIndex: 'createByName',
  },
];

export const getDetailColumns = () => [

  {
    title: '收费项',
    dataIndex: 'chargeName',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeInfo}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '上月底数',
    dataIndex: 'lastReading',
  },
  {
    title: '当前读数',
    dataIndex: 'thisReading',
    width: 155,
    slots: { customRender: 'thisReading' },
  },
  {
    title: '本月用量',
    dataIndex: 'roomAmount',
  },
  {
    title: '本月公共用量',
    dataIndex: 'publicAmount',
  },
  {
    title: '本月应摊用量',
    dataIndex: 'shareAmount',
  },
  {
    title: '待收金额（元）',
    dataIndex: 'amountCount',
  },
];
