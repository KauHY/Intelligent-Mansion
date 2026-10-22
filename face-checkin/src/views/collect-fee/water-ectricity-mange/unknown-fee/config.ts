/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 13:47:19
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { RentFeeInfo } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getDetailColumns = [

  {
    title: '收费项',
    dataIndex: 'chargeName',
    slots: { customRender: 'chargeName' },
  },
  // {
  //   title: '房源名称',
  //   dataIndex: 'houseName',
  // },
  {
    title: '金额（元）',
    dataIndex: 'amountCount',
  },
  // {
  //   title: '本月示数',
  //   dataIndex: 'thisReading',
  // },
  // {
  //   title: '上月底数',
  //   dataIndex: 'lastReading',
  // },
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
    title: '本月实际用量',
    dataIndex: 'totalAmount',
  },
];
export const columns = [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '收费项',
    dataIndex: 'chargeName',
    slots: { customRender: 'chargeName' },
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  }, {
    title: '结算月份',
    dataIndex: 'settledMonth',
  },
  {
    title: '应收日期',
    dataIndex: 'dueDate',
  },
  {
    title: '最晚缴纳日期',
    dataIndex: 'latestPayDate',
  },
  {
    title: '本期结算时间',
    dataIndex: 'settledTime',
  },
  {
    title: '应收金额（元）',
    dataIndex: 'receivableAmount',
  },
  {
    title: '数据来源',
    dataIndex: 'dataSource',
    slots: { customRender: 'dataSource' },
  },
];

export const operateDialogTitleName = {
  add: '新增',
  edit: '批量修改收款日期',
  del: '删除',
  details: '详情',
};
