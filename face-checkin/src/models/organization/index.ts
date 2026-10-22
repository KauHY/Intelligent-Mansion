/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-22 20:14:20
 */
import URLS from '~models/urlConstant/organization';
import request from '~utils/axios';
import {
  OrganizationItem, SearchParams, AddOrganization, OrganizationListItem,
} from './type';

export default class Organization {
/**
 * @description: 根据用户查询组织数
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
  async getOrganizationByParentId(parentId:string = '') {
    const { data } = await request({
      url: URLS.organizationByParentId,
      data: {
        parentId,
      },
      method: 'get',
    });
    return data as Array<OrganizationItem|never>;
  }

  /**
   * @description: 根据组织名查询组织树
   * @param {string} orgName
   * @return {*}
   * @author: GUOCHAO82
   */
  async getOrganizationTreeByorgName(orgName:string = '') {
    const { data } = await request({
      url: URLS.organizationTree,
      data: {
        param: orgName,
      },
    });
    return [data] as Array<OrganizationItem|never>;
  }

  async searchListPage<T>(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<OrganizationListItem>>list,
      total: <number>total,
    };
  }

  //   /**
  //    * @description: 新增组织
  //    * @param {T} data
  //    * @return {*}
  //    * @author: GUOCHAO82
  //    */
  async add(data:AddOrganization) {
    await request({
      url: URLS.add,
      data,
    });
  }

  /**
     * @description: 更新组织
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */

  async update(data:AddOrganization) {
    await request({
      url: URLS.update,
      data,
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
      url: `${URLS.del}${id}`,
      method: 'delete',
    });
  }
}
export * from './type';
