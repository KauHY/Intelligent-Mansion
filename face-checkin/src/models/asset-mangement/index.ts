/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-29 13:46:46
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 15:38:49
 */
import URLS from '@/models/urlConstant/asset-mangement';
import request from '~utils/axios';
import {
  GoodsCard,
  SearchManagerParams,
  SetWarning,
  GoodsInStock,
  ReceiveDetails,
  ReceiveAuditSetting,
} from './type';

export default class Park {
  /**
   * 物品管理
   */
  // 物品管理 - 分页列表
  async goodsListPage<T>(searchData:SearchManagerParams, init:boolean) {
    const { data } = await request({
      url: URLS.goodsListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<GoodsCard>>list,
      total: <number>total,
    };
  }

  // 新增巡检地点
  async patrolPlaceAdd(data:GoodsCard) {
    await request({
      url: URLS.patrolPlaceAdd,
      data,
    });
  }

  // 巡检地点删除
  async patrolPlaceDel(id:string) {
    await request({
      url: `${URLS.patrolPlaceDel}${id}`,
      method: 'delete',
    });
  }

  // 物品编辑
  async goodsEdit(data:GoodsCard) {
    await request({
      url: URLS.goodsEdit,
      data,
    });
  }

  // 预警值
  async settingMinStock(data:SetWarning) {
    await request({
      url: URLS.settingMinStock,
      data,
    });
  }

  /**
   * 库存管理
   */
  // 巡检计划新增
  async inventoryInGoodsAdd(data: GoodsInStock) {
    await request({
      url: URLS.inventoryStockAdd,
      data,
    });
  }

  // 库存管理分页查询
  async inventoryListPage<T>(searchData:SearchManagerParams, init:boolean) {
    const { data } = await request({
      url: URLS.inventoryListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<GoodsCard>>list,
      total: <number>total,
    };
  }

  // 出入库编辑
  async inventoryOutInStockEdit(data: GoodsInStock) {
    await request({
      url: URLS.inventoryOutInStockEdit,
      data,
    });
  }

  /**
   * 领用管理
   */

  // 领用管理 - 分页查询
  async receiveListPage<T>(searchData:SearchManagerParams, init:boolean) {
    const { data } = await request({
      url: URLS.receiveListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<GoodsCard>>list,
      total: <number>total,
    };
  }

  // 领用管理 - 审核出库
  async auditReceive(data: ReceiveDetails) {
    await request({
      url: URLS.auditReceive,
      data,
    });
  }

  // 领用管理 - 获取详情
  async getReceiveDetail(id: string) {
    const { data } = await request({
      url: URLS.getReceiveDetail + id,
      method: 'get',
    });
    return data;
  }

  // 领用管理 - 审核入库
  async auditReturn(data: ReceiveDetails) {
    await request({
      url: URLS.auditReturn,
      data,
    });
  }

  /**
   * 领用权限设置
   */

  // 领用权限设置 - 分页查询
  async receiveAuditListPage<T>(searchData:SearchManagerParams, init:boolean) {
    const { data } = await request({
      url: URLS.receiveAuditListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<GoodsCard>>list,
      total: <number>total,
    };
  }

  // 领用权限设置 - 权限设置
  async receiveAuditSetting(data: ReceiveAuditSetting) {
    await request({
      url: URLS.receiveAuditSetting,
      data,
    });
  }

  /**
   * 出入库
   */

  // 领用权限设置 - 分页查询
  async outInstockRecordListPage<T>(searchData:SearchManagerParams, init:boolean) {
    const { data } = await request({
      url: URLS.outInstockRecordListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<GoodsCard>>list,
      total: <number>total,
    };
  }

  /**
   * 盘点管理
   */
  // 盘点管理 - 分页查询
  async inventoryManagerListPage<T>(searchData:SearchManagerParams, init:boolean) {
    const { data } = await request({
      url: URLS.inventoryManagerListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<GoodsCard>>list,
      total: <number>total,
    };
  }

  // 领用权限设置 - 权限设置
  async inventoryManagerDetails(id: string) {
    return request({
      url: URLS.inventoryManagerDetails + id,
      method: 'GET',
    });
  }

  /**
   * 导出
   */
  // 物品管理 - 导出
  async goodsListPageExport(searchData: any) {
    const { data, headers } = await request({
      url: URLS.goodsListPageExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  /**
   * 导出
   */
  // 物品管理 - 导入
  async goodsListPageImport(searchData: any) {
    const { data } = await request({
      url: URLS.goodsListPageImport,
      data: searchData,
    });
    return data;
  }

  // 出入库管理 - 导出
  async outInstockRecordListPageExport(searchData: any) {
    const { data, headers } = await request({
      url: URLS.outInstockRecordListPageExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 盘点管理 - 导出
  async inventoryManagerListPageExport(searchData: any) {
    const { data, headers } = await request({
      url: URLS.inventoryManagerListPageExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  //  下载 导入失败的文件
  async downloadFailFile(key: string) {
    const { data, headers } = await request({
      url: URLS.downloadFile + key,
      // data: searchData,
      responseType: 'blob',
      method: 'GET',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }
}

export * from './type';
