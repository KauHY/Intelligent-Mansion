/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:38:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-18 15:11:47
 */
import URLS from '~models/urlConstant/role';
import request from '~utils/axios';
import { RoleListItem, SearchParams, AddParams } from './type';

export default class Role {
  async searchListPage(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<RoleListItem>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 新增角色信息
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async add(data:AddParams) {
    await request({
      url: URLS.add,
      data,
    });
  }

  /**
   * @description:更新角色信息
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async update(data:AddParams) {
    await request({
      url: URLS.update,
      data,
    });
  }
  /**
   * @description: 更新角色状态
   * @param {*}
   * @return {*}
   * @author: GUOCHAO82
   */

  async upDateStatus(id:string, status:number) {
    await request({
      url: URLS.updateStatus,
      data: {
        id,
        status,
      },
    });
  }

  /**
   * @description:删除角色
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

  async getRolesListByOrgId(roleName:string) { // 根据组织id查询角色
    const { data } = await request({
      url: URLS.listRoles,
      data: {
        // orgId: params.orgId || '',
        roleName,
        status: 1,
      },
      method: 'post',
    });
    return (data.list || []) as SelectOptions;
  }

  /**
   * @description: 保存权限
   * @param {string} roleId
   * @param {0} type
   * @return {*}
   * @author: GUOCHAO82
   */
  async setPermission(roleId:string, menuBaseDestIds:string[], organizationBaseDestIds:string[]) {
    const menuDestIds = menuBaseDestIds.filter((id) => id !== '0');
    const organizationDestIds = organizationBaseDestIds.filter((id) => id !== '0');
    await request({
      url: URLS.setMenuPermissionList,
      data: {
        roleId,
        destIds: menuDestIds,
      },
    });
    await request({
      url: URLS.setDataPermissionList,
      data: {
        roleId,
        destIds: organizationDestIds,
      },
    });
  }

  async getPermission(roleId:string, type:0|2) {
    const url = type === 0 ? URLS.echoMenu : URLS.echoData;
    const { data } = await request({
      url,
      method: 'get',
      data: {
        roleId,
      },
    });
    return data as string[];
  }
}
export * from './type';
