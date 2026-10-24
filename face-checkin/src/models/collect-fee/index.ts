/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-17 11:13:47
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-20 16:21:09
 */
import URLS from '~models/urlConstant/collect-fee';
import request from '~utils/axios';
import {
  ChargeItemSearchParams,
  ChargeItem,
  BillingSchemeSearchParams,
  BillingScheme,
  Calculation,
  settlementPayParams,
  leaveSettlementCreatedParams,
} from './type';

export default class {
  // ------------------------收费项管理-----------------------------------
  /**
   * @description: 收费项分页查询
   * @param {ChargeItemSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async chargeItemListPage<T>(searchData:ChargeItemSearchParams) {
    const { data } = await request({
      url: URLS.chargeItemListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<ChargeItem>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 删除
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async chargeItemDel(id:string) {
    await request({
      url: `${URLS.chargeItemDel}${id}`,
      method: 'DELETE',
    });
  }

  async chargeItemAdd(data:ChargeItem) {
    await request({
      url: URLS.chargeItemAdd,
      data,
    });
  }

  /**
   * @description: 修改状态
   * @param {object} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async chargeItemSetState(data:{id:string, status:0|1}) {
    await request({
      url: URLS.chargeItemSetState,
      data,
    });
  }

  async chargeItemModify(data:ChargeItem) {
    await request({
      url: URLS.chargeItemModify,
      data,
    });
  }

  async chargeItemFindById(id:string) {
    const { data } = await request({
      url: `${URLS.chargeItemFindById}${id}`,
      method: 'get',
    });
    return data as ChargeItem;
  }

  /**
   * @description: 收费项下拉数据
   * @param {string} parkOrgId
   * @return {*}
   * @author: GUOCHAO82
   */
  async chargeItemDict(parkOrgId:string) {
    const { data } = await request({
      url: URLS.chargeItemDict,
      data: {
        parkOrgId,
      },
      method: 'get',
    });
    return data as SelectOptions;
  }
  // -------------------------------计费方案管理---------------------------------------

  /**
   * @description: 计费方案分页查询
   * @param {BillingSchemeSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async billingSchemeListPage<T>(searchData:BillingSchemeSearchParams) {
    const { data } = await request({
      url: URLS.billingSchemeListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<BillingScheme>>list,
      total: <number>total,
    };
  }

  // 费用模块根据类型获取合同下拉列表
  async getDictListByType(companyId:string, parkOrgId:string, type:number) {
    const { data } = await request({
      url: `${URLS.getDictListByType}?companyId=${companyId}&&parkOrgId=${parkOrgId}&&type=${type}`,
      method: 'get',
    });
    return data;
  }

  /**
       * @description: 删除
       * @param {string} id
       * @return {*}
       * @author: GUOCHAO82
       */
  async billingSchemeDel(id:string) {
    await request({
      url: `${URLS.billingSchemeDel}${id}`,
      method: 'DELETE',
    });
  }

  async billingSchemeAdd(data:BillingScheme) {
    await request({
      url: URLS.billingSchemeAdd,
      data,
    });
  }

  /**
       * @description: 修改状态
       * @param {object} data
       * @return {*}
       * @author: GUOCHAO82
       */
  async billingSchemeSetState(data:{id:string, status:0|1}) {
    await request({
      url: URLS.billingSchemeSetState,
      data,
    });
  }

  async billingSchemeModify(data:BillingScheme) {
    await request({
      url: URLS.billingSchemeModify,
      data,
    });
  }

  async billingSchemeFindById(id:string) {
    const { data } = await request({
      url: `${URLS.billingSchemeFindById}${id}`,
      method: 'get',
    });
    return data as BillingScheme;
  }

  async getCalculationListByFeeType(id:string) {
    const { data } = await request({
      url: `${URLS.getCalculationListByFeeType}?id=${id}`,
      method: 'get',
    });
    return data as SelectOptions;
  }

  async findByIdleaveSettlement(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdleaveSettlement}${id}`,
      method: 'get',
    });
    return data as BillingScheme;
  }

  async leaveSettlement<T>(searchData:leaveSettlementCreatedParams) {
    const { data } = await request({
      url: URLS.leaveSettlement,
      data: searchData,
    });
    return data;
  }

  async listRentContractLeavePage<T>(searchData:BillingSchemeSearchParams) {
    const { data } = await request({
      url: URLS.listRentContractLeavePage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<BillingScheme>>list,
      total: <number>total,
    };
  }

  async settlementPay(data:any) {
    await request({
      url: URLS.settlementPay,
      data,
    });
  }

  // 导出
  async exportsettlement(searchData: any) {
    const { data, headers } = await request({
      url: URLS.exportsettlement,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async calculationApi(params:Calculation) {
    const { data } = await request({
      url: `${URLS.calculation}`,
      method: 'post',
      data: params,
    });
    return data;
  }

  async computeAmount(params:any) {
    const { data } = await request({
      url: `${URLS.computeAmount}`,
      method: 'post',
      data: params,
    });
    return data;
  }
}
export * from './type';

export { RentFee } from './rent-fee'; // 租赁费用
