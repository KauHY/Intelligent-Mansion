/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-11 19:13:50
 */
import { encrypt } from '@/utils/tool';
import URLS from '~models/urlConstant/company';
import request from '~utils/axios';
import {
  LoginFormData,
  UserInfo,
  UserListItem,
  UserPwdReset,
  SearchParams,
  AddUserParams,
  SupplySearchParams,
  SupplyListItem,
  UserPwdUpdate,
  ValidateIdentity,
  ResetPwdForForget,
  PurchaseListItem,
  createSearchParams,
  AuditParams,
} from './type';

export default class User {
  async login(formData: LoginFormData) {
    const { data } = await request({
      url: URLS.login,
      method: 'post',
      data: {
        ...formData,
        password: encrypt(formData.password),
      },
    });
    return data as UserInfo;
  }

  async getSmsCode(mobile: string) {
    const { data } = await request({
      url: URLS.getSmsCode,
      method: 'post',
      data: {
        mobile,
      },
    });
    return data as UserInfo;
  }

  async logout() { // 查询可见的园区名
    await request({
      url: URLS.logout,
      method: 'post',
    });
  }

  async searchListPage<T>(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<UserListItem>>list,
      total: <number>total,
    };
  }

  async cancelInfor(id:string) {
    const { data } = await request({
      url: `${URLS.cancel}${id}`,
      method: 'get',
    });
    return data;
  }

  async listParkDictByCode(id:any) {
    const { data } = await request({
      url: `${URLS.listParkDictByCode}?code=${id}`,
      method: 'get',
    });
    return data;
  }

  async findById(id:string) {
    const { data } = await request({
      url: `${URLS.findById}${id}`,
      method: 'get',
    });
    return data;
  }

  async listCompanyPage(param:any) {
    const { data } = await request({
      url: URLS.listCompanyPage,
      data: param,
    });
    const { list } = data;
    return list;
  }

  /**
   * @description: 获取当前用户的企业详情
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async getDetail() {
    const { data } = await request({
      url: URLS.getDetail,
    });
    return data;
  }

  /**
   * @description: 是否完善信息
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async getFlag() {
    const { data } = await request({
      url: URLS.getFlag,
    });
    return data;
  }

  /**
   * @description: 是否入驻
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async settled() {
    const { data } = await request({
      url: URLS.settled,
      method: 'get',
    });
    return data;
  }

  /**
   * @description: 获取当前用户的企业详情
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async listCompanyInformationAuditPage(searchData:AuditParams) {
    const { data } = await request({
      url: URLS.listCompanyInformationAuditPage,
      data: searchData,
    });
    return data;
  }

  /**
   * @description: 获取当前用户的企业详情
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async listCompanyAuditPage(searchData:AuditParams) {
    const { data } = await request({
      url: URLS.listCompanyAuditPage,
      data: searchData,
    });
    return data;
  }

  /**
   * @description: 获取当前最新企业审核记录
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async auditInfos() {
    const { data } = await request({
      url: URLS.getLastAudit,
    });
    return data;
  }

  /**
   * @description: 保存用户信息
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async update(data:AddUserParams) {
    await request({
      url: URLS.create,
      data,
    });
  }

  /**
   * @description: 保存用户信息
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async modifyInfos(data:AddUserParams) {
    await request({
      url: URLS.modifyInfos,
      data,
    });
  }

  async resetPwd(data:UserPwdReset) { // 用户管理重置密码
    const { newPassword, newPasswordAgain, userId } = data;
    await request({
      url: URLS.resetPwd,
      data: {
        newPassword: encrypt(newPassword),
        newPasswordAgain: encrypt(newPasswordAgain),
        userId,
      },
    });
  }

  async updatePwd(data:UserPwdUpdate) { // 修改密码
    const { newPassword, newPasswordAgain, oldPassword } = data;
    await request({
      url: URLS.updatePwd,
      data: {
        oldPassword: encrypt(oldPassword),
        newPassword: encrypt(newPassword),
        newPasswordAgain: encrypt(newPasswordAgain),
      },
    });
  }

  async register(params:ValidateIdentity) { // 注册
    // const { newPassword, newPasswordAgain, oldPassword } = data;
    const { data } = await request({
      url: URLS.register,
      data: {
        ...params,
        password: encrypt(params.password),
      },
    });
    return data as string;
  }

  async resetPwdForForget(data:ResetPwdForForget) { // 忘记密码重置密码
    const { newPassword, newPasswordAgain } = data;
    await request({
      url: URLS.resetPwdForForget,
      data: {
        ...data,
        newPassword: encrypt(newPassword),
        newPasswordAgain: encrypt(newPasswordAgain),
      },
    });
  }

  /**
   * @description: 更新用户状态
   * @param {*}
   * @return {*}
   * @author: GUOCHAO82
   */

  async upDateStatus(id:string, status:0|1) {
    // const url = status === 1 ? URLS.enable : URLS.disable;
    await request({
      // url: `${url}${id}`,
      url: URLS.updateStatus,
      data: {
        id,
        status,
      },
    });
  }

  /**
   * @description:删除用户
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async del(id:string) {
    await request({
      url: `${URLS.remove}${id}`,
      method: 'delete',
    });
  }

  async rsUserList({ value, orgId }:{value:string, orgId:string}) {
    const { data } = await request({
      url: URLS.rsUserList,
      data: {
        username: value,
        orgId,
      },
    });
    return data.list as SelectOptions;
  }

  /**
   * @description:供应服务
   */
  async listSupplyServicePageSupply<T>(searchData:SupplySearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listSupplyServicePageSupply,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<SupplyListItem>>list,
      total: <number>total,
    };
  }

  async findByIdSupply(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdSupply}${id}`,
      method: 'get',
    });
    return data;
  }

  async removeSupply(id:string) {
    const { data } = await request({
      url: `${URLS.removeSupply}${id}`,
      method: 'delete',
    });
    return data;
  }

  async createSupply(data:SupplyListItem) {
    await request({
      url: URLS.createSupply,
      data,
    });
  }

  async createAudit(data:createSearchParams) {
    await request({
      url: URLS.createAudit,
      data,
    });
  }

  async cancelAudit(id:string) {
    const { data } = await request({
      url: `${URLS.cancelAudit}${id}`,
      method: 'get',
    });
    return data;
  }

  async modifySupply(data:SupplyListItem) {
    await request({
      url: URLS.modifySupply,
      data,
    });
  }

  /**
   * @description:采购服务
   */
  async listPurchaseServicePage<T>(searchData:SupplySearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listPurchaseServicePage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<SupplyListItem>>list,
      total: <number>total,
    };
  }

  async listTenderRecordPage<T>(searchData:SupplySearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listTenderRecordPage,
      method: 'POST',
      data: { ...searchData, purchaseId: window.$localStorage.detailId },
    });
    const { list, total } = data;
    return {
      list: <Array<SupplyListItem>>list,
      total: <number>total,
    };
  }

  async confirmTenderRecord(id:string) {
    const { data } = await request({
      url: `${URLS.confirmTenderRecord}${id}`,
      method: 'get',
    });
    return data;
  }

  /**
   * 导出
   */
  async exportTenderRecord(searchData: any) {
    const { data, headers } = await request({
      url: URLS.exportTenderRecord,
      data: { ...searchData, purchaseId: window.$localStorage.detailId },
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async findByIdPurchaseService(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdPurchaseService}${id}`,
      method: 'get',
    });
    return data;
  }

  async createService(data:PurchaseListItem) {
    await request({
      url: URLS.createService,
      data,
    });
  }

  async modifyService(data:PurchaseListItem) {
    await request({
      url: URLS.modifyService,
      data,
    });
  }

  async changeState(id:string, state:number) {
    await request({
      url: URLS.changeState,
      data: {
        id,
        state,
      },
    });
  }

  async changePurchaseState(id:string, state:number) {
    await request({
      url: URLS.changePurchaseState,
      data: {
        id,
        state,
      },
    });
  }

  async removeService(id:string) {
    const { data } = await request({
      url: `${URLS.removeService}${id}`,
      method: 'delete',
    });
    return data;
  }

  /**
   * @description:企业端费用账单-分页查询
   */
  async listCompanyPortBillPage<T>(searchData:SupplySearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listCompanyPortBillPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<SupplyListItem>>list,
      total: <number>total,
    };
  }

  async uploadFile(data:any) {
    await request({
      url: URLS.uploadFile,
      data,
    });
  }

  /**
   * @description:企业端水电费用账单-分页查询
   */
  async listMeterPortBillPage<T>(searchData:SupplySearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listMeterPortBillPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<SupplyListItem>>list,
      total: <number>total,
    };
  }

  async uploadMeterFile(data:any) {
    await request({
      url: URLS.uploadMeterFile,
      data,
    });
  }

  async findByIdConsumptionBill(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdConsumptionBill}${id}`,
      method: 'get',
    });
    return data;
  }

  async findByIdRentContractBill(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdRentContractBill}${id}`,
      method: 'get',
    });
    return data;
  }
}
export * from './type';
