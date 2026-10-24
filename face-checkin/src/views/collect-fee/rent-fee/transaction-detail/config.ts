/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-28 16:45:54
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>
import { RentFeeTransactionDetail } from '~models/collect-fee';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getColumns = (options:DictionaryMap<['FEE_TYPE', 'RENT_FEE_PAY_TYPE', 'RENT_TRADE_TYPE']>, contractType: any) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
    width: 150,
    fixed: 'left',
  },
  {
    title: '订单编号',
    dataIndex: 'tradeNo',
    width: 150,
    fixed: 'left',
    slots: { customRender: 'tradeNo' },
  },
  {
    title: '账单编号',
    dataIndex: 'billNo',
  },
  {
    title: '入驻企业名称',
    dataIndex: 'companyName',
  },

  {
    title: '支付方式',
    dataIndex: 'payType',
    slots: { customRender: 'payType' },
    // customRender: ({ record }:{record:RentFeeTransactionDetail}) => (record.billingCycle || record.billingCycle === 0 ? options.FEE_BILLING_CYCLE?.[record.billingCycle]?.label : ''),
  },
  {
    title: '订单类型',
    dataIndex: 'type',
    customRender: ({ record }:{record:RentFeeTransactionDetail}) => (record.type || record.type === 0 ? options.RENT_TRADE_TYPE?.[record.type]?.label : ''),
  },

  {
    title: '账单应收金额（元）',
    dataIndex: 'receivableAmount',
  },
  {
    title: '实付金额（元）',
    dataIndex: 'receivedAmount',
  },

  {
    title: '支付时间',
    dataIndex: 'createTime',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    hidden: () => contractType === 6 || contractType === 7,
    customRender: ({ record }:{record:RentFeeTransactionDetail}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '所在账期',
    dataIndex: 'accountPeriod',
  },
  {
    title: '所含收费项',
    dataIndex: 'chargeName',
    ellipsis: false,
  },
];

// 交易明细
export const transactionDetailColumns = [
  {
    title: '所含收费项',
    dataIndex: 'chargeName',
  },
  {
    title: '房源名称',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeTransactionDetail}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '所在账期',
    dataIndex: 'accountPeriod',
  },
  {
    title: '账单应收金额（元）',
    dataIndex: 'receivableAmount',
  },
  {
    title: '实付金额（元）',
    dataIndex: 'receivedAmount',
  },
];

// 退款记录
export const returnMoneyRecordsColumns = [
  {
    title: '退款单号',
    dataIndex: 'extraTradeNo',
    slots: { customRender: 'extraTradeNo' },
  },
  {
    title: '申请人',
    dataIndex: 'applicantName',
  },
  {
    title: '退款时间',
    dataIndex: 'returnTime',
  },
  {
    title: '退款金额（元）',
    dataIndex: 'returnAmount',
  },
];

export const returnMoneyContentsColumns = [
  {
    title: '所属账期',
    dataIndex: 'accountPeriod',
  },
  {
    title: '费用项',
    dataIndex: 'chargeName',
  },
  {
    title: '楼宇房源',
    dataIndex: 'houseName',
    customRender: ({ record }:{record:RentFeeTransactionDetail}) => ((record.houseName ? record.houseName?.join(';') : '-')),
  },
  {
    title: '退款金额（元）',
    dataIndex: 'returnAmount',
  },
];

export const operateDialogTitleName = {
  add: '新增表单',
  edit: '编辑表单',
  del: '删除',
  details: '表单详情',
};
