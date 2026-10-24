/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-20 16:20:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-06 14:57:27
 */

import URLS from '~models/urlConstant/collect-fee';
import request from '~utils/axios';

import {
  RentFeeInfo,
  RentFeeSearchParams,
  RentFeeBillInfo,
  RentFeeBillDetail,
  BeforeCheckoutRentFee,
  RentFeeTransactionDetailSearchParams,
  RentFeeTransactionDetail,
  RentFeeReturnMoney,
  ReturnRentDepositFee,
  RentLateFeeInfo,
  RentLateFeeSearchParams,
  UnknownRentFeeModifyDate,
  RentFeeInfoDetails,
} from './type';

export class RentFee { // 未出租赁费用分页
  async rentFeeListPage<T>(searchData:Partial<RentFeeSearchParams>) {
    const { data } = await request<{list:RentFeeInfo[], total:number}>({
      url: URLS.rentFeeListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 费用批量转为已出
  async rentFeeTransform(ids:string[]) {
    await request({
      url: URLS.rentFeeTransform,
      data: {
        ids,
      },
    });
  }

  // 批量修改日期
  async rentFeeModifyDate(data:UnknownRentFeeModifyDate) {
    await request({
      url: URLS.rentFeeModifyDate,
      data,
    });
  }

  // 租赁未出/已出/押金导出
  async rentFeeExport(searchData:RentLateFeeSearchParams) {
    const { data, headers } = await request({
      url: URLS.rentFeeExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 租赁已出/未出/押金费用费根据id查询详情
  async rentFeeFindById(feeId:string) {
    const { data } = await request({
      url: `${URLS.rentFeeFindById}${feeId}`,
      method: 'get',
    });
    const {
      rentContractFeeDTO,
      deductionRecordList,
    } = data;
    const {
      id,
      refundedAmount,
      userName,
      handleTime,
    } = data.rentContractFeeDTO;
    return {
      ...rentContractFeeDTO,
      deductionRecordList,
      depositFeeRentrnRecord: [{
        id,
        refundedAmount,
        userName,
        handleTime,
      }],
    } as RentFeeInfoDetails;
  }

  // ---------------- 账单-------------------------------
  async rentFeeBillListPage<T>(searchData:RentFeeSearchParams) {
    const { data } = await request<{list:RentFeeBillInfo[], total:number}>({
      url: URLS.rentFeeBillListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // --------------- 账单编辑 ---------------------
  async rentFeeBillModify(data:{id:string, accountPeriod:string}) {
    await request({
      url: URLS.rentFeeBillModify,
      data,
    });
  }

  // 收款|批量收款
  async rentFeeBillCheckout(data:{ids:string[], checkoutDate:string, remark:string, payType:number}) {
    await request({
      url: URLS.rentFeeBillCheckout,
      data,
    });
  }

  // 租赁费用账单减免
  async rentFeeBillDeduction(data:{id:string, deductionAmount:number|null, remark:string, file:string}) {
    await request({
      url: URLS.rentFeeBillDeduction,
      data,
    });
  }

  // 租赁费用账单明细
  async rentFeeBillDetails(id:string) {
    const { data } = await request<RentFeeBillDetail>({
      url: `${URLS.rentFeeBillDetails}${id}`,
      method: 'get',
    });
    return data;
  }

  // 收款前金额合计
  async beforeCheckoutRentFee(id:string) {
    const { data } = await request<BeforeCheckoutRentFee>({
      url: URLS.beforeCheckoutRentFee,
      method: 'get',
      data: {
        id,
      },
    });
    return data;
  }

  // 批量收款前金额合计
  async beforeBatchCheckoutRentFee(ids:string[]) {
    console.log('ids------', ids);
    const { data } = await request<BeforeCheckoutRentFee>({
      url: URLS.beforeBatchCheckoutRentFee,
      method: 'get',
      data: {
        ids,
      },
    });
    return data;
  }

  // 租赁未出/已出/押金导出
  async rentFeeBillExport(searchData:RentFeeSearchParams) {
    const { data, headers } = await request({
      url: URLS.rentFeeBillExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // ------------ 交易明细----------------------

  // 交易明细分页
  async rentFeeTransactionDetailListPage<T>(searchData:RentFeeTransactionDetailSearchParams) {
    const { data } = await request<{list:RentFeeTransactionDetail[], total:number}>({
      url: URLS.rentFeeTransactionDetailListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 交易明细根据id查询详情
  async rentFeeTransactionDetailFindById(id:string) {
    const { data } = await request<RentFeeTransactionDetail>({
      url: `${URLS.rentFeeTransactionDetailFindById}${id}`,
      method: 'get',
    });
    // const transactionDetailList:RentFeeTransactionDetail['transactionDetailList'] = ['chargeName','houseName','accountPeriod',]
    const {
      tradeNo,
      chargeName, // 所含收费项名称
      houseName, // 房源名称
      accountPeriod, // 所在账期
      receivableAmount, // 应收金额
      receivedAmount, // 实付金额

      extraTradeId,
      extraTradeNo, // 退款记录单号
      applicantName, // 申请人
      returnTime, // 退款时间
      returnAmount, // 退款金额
    } = data;
    return {
      ...data,
      transactionDetailList: [{
        tradeNo,
        chargeName, // 所含收费项名称
        houseName, // 房源名称
        accountPeriod, // 所在账期
        receivableAmount, // 应收金额
        receivedAmount, // 实付金额
      }],
      returnMoneyRecords: [
        {
          extraTradeId,
          extraTradeNo, // 退款记录单号
          applicantName, // 申请人
          returnTime, // 退款时间
          returnAmount, // 退款金额
        },
      ],
      returnMoneyContent: [{
        extraTradeId,
        accountPeriod, // 所在账期
        chargeName, // 所含收费项名称
        houseName,
        returnAmount,
      }],
    };
  }

  // 退款
  async rentFeeReturnMoney(data:RentFeeReturnMoney) {
    await request<BeforeCheckoutRentFee>({
      url: URLS.rentFeeReturnMoney,
      data,
    });
  }

  // 滞纳金导出
  async rentFeeTransactionDetailExport(searchData:RentFeeTransactionDetailSearchParams) {
    const { data, headers } = await request({
      url: URLS.rentFeeTransactionDetailExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }
  // ---------------------押金------------------

  // 押金退款
  async rentDepositFeeReturnMoney(data:ReturnRentDepositFee) {
    await request<BeforeCheckoutRentFee>({
      url: URLS.rentDepositFeeReturnMoney,
      data,
    });
  }

  // -------------- 滞纳金---------------------------
  // 滞纳金分页
  async rentLateFeeListPage<T>(searchData:RentLateFeeSearchParams) {
    const { data } = await request<{list:RentLateFeeInfo[], total:number}>({
      url: URLS.rentLateFeeListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 滞纳金免除
  async rentLateFeePrevent(ids:string[]) {
    await request<BeforeCheckoutRentFee>({
      url: URLS.rentLateFeePrevent,
      data: { ids },
    });
  }

  // 滞纳金导出
  async rentLateFeeExport(searchData:RentLateFeeSearchParams) {
    const { data, headers } = await request({
      url: URLS.rentLateFeeExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // -------------- 水电费---------------------------
  // 分页
  async listMeterFeePage<T>(searchData:Partial<RentFeeSearchParams>) {
    const { data } = await request<{list:RentFeeInfo[], total:number}>({
      url: URLS.listMeterFeePage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 已出/未出根据id查询详情
  async findByIdFeePage(feeId:string) {
    const { data } = await request({
      url: `${URLS.findByIdFeePage}${feeId}`,
      method: 'get',
    });
    return data;
  }

  // 批量修改日期
  async modifyDateFee(data:UnknownRentFeeModifyDate) {
    await request({
      url: URLS.modifyDateFee,
      data,
    });
  }

  // 费用批量转为已出
  async transformFee(ids:string[]) {
    await request({
      url: URLS.transformFee,
      data: {
        ids,
      },
    });
  }

  // 导出
  async exportFeePage(searchData:RentLateFeeSearchParams) {
    const { data, headers } = await request({
      url: URLS.exportFeePage,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // -------------- 滞纳金---------------------------
  // 滞纳金分页
  async listMeterLateFeePage<T>(searchData:RentLateFeeSearchParams) {
    const { data } = await request<{list:RentLateFeeInfo[], total:number}>({
      url: URLS.listMeterLateFeePage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 滞纳金免除
  async preventLateFee(ids:string[]) {
    await request<BeforeCheckoutRentFee>({
      url: URLS.preventLateFee,
      data: { ids },
    });
  }

  // 滞纳金导出
  async exportMeterLateFee(searchData:RentLateFeeSearchParams) {
    const { data, headers } = await request({
      url: URLS.exportMeterLateFee,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // --------------- 费用 ---------------------
  async modifyConsumptionBill(data:{id:string, accountPeriod:string}) {
    await request({
      url: URLS.modifyConsumptionBill,
      data,
    });
  }

  // 收款|批量收款
  async checkout(data:{ids:string[], checkoutDate:string, remark:string, payType:number}) {
    await request({
      url: URLS.checkout,
      data,
    });
  }

  // 费用账单减免
  async deductionFee(data:{id:string, deductionAmount:number|null, remark:string, file:string}) {
    await request({
      url: URLS.deductionFee,
      data,
    });
  }

  // 导出
  async exportConsumptionBill(searchData:RentLateFeeSearchParams) {
    const { data, headers } = await request({
      url: URLS.exportConsumptionBill,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async listMeterConsumptionBillPage<T>(searchData:RentFeeSearchParams) {
    const { data } = await request<{list:RentFeeBillInfo[], total:number}>({
      url: URLS.listMeterConsumptionBillPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  async findLateFeeByBillIds(id:string) {
    const { data } = await request<RentFeeBillDetail>({
      url: `${URLS.findLateFeeByBillIds}?ids=${id}`,
      method: 'get',
    });
    return data;
  }

  async findLateFeeByBillId(id:string) {
    const { data } = await request<RentFeeBillDetail>({
      url: `${URLS.findLateFeeByBillId}?id=${id}`,
      method: 'get',
    });
    return data;
  }

  async findByIdConsumptionBill(id:string) {
    const { data } = await request<RentFeeBillDetail>({
      url: `${URLS.findByIdConsumptionBill}${id}`,
      method: 'get',
    });
    return data;
  }

  // --------------- 明细 ---------------------

  // 导出
  async exportConsumptionPage(searchData:RentLateFeeSearchParams) {
    const { data, headers } = await request({
      url: URLS.exportConsumptionPage,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async findByIdConsumptionPage(id:string) {
    const { data } = await request<RentFeeTransactionDetail>({
      url: `${URLS.findByIdConsumptionPage}${id}`,
      method: 'get',
    });
    // const transactionDetailList:RentFeeTransactionDetail['transactionDetailList'] = ['chargeName','houseName','accountPeriod',]
    const {
      tradeNo,
      chargeName, // 所含收费项名称
      houseName, // 房源名称
      accountPeriod, // 所在账期
      receivableAmount, // 应收金额
      receivedAmount, // 实付金额

      extraTradeId,
      extraTradeNo, // 退款记录单号
      applicantName, // 申请人
      returnTime, // 退款时间
      returnAmount, // 退款金额
    } = data;
    return {
      ...data,
      transactionDetailList: [{
        tradeNo,
        chargeName, // 所含收费项名称
        houseName, // 房源名称
        accountPeriod, // 所在账期
        receivableAmount, // 应收金额
        receivedAmount, // 实付金额
      }],
      returnMoneyRecords: [
        {
          extraTradeId,
          extraTradeNo, // 退款记录单号
          applicantName, // 申请人
          returnTime, // 退款时间
          returnAmount, // 退款金额
        },
      ],
      returnMoneyContent: [{
        extraTradeId,
        accountPeriod, // 所在账期
        chargeName, // 所含收费项名称
        houseName,
        returnAmount,
      }],
    };
  }

  async returnMoney(data:RentFeeReturnMoney) {
    await request<BeforeCheckoutRentFee>({
      url: URLS.returnMoney,
      data,
    });
  }

  async listMeterConsumptionPage<T>(searchData:RentFeeTransactionDetailSearchParams) {
    const { data } = await request<{list:RentFeeTransactionDetail[], total:number}>({
      url: URLS.listMeterConsumptionPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // --------------- 水电离场结算 ---------------------
  // 导出
  async exportmeterConsumptionLeave(searchData:any) {
    const { data, headers } = await request({
      url: URLS.exportmeterConsumptionLeave,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async findByIdmeterConsumptionLeave(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdmeterConsumptionLeave}${id}`,
      method: 'get',
    });
    return data;
  }

  async leaveSettlementmeterConsumptionLeave<T>(searchData:any) {
    const { data } = await request({
      url: URLS.leaveSettlementmeterConsumptionLeave,
      data: searchData,
    });
    return data;
  }

  async settlementPaymeterLeave(data:any) {
    await request({
      url: URLS.settlementPaymeterLeave,
      data,
    });
  }

  async listRentContractLeaveConsumptionLeave<T>(searchData:Partial<RentFeeSearchParams>) {
    const { data } = await request<{list:RentFeeInfo[], total:number}>({
      url: URLS.listRentContractLeaveConsumptionLeave,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  async getHouseListByContract(id:string) {
    const { data } = await request({
      url: `${URLS.getHouseListByContract}${id}`,
      method: 'get',
    });
    return data;
  }
}
