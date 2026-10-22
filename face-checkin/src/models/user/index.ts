/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-11 19:13:50
 */
import { encrypt } from '@/utils/tool';
import URLS from '~models/urlConstant/user';
import request from '~utils/axios';
import {
  LoginFormData,
  UserInfo,
  UserListItem,
  UserPwdReset,
  SearchParams,
  AddUserParams,
  RsUserListItem,
  UserPwdUpdate,
  ValidateIdentity,
  ResetPwdForForget,
} from './type';

export default class User {
  async login(formData: LoginFormData) { // 查询可见的园区名
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

  /**
   * @description: 新增用户信息
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async save(data:AddUserParams) {
    // data.password = encrypt(data.password);
    await request({
      url: URLS.save,
      data,
    });
  }

  /**
   * @description: 保存用户信息
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async update(data:AddUserParams) {
    await request({
      url: URLS.update,
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

  async updateCompanyPwd(data:UserPwdUpdate) { // 修改密码
    const { newPassword, newPasswordAgain, oldPassword } = data;
    await request({
      url: URLS.updateCompanyPwd,
      data: {
        oldPassword: encrypt(oldPassword),
        newPassword: encrypt(newPassword),
        newPasswordAgain: encrypt(newPasswordAgain),
      },
    });
  }

  async validateIdentity(params:ValidateIdentity) { // 忘记密码身份校验
    // const { newPassword, newPasswordAgain, oldPassword } = data;
    const { data } = await request({
      url: URLS.validateIdentity,
      data: params,
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

  async validateCompanyIdentity(params:ValidateIdentity) { // 忘记密码身份校验
    // const { newPassword, newPasswordAgain, oldPassword } = data;
    const { data } = await request({
      url: URLS.validateCompanyIdentity,
      data: params,
    });
    return data as string;
  }

  async resetPwdCompanyForForget(data:ResetPwdForForget) { // 忘记密码重置密码
    const { newPassword, newPasswordAgain } = data;
    await request({
      url: URLS.resetPwdCompanyForForget,
      data: {
        ...data,
        newPassword: encrypt(newPassword),
        newPasswordAgain: encrypt(newPasswordAgain),
      },
    });
  }

  // 企业端
  async getSmsCode(mobile: string) {
    const { data } = await request({
      url: URLS.getResetPwdSmsCode,
      method: 'post',
      data: {
        mobile,
      },
    });
    return data as UserInfo;
  }

  // 运营端
  async getBusSmsCode(mobile: string) {
    const { data } = await request({
      url: URLS.getSmsCode,
      method: 'post',
      data: {
        mobile,
      },
    });
    return data as UserInfo;
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
        param: value,
      },
    });
    return data.list as SelectOptions;
  }
}
export * from './type';
