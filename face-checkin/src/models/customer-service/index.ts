/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: your name
 * @LastEditTime: 2022-03-15 22:03:02
 */
import { encrypt } from '@/utils/tool';
import URLS from '~models/urlConstant/customer-service';
import request from '~utils/axios';
import {
  OrderSearchParams,
  OrderInfo,
  AssignUser,
  OrderCancelParams,
  OrderDetails,
  AnnouncementSearchParams,
  AnnouncementInfo,
  NoticeSearchParams,
  NoticeInfo,
  GoodsReleaseSearchParams,
  GoodsReleaseInfo,
  GoodsReleaseAudit,
} from './type';

export default class CustomerService {
  async orderSearchListPage<T>(searchData:OrderSearchParams) {
    const { data } = await request<{list:OrderInfo[], total:number}>({
      url: URLS.listOrderPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  /**
   * @description: 新报事情保修
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async orderAdd(data:OrderInfo) {
    await request({
      url: URLS.orderAdd,
      data,
    });
  }

  /**
   * @description:撤销
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async orderCancel(data:OrderCancelParams) {
    await request({
      url: URLS.orderCancel,
      data,
    });
  }

  async assignUser(data:AssignUser) {
    await request({
      url: URLS.orderAssignUser,
      data,
    });
  }

  async orderFindById(id:string) {
    const { data } = await request<Partial<OrderDetails>>({
      url: `${URLS.orderFindById}${id}`,
      method: 'get',
    });
    return data;
  }

  // --------------------------------------------公告------------------------------
  /**
   * @description: 公告分页查询
   * @param {AnnouncementSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async announcementSearchListPage<T>(searchData:AnnouncementSearchParams) {
    const { data } = await request<{list:AnnouncementInfo[], total:number}>({
      url: URLS.announcementListPage,
      method: 'POST',
      data: searchData,
    });
    // console.log(data, '-----data');
    return data;
  }

  /**
   * @description: 新增公告
   * @param {AnnouncementInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async announcementAdd(data:AnnouncementInfo) {
    await request({
      url: URLS.announcementAdd,
      data,
    });
  }

  /**
   * @description: 编辑公告
   * @param {AnnouncementInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async announcementEdit(data:AnnouncementInfo) {
    await request({
      url: URLS.announcementEdit,
      data,
    });
  }

  /**
   * @description: 删除公告
   * @param {string} id
   * @return {*}
   */
  async announcementDel(id:string) {
    await request({
      url: `${URLS.announcementDel}${id}`,
      method: 'delete',
    });
  }

  /**
   * @description: 公告根据id查询详情
   * @param {string} id
   * @return {*}
   */
  async announcementFindById(id:string) {
    const { data } = await request<AnnouncementInfo>({
      url: `${URLS.announcementFindById}${id}`,
      method: 'get',
    });
    return data;
  }

  // -------------------------------------咨询---------------------------------------------
  /**
   * @description:删除资讯
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async noticeDel(id:string) {
    await request({
      url: `${URLS.noticeDel}${id}`,
      method: 'delete',
    });
  }

  /**
   * @description:查询资讯id
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async informationFindById(id:string) {
    const { data } = await request({
      url: `${URLS.informationFindById}${id}`,
      method: 'get',
    });
    return data;
  }

  /**
   * @description: 咨询列表
   * @param {NoticeSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async noticeSearchListPage<T>(searchData:NoticeSearchParams) {
    const { data } = await request<{list:NoticeInfo[], total:number}>({
      url: URLS.listInfoPage,
      method: 'POST',
      data: searchData,
    });
    // const { list, total } = data;
    return data;
  }

  /**
   * @description: 新增咨讯
   * @param {NoticeInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async noticeAdd(data:NoticeInfo) {
    await request({
      url: URLS.noticeAdd,
      data,
    });
  }

  /**
     * @description: 编辑咨询
     * @param {NoticeInfo} data
     * @return {*}
     * @author: GUOCHAO82
     */
  async noticeEdit(data:NoticeInfo) {
    await request({
      url: URLS.noticeEdit,
      data,
    });
  }

  // ---------------------------------物品放行--------------------------------------------------

  /**
   * @description: 物品放行分页查询
   * @param {GoodsReleaseSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async goodsReleaseListPage<T>(searchData:GoodsReleaseSearchParams) {
    const { data } = await request<{list:GoodsReleaseInfo[], total:number}>({
      url: URLS.goodsReleaseListPage,
      method: 'POST',
      data: searchData,
    });
    // const { list, total } = data;
    return data;
  }

  /**
   * 导出
   */
  async exportGoodsRelease(searchData: any) {
    const { data, headers } = await request({
      url: URLS.exportGoodsRelease,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async goodsReleaseAudit(data:GoodsReleaseAudit) {
    await request({
      url: URLS.goodsReleaseAudit,
      data,
    });
  }

  async goodsReleaseFindById(id:string) {
    const { data } = await request<GoodsReleaseInfo>({
      url: `${URLS.goodsReleaseFindById}${id}`,
      method: 'get',
    });
    return data;
  }
}

export * from './type';
