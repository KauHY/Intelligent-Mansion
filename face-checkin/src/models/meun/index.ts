/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-24 18:04:23
 */
import URLS from '~models/urlConstant/meun';
import URL from '~models/urlConstant/company';
import request from '~utils/axios';
import SearchModel from '~models/base-model/searchModel';
import { getTree } from '~utils/tool';
import { SearchParams, RsMenuTree } from './type';

export default class Menu extends SearchModel {
  async searchListPage(searchData:SearchParams, init:boolean) {
    // this.changePage(null, null, init);
    const { data } = await request({
      url: URLS.listPage,
      method: 'get',
      data: searchData,
    });
    // const { list, total } = data;
    // debugger;
    const listTree = getTree<MenuItem>(data);
    // console.log(listTree, '---listTree');
    return {
      list: <Array<MenuItem>>listTree,
      total: listTree.length,
    };
  }

  /**
   * @description: 查询用户所有可操作的菜单
   * @param {*}
   * @return {*}
   * @author: GUOCHAO82
   */
  async getUserMenuList() {
    const { data } = await request({
      url: window.$localStorage.isCompany === 1 ? URL.getMenuList : URLS.menuList,
      method: window.$localStorage.isCompany === 1 ? 'post' : 'get',
    });
    return data as Array<MenuItem>;
  }

  async add<T>(data:T) {
    await request({
      url: URLS.createMenu,
      data,
    });
  }

  async edit(data:MenuItem) {
    data.icon = data.icon || '';
    await request({
      url: URLS.update,
      data,
    });
  }

  /**
   * @description: 更改订单状态
   * @param {*}
   * @return {*}
   * @author: GUOCHAO82
   */
  async upDateStatus(id:string, status:number) {
    await request({
      url: URLS.updateStatus,
      data: {
        menuId: id,
        status,
      },
    });
  }

  async del(id:string) {
    await request({
      url: `${URLS.remove}${id}`,
      method: 'delete',
    });
  }

  /**
   * @description: 获取当前用户的菜单树
   * @param {*}
   * @return {*}
   * @author: GUOCHAO82
   */
  async getRsMenuTree() {
    const { data } = await request({
      url: URLS.rsMenuTree,
      method: 'get',
    });
    return data as Array<RsMenuTree>;
  }

  /**
   * @description: 重置菜单
   * @param {*}
   * @return {*}
   * @author: GUOCHAO82
   */
  async resetMenu() {
    await request({
      url: URLS.resetMenu,
      method: 'put',
    });
  }
}

export * from './type';
